package com.badiar.badiar_study.enrollment.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateStudentRequest {

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(max = 100, message = "Le prénom ne peut pas dépasser 100 caractères")
    private String firstName;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 100, message = "Le nom ne peut pas dépasser 100 caractères")
    private String lastName;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email n'est pas valide")
    @Size(max = 150, message = "L'email ne peut pas dépasser 150 caractères")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    private String password;

    @NotBlank(message = "Le matricule est obligatoire")
    @Size(max = 30, message = "Le matricule ne peut pas dépasser 30 caractères")
    private String registrationNumber;

    private LocalDate dateOfBirth;

    @Size(max = 255)
    private String address;

    @Size(max = 20)
    private String phoneNumber;

    private Long genderId;

    @NotNull(message = "La filière est obligatoire")
    private Long academicProgramId;

    @NotNull(message = "L'année académique est obligatoire")
    private Long academicYearId;

    @NotNull(message = "Le niveau est obligatoire")
    @Min(value = 1, message = "Le niveau doit être au moins 1")
    private Integer currentLevel;
}