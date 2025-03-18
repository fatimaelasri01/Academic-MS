package pfe.mandomati.academicms.Service.Impl;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pfe.mandomati.academicms.Client.IamClient;
import pfe.mandomati.academicms.Dto.IamDto;
import pfe.mandomati.academicms.Dto.StudentDto;
import pfe.mandomati.academicms.Dto.UserDto;
import pfe.mandomati.academicms.Model.StudentAcademicProfile;
import pfe.mandomati.academicms.Repository.ClassRepository;
import pfe.mandomati.academicms.Repository.StudentAcademicProfileRepository;
import pfe.mandomati.academicms.Service.StudentService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class StudentServiceImpl implements StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Autowired
    private IamClient iamClient;

    @Autowired
    private StudentAcademicProfileRepository studentAcademicProfileRepository;

    @Autowired
    private ClassRepository classRepository;

    @Override
    @Transactional
    public ResponseEntity<String> registerUser(UserDto userDTO) {
        Long studentId = null;
        Long parentId = null;
        try {
            // Ajouter l'étudiant dans IAMMS
            IamDto.Role studentRole = IamDto.Role.builder()
                    .id(5L)
                    .name("STUDENT")
                    .build();

            IamDto iamStudentDto = IamDto.builder()
                    .username(userDTO.getUsername())
                    .lastname(userDTO.getLastname())
                    .firstname(userDTO.getFirstname())
                    .email(userDTO.getEmail())
                    .password(userDTO.getPassword())
                    .status(userDTO.isStatus())
                    .address(userDTO.getAddress())
                    .birthDate(userDTO.getBirthDate())
                    .city(userDTO.getCity())
                    .createdAt(userDTO.getCreatedAt())
                    .role(studentRole) // Utilisation de l'objet Role
                    .build();

            logger.info("Sending student data to IAMMS: {}", iamStudentDto);

            ResponseEntity<String> studentResponse = iamClient.registerUser(iamStudentDto);
            if (!studentResponse.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Failed to create student in IAMMS");
            }

            logger.info("student register in IAMMS: {}", studentResponse.getBody());

            // Extraire l'ID de la réponse
            studentId = extractIdFromResponse(studentResponse.getBody());
            logger.info("studentId: {}", studentId);

            // Vérifier si le parent existe déjà dans la base de données
            List<StudentAcademicProfile> existingParentProfiles = studentAcademicProfileRepository.findByParentEmail(userDTO.getParentEmail());
            if (existingParentProfiles.isEmpty()) {
                // Ajouter le parent dans IAMMS
                IamDto.Role parentRole = IamDto.Role.builder()
                        .id(6L)
                        .name("PARENT")
                        .build();

                IamDto iamParentDto = IamDto.builder()
                        .username(userDTO.getParentUsername())
                        .lastname(userDTO.getParentFirstname())
                        .firstname(userDTO.getParentLastname())
                        .email(userDTO.getParentEmail())
                        .password(userDTO.getParentPassword())
                        .status(userDTO.isParentStatus())
                        .address(userDTO.getParentAddress())
                        .birthDate(userDTO.getParentBirthDate())
                        .city(userDTO.getParentCity())
                        .createdAt(userDTO.getCreatedAt())
                        .role(parentRole) // Utilisation de l'objet Role
                        .build();

                logger.info("Sending parent data to IAMMS: {}", iamParentDto);

                ResponseEntity<String> parentResponse = iamClient.registerUser(iamParentDto);
                if (!parentResponse.getStatusCode().is2xxSuccessful()) {
                    logger.error("Failed to create parent in IAMMS. Deleting student from IAMMS: {}", userDTO.getUsername());
                    iamClient.deleteUser(URLEncoder.encode(userDTO.getUsername(), StandardCharsets.UTF_8.toString()));
                    throw new RuntimeException("Failed to create parent in IAMMS");
                }

                logger.info("parent register in IAMMS: {}", parentResponse.getBody());

                // Extraire l'ID de la réponse
                parentId = extractIdFromResponse(parentResponse.getBody());
                logger.info("parentId: {}", parentId);
            } else {
                // Utiliser le premier profil parent trouvé
                StudentAcademicProfile existingParentProfile = existingParentProfiles.get(0);
                parentId = existingParentProfile.getParentId();
                logger.info("Parent already exists in academicms: {}", existingParentProfile);
            }

            StudentAcademicProfile studentProfile = StudentAcademicProfile.builder()
                    .cne(userDTO.getCne())
                    .studentId(studentId)
                    .classId(classRepository.findIdByName(userDTO.getClassName()))
                    .admissionDate(userDTO.getAdmissionDate())
                    .academicStatus(userDTO.getAcademicStatus())
                    .parentId(parentId)
                    .parentName(userDTO.getParentFirstname() + " " + userDTO.getParentLastname())
                    .parentContact(userDTO.getParentContact())
                    .parentEmail(userDTO.getParentEmail())
                    .build();

            logger.info("save studentprofile {}", studentProfile);

            studentAcademicProfileRepository.save(studentProfile);

            logger.info("student profile saved successfully in academicms");

            return ResponseEntity.ok("User registered successfully");

        } catch (Exception e) {
            logger.error("Failed to register user", e);
            // Supprimer le parent et l'étudiant de IAMMS en cas d'erreur
            if (parentId != null) {
                try {
                    iamClient.deleteUser(URLEncoder.encode(userDTO.getParentUsername(), StandardCharsets.UTF_8.toString()));
                    logger.info("Deleted parent from IAMMS: {}", userDTO.getParentUsername());
                } catch (Exception ex) {
                    logger.error("Failed to delete parent from IAMMS", ex);
                }
            }
            if (studentId != null) {
                try {
                    iamClient.deleteUser(URLEncoder.encode(userDTO.getUsername(), StandardCharsets.UTF_8.toString()));
                    logger.info("Deleted student from IAMMS: {}", userDTO.getUsername());
                } catch (Exception ex) {
                    logger.error("Failed to delete student from IAMMS", ex);
                }
            }
            throw new RuntimeException("Failed to register user", e);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> updateStudent(Long id, UserDto userDto) {
        try {
            StudentAcademicProfile existingStudentProfile = studentAcademicProfileRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            // Mettre à jour les informations de l'étudiant dans IAMMS
            IamDto iamStudentDto = IamDto.builder()
                    .username(userDto.getUsername())
                    .lastname(userDto.getLastname())
                    .firstname(userDto.getFirstname())
                    .email(userDto.getEmail())
                    .password(userDto.getPassword())
                    .status(userDto.isStatus())
                    .address(userDto.getAddress())
                    .birthDate(userDto.getBirthDate())
                    .city(userDto.getCity())
                    .createdAt(userDto.getCreatedAt())
                    .role(IamDto.Role.builder().id(5L).name("STUDENT").build()) // Utilisation de l'objet Role
                    .build();

            iamClient.editUser(URLEncoder.encode(userDto.getUsername(), StandardCharsets.UTF_8.toString()), iamStudentDto);

            // Mettre à jour les informations de l'étudiant dans Academic-MS
            existingStudentProfile.setCne(userDto.getCne());
            existingStudentProfile.setClassId(classRepository.findIdByName(userDto.getClassName()));
            existingStudentProfile.setAdmissionDate(userDto.getAdmissionDate());
            existingStudentProfile.setAcademicStatus(userDto.getAcademicStatus());
            existingStudentProfile.setAssurance(userDto.isAssurance());
            existingStudentProfile.setParentId(existingStudentProfile.getParentId());
            existingStudentProfile.setParentName(userDto.getParentFirstname() + " " + userDto.getParentLastname());
            existingStudentProfile.setParentContact(userDto.getParentContact());
            existingStudentProfile.setParentEmail(userDto.getParentEmail());

            studentAcademicProfileRepository.save(existingStudentProfile);

            // Mettre à jour les informations du parent dans IAMMS
            IamDto iamParentDto = IamDto.builder()
                    .username(userDto.getParentUsername())
                    .lastname(userDto.getParentFirstname())
                    .firstname(userDto.getParentLastname())
                    .email(userDto.getParentEmail())
                    .password(userDto.getParentPassword())
                    .status(userDto.isParentStatus())
                    .address(userDto.getParentAddress())
                    .birthDate(userDto.getParentBirthDate())
                    .city(userDto.getParentCity())
                    .createdAt(userDto.getCreatedAt())
                    .role(IamDto.Role.builder().id(6L).name("PARENT").build()) // Utilisation de l'objet Role
                    .build();

            iamClient.editUser(URLEncoder.encode(userDto.getParentUsername(), StandardCharsets.UTF_8.toString()), iamParentDto);

            // Mettre à jour les informations du parent pour tous les étudiants liés
            List<StudentAcademicProfile> parentProfiles = studentAcademicProfileRepository.findByParentId(existingStudentProfile.getParentId());
            for (StudentAcademicProfile profile : parentProfiles) {
                profile.setParentName(userDto.getParentFirstname() + " " + userDto.getParentLastname());
                profile.setParentContact(userDto.getParentContact());
                profile.setParentEmail(userDto.getParentEmail());
                studentAcademicProfileRepository.save(profile);
            }

            return ResponseEntity.ok("Student updated successfully");
        } catch (Exception e) {
            logger.error("Failed to update student", e);
            throw new RuntimeException("Failed to update student", e);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteStudent(Long id) {
        try {
            StudentAcademicProfile existingStudentProfile = studentAcademicProfileRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            Long parentId = existingStudentProfile.getParentId();
            String studentUsername = existingStudentProfile.getParentEmail();
            studentAcademicProfileRepository.deleteById(id);

            // Supprimer l'étudiant de IAMMS
            iamClient.deleteUser(URLEncoder.encode(studentUsername, StandardCharsets.UTF_8.toString()));
            logger.info("Deleted student from IAMMS: {}", studentUsername);

            // Vérifier si le parent est lié à d'autres étudiants
            List<StudentAcademicProfile> remainingProfiles = studentAcademicProfileRepository.findByParentId(parentId);
            if (remainingProfiles.isEmpty()) {
                // Supprimer le parent de IAMMS
                iamClient.deleteUser(URLEncoder.encode(existingStudentProfile.getParentEmail(), StandardCharsets.UTF_8.toString()));
                logger.info("Deleted parent from IAMMS: {}", existingStudentProfile.getParentEmail());
            }

            return ResponseEntity.ok("Student deleted successfully");
        } catch (Exception e) {
            logger.error("Failed to delete student", e);
            throw new RuntimeException("Failed to delete student", e);
        }
    }

    private Long extractIdFromResponse(String responseBody) {
        Pattern pattern = Pattern.compile("ID: (\\d+)");
        Matcher matcher = pattern.matcher(responseBody);
        if (matcher.find()) {
            return Long.parseLong(matcher.group(1));
        } else {
            throw new RuntimeException("Failed to extract ID from response: " + responseBody);
        }
    }

    @Override
    public List<StudentDto> getAllStudents() {
        return studentAcademicProfileRepository.findAll().stream()
                .map(s -> new StudentDto(
                        s.getStudentId(),
                        s.getCne(),
                        s.getClassId(),
                        s.getAdmissionDate(),
                        s.getAcademicStatus(),
                        false, s.getParentId(),
                                                s.getParentName(),
                                                s.getParentContact(),
                                                s.getParentEmail(), null, null
                ))
                .toList();
    }

    @Override
    public StudentDto getStudentByStudentId(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStudentByStudentId'");
    }

    @Override
    public StudentDto createOrUpdateStudentProfile(StudentDto studentDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createOrUpdateStudentProfile'");
    }

    @Override
    public void deleteStudentProfile(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteStudentProfile'");
    }

    @Override
    public List<StudentDto> getStudentsByClassId(Long classId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStudentsByClassId'");
    }

    @Override
    public List<StudentDto> getStudentsByCne(String cne) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStudentsByCne'");
    }

    @Override
    public List<StudentDto> getStudentsByAdmissionDate(Date admissionDate) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStudentsByAdmissionDate'");
    }
}