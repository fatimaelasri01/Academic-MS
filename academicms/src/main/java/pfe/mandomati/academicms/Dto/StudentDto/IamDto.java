package pfe.mandomati.academicms.Dto.StudentDto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class IamDto {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String firstname;
    private String lastname;
    private boolean status;
    private LocalDate birthDate;
    private String address;
    private String city;
    private LocalDateTime createdAt;
    private Role role; // Ajout de l'objet Role

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public static class Role {
        private Long id;
        private String name;
    }
}