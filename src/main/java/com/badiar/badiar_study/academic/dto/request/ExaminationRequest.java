package com.badiar.badiar_study.academic.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO pour créer ou modifier un examen.
 * Utilisé par POST et PUT /api/v1/academic/examinations
 *
 * Université : examOrder=1 (note1), examOrder=2 (note2), examOrder=3 (note3)
 * Lycée      : examOrder=1 (mois1), examOrder=2 (mois2), examOrder=3 (mois3)
 */
@Getter
@Setter
@NoArgsConstructor
public class ExaminationRequest {

    @NotNull(message = "L'identifiant de la matière est obligatoire")
    private Long subjectId;

    @NotNull(message = "L'identifiant du type d'examen est obligatoire")
    private Long examinationTypeId;

    private LocalDate examDate;

    @NotNull(message = "L'ordre de l'examen est obligatoire")
    @Min(value = 1, message = "L'ordre minimum est 1")
    @Max(value = 3, message = "L'ordre maximum est 3")
    private Integer examOrder;
}
