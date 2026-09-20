package com.badiar.badiar_study.enrollment.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateStudentRequest {

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(max = 100)
    private String firstName;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 100)
    private String lastName;

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
    @Min(value = 1)
    private Integer currentLevel;
}