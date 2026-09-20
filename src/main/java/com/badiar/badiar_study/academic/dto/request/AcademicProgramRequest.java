package com.badiar.badiar_study.academic.dto.request;

import com.badiar.badiar_study.common.constant.AcademicConstants;
import com.badiar.badiar_study.common.constant.ValidationConstants;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO pour créer ou modifier une filière.
 * Utilisé par POST et PUT /api/v1/academic/programs
 */
@Getter
@Setter
@NoArgsConstructor
public class AcademicProgramRequest {

    @NotBlank(message = "Le nom de la filière est obligatoire")
    @Size(
            min = ValidationConstants.ACADEMIC_NAME_MIN_LENGTH,
            max = ValidationConstants.ACADEMIC_NAME_MAX_LENGTH,
            message = "Le nom doit contenir entre "
                    + ValidationConstants.ACADEMIC_NAME_MIN_LENGTH + " et "
                    + ValidationConstants.ACADEMIC_NAME_MAX_LENGTH + " caractères"
    )
    private String name;

    @Size(max = ValidationConstants.DESCRIPTION_MAX_LENGTH,
            message = "La description ne doit pas dépasser "
                    + ValidationConstants.DESCRIPTION_MAX_LENGTH + " caractères")
    private String description;

    /**
     * Barème de notation : 10 (université) ou 20 (lycée/collège).
     */
    @NotNull(message = "Le barème est obligatoire")
    @Min(value = 10, message = "Le barème doit être "
            + AcademicConstants.GRADING_SCALE_UNIVERSITY
            + " (université) ou "
            + AcademicConstants.GRADING_SCALE_SECONDARY
            + " (lycée/collège)")
    @Max(value = 20, message = "Le barème doit être "
            + AcademicConstants.GRADING_SCALE_UNIVERSITY
            + " (université) ou "
            + AcademicConstants.GRADING_SCALE_SECONDARY
            + " (lycée/collège)")
    private Integer gradingScale;
}
