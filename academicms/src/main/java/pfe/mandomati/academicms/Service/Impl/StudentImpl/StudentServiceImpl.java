package pfe.mandomati.academicms.Service.Impl.StudentImpl;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pfe.mandomati.academicms.Client.IamClient;
import pfe.mandomati.academicms.Dto.StudentDto.*;
import pfe.mandomati.academicms.Model.Student.Student;
import pfe.mandomati.academicms.Repository.ClassRepo.ClassRepository;

import pfe.mandomati.academicms.Repository.StudentRepo.StudentRepository;
import pfe.mandomati.academicms.Service.StudentService.StudentService;
import pfe.mandomati.academicms.Model.Class.Class;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Base64;


@Slf4j
@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService {

    //private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);
    private final IamClient iamClient;

    private final StudentRepository studentAcademicProfileRepository;

    private final ClassRepository classRepository;

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

            log.info("Sending student data to IAMMS: {}", iamStudentDto);

            ResponseEntity<String> studentResponse = iamClient.registerUser(iamStudentDto);
            if (!studentResponse.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Failed to create student in IAMMS");
            }

            log.info("student register in IAMMS: {}", studentResponse.getBody());

            // Extraire l'ID de la réponse
            studentId = extractIdFromResponse(studentResponse.getBody());
            log.info("studentId: {}", studentId);

            // Vérifier si le parent existe déjà dans la base de données
            List<Student> existingParentProfiles = studentAcademicProfileRepository.findByParentEmail(userDTO.getParentEmail());
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

                log.info("Sending parent data to IAMMS: {}", iamParentDto);

                ResponseEntity<String> parentResponse = iamClient.registerUser(iamParentDto);
                if (!parentResponse.getStatusCode().is2xxSuccessful()) {
                    log.error("Failed to create parent in IAMMS. Deleting student from IAMMS: {}", userDTO.getUsername());
                    iamClient.deleteUser(URLEncoder.encode(userDTO.getUsername(), StandardCharsets.UTF_8.toString()));
                    throw new RuntimeException("Failed to create parent in IAMMS");
                }

                log.info("parent register in IAMMS: {}", parentResponse.getBody());

                // Extraire l'ID de la réponse
                parentId = extractIdFromResponse(parentResponse.getBody());
                log.info("parentId: {}", parentId);
            } else {
                // Utiliser le premier profil parent trouvé
                Student existingParentProfile = existingParentProfiles.get(0);
                parentId = existingParentProfile.getParentId();
                log.info("Parent already exists in academicms: {}", existingParentProfile);
            }

            // Rechercher la classe en fonction de filiereName et numero
            Class classProfile = classRepository.findByFiliereNameAndNumero(userDTO.getFiliereName(), userDTO.getNumero())
                .orElseThrow(() -> new RuntimeException("Class not found with filiereName: " + userDTO.getFiliereName() + " and numero: " + userDTO.getNumero()));

            // Vérifier la capacité de la classe
            long currentStudentCount = studentAcademicProfileRepository.countBySchoolClass(classProfile);
            if (currentStudentCount >= classProfile.getCapacity()) {
                throw new RuntimeException("Class capacity exceeded for class: " + userDTO.getFiliereName() + userDTO.getNumero());
            }

            Student studentProfile = Student.builder()
                    .cne(userDTO.getCne())
                    .studentId(studentId)
                    .schoolClass(classProfile)
                    .admissionDate(userDTO.getAdmissionDate())
                    .academicStatus(userDTO.getAcademicStatus())
                    .parentId(parentId)
                    .parentName(userDTO.getParentFirstname() + " " + userDTO.getParentLastname())
                    .parentContact(userDTO.getParentContact())
                    .parentEmail(userDTO.getParentEmail())
                    .build();

            log.info("save studentprofile {}", studentProfile);

            studentAcademicProfileRepository.save(studentProfile);

            log.info("student profile saved successfully in academicms");

            return ResponseEntity.ok("User registered successfully");

        } catch (Exception e) {
            log.error("Failed to register user", e);
            // Supprimer le parent et l'étudiant de IAMMS en cas d'erreur
            if (parentId != null) {
                try {
                    iamClient.deleteUser(URLEncoder.encode(userDTO.getParentUsername(), StandardCharsets.UTF_8.toString()));
                    log.info("Deleted parent from IAMMS: {}", userDTO.getParentUsername());
                } catch (Exception ex) {
                    log.error("Failed to delete parent from IAMMS", ex);
                }
            }
            if (studentId != null) {
                try {
                    iamClient.deleteUser(URLEncoder.encode(userDTO.getUsername(), StandardCharsets.UTF_8.toString()));
                    log.info("Deleted student from IAMMS: {}", userDTO.getUsername());
                } catch (Exception ex) {
                    log.error("Failed to delete student from IAMMS", ex);
                }
            }
            throw new RuntimeException("Failed to register user", e);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> updateStudent(Long id, UserDto userDto) {
        try {
            Student existingStudentProfile = studentAcademicProfileRepository.findById(id)
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

            // Rechercher la classe en fonction de filiereName et numero
            Class classProfile = classRepository.findByFiliereNameAndNumero(userDto.getFiliereName(), userDto.getNumero())
                .orElseThrow(() -> new RuntimeException("Class not found with filiereName: " + userDto.getFiliereName() + " and numero: " + userDto.getNumero()));

            // Vérifier la capacité de la classe
            long currentStudentCount = studentAcademicProfileRepository.countBySchoolClass(classProfile);
            if (currentStudentCount >= classProfile.getCapacity()) {
                throw new RuntimeException("Class capacity exceeded for class: " + userDto.getFiliereName() + userDto.getNumero());
            }

            // Mettre à jour les informations de l'étudiant dans Academic-MS
            existingStudentProfile.setCne(userDto.getCne());
            existingStudentProfile.setSchoolClass(classProfile);
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
            List<Student> parentProfiles = studentAcademicProfileRepository.findByParentId(existingStudentProfile.getParentId());
            for (Student profile : parentProfiles) {
                profile.setParentName(userDto.getParentFirstname() + " " + userDto.getParentLastname());
                profile.setParentContact(userDto.getParentContact());
                profile.setParentEmail(userDto.getParentEmail());
                studentAcademicProfileRepository.save(profile);
            }

            return ResponseEntity.ok("Student updated successfully");
        } catch (Exception e) {
            log.error("Failed to update student", e);
            throw new RuntimeException("Failed to update student", e);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteStudent(Long id) {
        try {
            Student existingStudentProfile = studentAcademicProfileRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            Long parentId = existingStudentProfile.getParentId();
            ResponseEntity<IamDto> iamStudent = iamClient.getStudentById(id.toString());
            if (!iamStudent.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Failed to retrieve student from IAMMS");
            }
            String studentUsername = iamStudent.getBody().getUsername();
            studentAcademicProfileRepository.deleteById(id);

            // Supprimer l'étudiant de IAMMS
            iamClient.deleteUser(URLEncoder.encode(studentUsername, StandardCharsets.UTF_8.toString()));
            log.info("Deleted student from IAMMS: {}", studentUsername);

            // Vérifier si le parent est lié à d'autres étudiants
            List<Student> remainingProfiles = studentAcademicProfileRepository.findByParentId(parentId);
            if (remainingProfiles.isEmpty()) {
                // Supprimer le parent de IAMMS
                iamClient.deleteUser(URLEncoder.encode(existingStudentProfile.getParentEmail(), StandardCharsets.UTF_8.toString()));
                log.info("Deleted parent from IAMMS: {}", existingStudentProfile.getParentEmail());
            }

            return ResponseEntity.ok("Student deleted successfully");
        } catch (Exception e) {
            log.error("Failed to delete student", e);
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
        try {
            List<IamDto> iamStudents = iamClient.getAllStudents().getBody();
            if (iamStudents == null) {
                throw new RuntimeException("Failed to retrieve students from IAMMS");
            }
            log.info("Retrieved students from IAMMS: {}", iamStudents);
            return iamStudents.stream().map(iamStudent -> {
                log.info("Processing IAM student: {}", iamStudent);
                if (iamStudent.getId() == null) {
                    log.error("Student ID is null for IAM student: {}", iamStudent);
                    throw new RuntimeException("Student ID is null for IAM student");
                }
                Optional<Student> profileOptional = studentAcademicProfileRepository.findById(iamStudent.getId());
                Student profile = profileOptional.orElseThrow(() -> 
                    new RuntimeException("Student profile not found for studentId: " + iamStudent.getId()));
                return convertToStudentDto(profile, iamStudent);
            }).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to get all students", e);
            throw new RuntimeException("Failed to get all students", e);
        }
    }

    @Override
    public StudentDto getStudentByStudentId(Long id) {
        try {

            log.info("Getting student by id: {}", id);

            ResponseEntity<IamDto> responseEntity = iamClient.getStudentById(id.toString());

            log.info("Response entity: {}", responseEntity);

            IamDto iamStudent = responseEntity.getBody();

            log.info("Retrieved student from IAMMS: {}", iamStudent);

            if (iamStudent == null) {
                throw new RuntimeException("Failed to retrieve student from IAMMS for id: " + id);
            }
            Optional<Student> profileOptional = studentAcademicProfileRepository.findById(id);
            Student profile = profileOptional.orElseThrow(() -> 
                new RuntimeException("Student profile not found for studentId: " + id));
            return convertToStudentDto(profile, iamStudent);
        } catch (Exception e) {
            log.error("Failed to get student by id", e);
            throw new RuntimeException("Failed to get student by id", e);
        }
    }

    @Override
    public List<StudentDto> getStudentsByClassId(Long classId) {
        try {
            // Vérifier si la classe existe
            Class classProfile = classRepository.findById(classId)
                    .orElseThrow(() -> new RuntimeException("Class not found with id: " + classId));
    
            // Récupérer tous les étudiants
            List<StudentDto> allStudents = getAllStudents();
    
            // Filtrer les étudiants par classId
            return allStudents.stream()
                    .filter(student -> student.getClassName().equals(classProfile.getFiliere().getName() + classProfile.getNumero()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to get students by class id", e);
            throw new RuntimeException("Failed to get students by class id", e);
        }
    }

    @Override
    public StudentDto getStudentByCne(String cne) {
        try {
            log.info("Getting student by CNE: {}", cne);
    
            // Rechercher l'étudiant dans la base de données par CNE
            Student student = studentAcademicProfileRepository.findByCne(cne)
                    .orElseThrow(() -> new RuntimeException("Student not found with CNE: " + cne));
    
            // Récupérer les informations de l'étudiant depuis IAMMS
            ResponseEntity<IamDto> responseEntity = iamClient.getStudentById(student.getStudentId().toString());
            IamDto iamStudent = responseEntity.getBody();
            return convertToStudentDto(student, iamStudent);
        } catch (Exception e) {
            log.error("Failed to get student by CNE", e);
            throw new RuntimeException("Failed to get student by CNE", e);
        }
    }
    
    @Override
    public List<StudentDto> getStudentsByAdmissionDate(Date admissionDate) {
        try {
            log.info("Getting students by admission date: {}", admissionDate);
    
            // Rechercher les étudiants dans la base de données par date d'admission
            List<Student> students = studentAcademicProfileRepository.findByAdmissionDate(admissionDate);
    
            if (students.isEmpty()) {
                throw new RuntimeException("No students found with admission date: " + admissionDate);
            }
    
            // Construire et retourner la liste des DTOs des étudiants
            return students.stream().map(student -> {
                ResponseEntity<IamDto> responseEntity = iamClient.getStudentById(student.getStudentId().toString());
                IamDto iamStudent = responseEntity.getBody();
                return convertToStudentDto(student, iamStudent);
            }).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to get students by admission date", e);
            throw new RuntimeException("Failed to get students by admission date", e);
        }
    }

    @Override
    public List<StudentDto> getStudentByFullName(String firstname, String lastname) {
        try {
            log.info("Getting students by full name: {} {}", firstname, lastname);
    
            // Appeler IAMMS pour récupérer les utilisateurs par prénom et nom
            ResponseEntity<List<IamDto>> responseEntity = iamClient.getUsersByFirstnameAndLastname(firstname, lastname);
            List<IamDto> users = responseEntity.getBody();
    
            if (users == null || users.isEmpty()) {
                throw new RuntimeException("No users found with full name: " + firstname + " " + lastname);
            }
    
            // Filtrer uniquement les étudiants
            List<IamDto> students = users.stream()
                    .filter(user -> user.getRole() != null && "STUDENT".equalsIgnoreCase(user.getRole().getName()))
                    .collect(Collectors.toList());
    
            if (students.isEmpty()) {
                throw new RuntimeException("No students found with full name: " + firstname + " " + lastname);
            }
    
            // Construire la liste des profils académiques des étudiants
            return students.stream().map(iamStudent -> {
                Optional<Student> profileOptional = studentAcademicProfileRepository.findById(iamStudent.getId());
                Student profile = profileOptional.orElseThrow(() -> 
                    new RuntimeException("Student profile not found for studentId: " + iamStudent.getId()));
                return convertToStudentDto(profile, iamStudent);
            }).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to get students by full name", e);
            throw new RuntimeException("Failed to get students by full name", e);
        }
    }

    @Override
    public StudentDto getStudentByEmail(String email) {
        try {
            log.info("Getting student by email: {}", email);
    
            // Appeler IAMMS pour récupérer l'utilisateur par email
            ResponseEntity<IamDto> responseEntity = iamClient.getUserByEmail(email);
            IamDto iamStudent = responseEntity.getBody();
    
            if (iamStudent == null) {
                throw new RuntimeException("Student not found in IAMMS with email: " + email);
            }
    
            // Récupérer le profil académique de l'étudiant
            Optional<Student> profileOptional = studentAcademicProfileRepository.findById(iamStudent.getId());
            Student profile = profileOptional.orElseThrow(() -> 
                new RuntimeException("Student profile not found for studentId: " + iamStudent.getId()));
    
            return convertToStudentDto(profile, iamStudent);
        } catch (Exception e) {
            log.error("Failed to get student by email", e);
            throw new RuntimeException("Failed to get student by email", e);
        }
    }

    @Override
    public StudentDto getStudentByToken(String token) {
        try {
            log.info("Extracting username from token: {}", token);
    
            // Extraire le username à partir du token
            String username = extractUsernameFromToken(token);
            if (username == null || username.isEmpty()) {
                throw new RuntimeException("Invalid token: Unable to extract username");
            }
    
            log.info("Username extracted from token: {}", username);
    
            // Récupérer les informations de l'utilisateur à partir du service IAMMS
            ResponseEntity<IamDto> responseEntity = iamClient.getUserByUsername(username);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                IamDto iamStudent = responseEntity.getBody();
                if (iamStudent == null || iamStudent.getId() == null) {
                    throw new RuntimeException("User not found in IAMMS for username: " + username);
                }
    
                log.info("Retrieved student from IAMMS: {}", iamStudent);
    
                // Retrieve academic profile
                Optional<Student> profileOptional = studentAcademicProfileRepository.findById(iamStudent.getId());
                Student profile = profileOptional.orElseThrow(() ->
                    new RuntimeException("Student profile not found for studentId: " + iamStudent.getId()));
    
                return convertToStudentDto(profile, iamStudent);
            } else {
                // Handle non-JSON responses (e.g., plain text)
                String errorMessage = responseEntity.getBody() != null ? responseEntity.getBody().toString() : "Unknown error";
                throw new RuntimeException("Failed to retrieve user from IAMMS: " + errorMessage);
            }
        } catch (Exception e) {
            log.error("Failed to get student by token", e);
            throw new RuntimeException("Failed to get student by token", e);
        }
    }

    private String extractUsernameFromToken(String token) {
        try {
            String payload = token.split("\\.")[1];
            String decodedPayload = new String(Base64.getUrlDecoder().decode(payload), StandardCharsets.UTF_8);
    
            // Utiliser Jackson pour convertir le payload JSON en Map
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonMap = objectMapper.readValue(decodedPayload, new TypeReference<Map<String, Object>>() {});
    
            return (String) jsonMap.get("preferred_username");
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid token format or unable to extract username", e);
        }
    }

    private StudentDto convertToStudentDto(Student student, IamDto iamStudent) {
        if (iamStudent == null) {
            throw new RuntimeException("IAM student data is null for studentId: " + student.getStudentId());
        }

        // Construire className à partir de filiereName et numero
        String className = student.getSchoolClass().getFiliere().getName() + student.getSchoolClass().getNumero();

        return StudentDto.builder()
                .id(iamStudent.getId())
                .cne(student.getCne())
                .username(iamStudent.getUsername())
                .firstname(iamStudent.getFirstname())
                .lastname(iamStudent.getLastname())
                .email(iamStudent.getEmail())
                .city(iamStudent.getCity())
                .address(iamStudent.getAddress())
                .birthDate(iamStudent.getBirthDate())
                .assurance(student.isAssurance())
                .parentName(student.getParentName())
                .parentContact(student.getParentContact())
                .parentEmail(student.getParentEmail())
                .className(className)
                .admissionDate(student.getAdmissionDate())
                .academicStatus(student.getAcademicStatus())
                .build();
    }
    
}