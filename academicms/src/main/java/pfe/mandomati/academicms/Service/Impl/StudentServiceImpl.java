package pfe.mandomati.academicms.Service.Impl;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pfe.mandomati.academicms.Client.IamClient;
import pfe.mandomati.academicms.Dto.IamDto;
import pfe.mandomati.academicms.Dto.StudentDto;
import pfe.mandomati.academicms.Dto.UserDto;
import pfe.mandomati.academicms.Model.Student;
import pfe.mandomati.academicms.Repository.ClassRepository;

import pfe.mandomati.academicms.Repository.StudentRepository;
import pfe.mandomati.academicms.Service.StudentService;
import pfe.mandomati.academicms.Model.Class;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.json.JSONObject;
import java.util.Base64;


@Service
public class StudentServiceImpl implements StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Autowired
    private IamClient iamClient;

    @Autowired
    private StudentRepository studentAcademicProfileRepository;

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
                Student existingParentProfile = existingParentProfiles.get(0);
                parentId = existingParentProfile.getParentId();
                logger.info("Parent already exists in academicms: {}", existingParentProfile);
            }

            Class classProfile = classRepository.findById(userDTO.getClassId())
                    .orElseThrow(() -> new RuntimeException("Class not found"));

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

            Class classProfile = classRepository.findById(userDto.getClassId())
                    .orElseThrow(() -> new RuntimeException("Class not found"));

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
            logger.error("Failed to update student", e);
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
            logger.info("Deleted student from IAMMS: {}", studentUsername);

            // Vérifier si le parent est lié à d'autres étudiants
            List<Student> remainingProfiles = studentAcademicProfileRepository.findByParentId(parentId);
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
        try {
            List<IamDto> iamStudents = iamClient.getAllStudents().getBody();
            if (iamStudents == null) {
                throw new RuntimeException("Failed to retrieve students from IAMMS");
            }
            logger.info("Retrieved students from IAMMS: {}", iamStudents);
            return iamStudents.stream().map(iamStudent -> {
                logger.info("Processing IAM student: {}", iamStudent);
                if (iamStudent.getId() == null) {
                    logger.error("Student ID is null for IAM student: {}", iamStudent);
                    throw new RuntimeException("Student ID is null for IAM student");
                }
                Optional<Student> profileOptional = studentAcademicProfileRepository.findById(iamStudent.getId());
                Student profile = profileOptional.orElseThrow(() -> 
                    new RuntimeException("Student profile not found for studentId: " + iamStudent.getId()));
                return convertToStudentDto(profile, iamStudent);
            }).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Failed to get all students", e);
            throw new RuntimeException("Failed to get all students", e);
        }
    }

    @Override
    public StudentDto getStudentByStudentId(Long id) {
        try {

            logger.info("Getting student by id: {}", id);

            ResponseEntity<IamDto> responseEntity = iamClient.getStudentById(id.toString());

            logger.info("Response entity: {}", responseEntity);

            IamDto iamStudent = responseEntity.getBody();

            logger.info("Retrieved student from IAMMS: {}", iamStudent);

            if (iamStudent == null) {
                throw new RuntimeException("Failed to retrieve student from IAMMS for id: " + id);
            }
            Optional<Student> profileOptional = studentAcademicProfileRepository.findById(id);
            Student profile = profileOptional.orElseThrow(() -> 
                new RuntimeException("Student profile not found for studentId: " + id));
            return convertToStudentDto(profile, iamStudent);
        } catch (Exception e) {
            logger.error("Failed to get student by id", e);
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
                    .filter(student -> student.getClassId().equals(classId))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Failed to get students by class id", e);
            throw new RuntimeException("Failed to get students by class id", e);
        }
    }

    @Override
    public StudentDto getStudentByCne(String cne) {
        try {
            logger.info("Getting student by CNE: {}", cne);
    
            // Rechercher l'étudiant dans la base de données par CNE
            Student student = studentAcademicProfileRepository.findByCne(cne)
                    .orElseThrow(() -> new RuntimeException("Student not found with CNE: " + cne));
    
            // Récupérer les informations de l'étudiant depuis IAMMS
            ResponseEntity<IamDto> responseEntity = iamClient.getStudentById(student.getStudentId().toString());
            IamDto iamStudent = responseEntity.getBody();
            return convertToStudentDto(student, iamStudent);
        } catch (Exception e) {
            logger.error("Failed to get student by CNE", e);
            throw new RuntimeException("Failed to get student by CNE", e);
        }
    }
    
    @Override
    public List<StudentDto> getStudentsByAdmissionDate(Date admissionDate) {
        try {
            logger.info("Getting students by admission date: {}", admissionDate);
    
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
            logger.error("Failed to get students by admission date", e);
            throw new RuntimeException("Failed to get students by admission date", e);
        }
    }

    @Override
    public List<StudentDto> getStudentByFullName(String firstname, String lastname) {
        try {
            logger.info("Getting students by full name: {} {}", firstname, lastname);
    
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
            logger.error("Failed to get students by full name", e);
            throw new RuntimeException("Failed to get students by full name", e);
        }
    }

    @Override
    public StudentDto getStudentByEmail(String email) {
        try {
            logger.info("Getting student by email: {}", email);
    
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
            logger.error("Failed to get student by email", e);
            throw new RuntimeException("Failed to get student by email", e);
        }
    }

    @Override
    public StudentDto getStudentByToken(String token) {
        try {
            logger.info("Extracting username from token: {}", token);
    
            // Extraire le username à partir du token
            String username = extractUsernameFromToken(token);
            if (username == null || username.isEmpty()) {
                throw new RuntimeException("Invalid token: Unable to extract username");
            }
    
            logger.info("Username extracted from token: {}", username);
    
            // Récupérer les informations de l'utilisateur à partir du service IAMMS
            ResponseEntity<IamDto> responseEntity = iamClient.getUserByUsername(username);
            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Failed to retrieve user from IAMMS for username: " + username);
            }
    
            IamDto iamStudent = responseEntity.getBody();
            if (iamStudent == null || iamStudent.getId() == null) {
                throw new RuntimeException("User not found in IAMMS for username: " + username);
            }
    
            logger.info("Retrieved student from IAMMS: {}", iamStudent);
    
            // Récupérer le profil académique de l'étudiant
            Optional<Student> profileOptional = studentAcademicProfileRepository.findById(iamStudent.getId());
            Student profile = profileOptional.orElseThrow(() -> 
                new RuntimeException("Student profile not found for studentId: " + iamStudent.getId()));
    
            // Convertir en DTO et retourner
            return convertToStudentDto(profile, iamStudent);
        } catch (Exception e) {
            logger.error("Failed to get student by token", e);
            throw new RuntimeException("Failed to get student by token", e);
        }
    }

    private String extractUsernameFromToken(String token) {
        try {
            // Séparer le token en 3 parties : Header, Payload, Signature
            String payload = token.split("\\.")[1];

            // Décoder le payload en base64
            String decodedPayload = new String(Base64.getUrlDecoder().decode(payload), StandardCharsets.UTF_8);

            // Extraire l'username du payload (en supposant que l'username soit sous la clé "preferred_username")
            JSONObject json = new JSONObject(decodedPayload);
            return json.getString("preferred_username");
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid token format or unable to extract username", e);
        }
    }

    private StudentDto convertToStudentDto(Student student, IamDto iamStudent) {
        if (iamStudent == null) {
            throw new RuntimeException("IAM student data is null for studentId: " + student.getStudentId());
        }

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
                .classId(student.getSchoolClass().getId())
                .admissionDate(student.getAdmissionDate())
                .academicStatus(student.getAcademicStatus())
                .build();
    }
    
}