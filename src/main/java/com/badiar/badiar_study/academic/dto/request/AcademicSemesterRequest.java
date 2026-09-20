package com.badiar.badiar_study.academic.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO pour créer ou modifier un semestre/module/trimestre.
 * Utilisé par POST et PUT /api/v1/academic/semesters
 *
 * Exemples :
 * - Université : name="Module 1", displayOrder=1
 * - Lycée      : name="Trimestre 1", displayOrder=1
 */
@Getter
@Setter
@NoArgsConstructor
public class AcademicSemesterRequest {

    @NotNull(message = "L'identifiant du niveau de programme est obligatoire")
    private Long programYearLevelId;

    @NotBlank(message = "Le nom du semestre est obligatoire")
    @Size(max = 50, message = "Le nom ne doit pas dépasser 50 caractères")
    private String name;

    @NotNull(message = "L'ordre d'affichage est obligatoire")
    @Min(value = 1, message = "L'ordre minimum est 1")
    private Integer displayOrder;

    private LocalDate startDate;
    private LocalDate endDate;
}
