package com.badiar.badiar_study.academic.dto.request;

import com.badiar.badiar_study.common.constant.AcademicConstants;
import com.badiar.badiar_study.common.constant.ValidationConstants;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO pour créer ou modifier une matière.
 * Utilisé par POST et PUT /api/v1/academic/subjects
 *
 * La matière a un double lien :
 * - academicProgramId : filière directe
 * - semesterId        : semestre/module/trimestre précis
 */
@Getter
@Setter
@NoArgsConstructor
public class SubjectRequest {

    @NotNull(message = "L'identifiant de la filière est obligatoire")
    private Long academicProgramId;

    @NotNull(message = "L'identifiant du semestre est obligatoire")
    private Long semesterId;

    @NotBlank(message = "Le nom de la matière est obligatoire")
    @Size(
            min = ValidationConstants.ACADEMIC_NAME_MIN_LENGTH,
            max = ValidationConstants.ACADEMIC_NAME_MAX_LENGTH,
            message = "Le nom doit contenir entre "
                    + ValidationConstants.ACADEMIC_NAME_MIN_LENGTH + " et "
                    + ValidationConstants.ACADEMIC_NAME_MAX_LENGTH + " caractères"
    )
    private String name;

    @NotNull(message = "Le coefficient est obligatoire")
    @DecimalMin(
            value = "0.5",
            message = "Le coefficient minimum est " + AcademicConstants.COEFFICIENT_MIN
    )
    @DecimalMax(
            value = "10.0",
            message = "Le coefficient maximum est " + AcademicConstants.COEFFICIENT_MAX
    )
    private BigDecimal coefficient;
}
