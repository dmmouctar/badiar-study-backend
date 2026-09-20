package com.badiar.badiar_study.academic.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO pour créer ou modifier une année académique.
 * Utilisé par POST et PUT /api/v1/academic/years
 */
@Getter
@Setter
@NoArgsConstructor
public class AcademicYearRequest {

    @NotBlank(message = "Le nom de l'année académique est obligatoire")
    @Pattern(
            regexp = "^\\d{4}-\\d{4}$",
            message = "Le format doit être AAAA-AAAA (ex: 2024-2025)"
    )
    private String name;

    @NotNull(message = "La date de début est obligatoire")
    private LocalDate startDate;

    @NotNull(message = "La date de fin est obligatoire")
    private LocalDate endDate;
}
