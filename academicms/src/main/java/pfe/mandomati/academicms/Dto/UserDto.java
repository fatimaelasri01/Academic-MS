package pfe.mandomati.academicms.Dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    //informations de l'utilisateur student
    private String username;

    private String lastname;

    private String firstname;

    private String email;

    private String password;

    private boolean status = true;

    private String address;

    private LocalDate birthDate;

    private String city;

    private LocalDateTime createdAt;



    private String cne;

    private Long classId;

    private Date admissionDate;

    private String academicStatus;

    private boolean assurance;

    //informations de l'utilisateur parent
    private String parentUsername;

    private String parentFirstname;

    private String parentLastname;

    private String parentContact;

    private String parentEmail;

    private String parentPassword;

    private boolean parentStatus = true;

    private String parentAddress;

    private LocalDate parentBirthDate;

    private String parentCity;


}
