package com.badiar.badiar_study.academic.dto.request;

import com.badiar.badiar_study.common.constant.ValidationConstants;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO pour créer ou modifier une liaison Filière + Année + Niveau.
 * Utilisé par POST et PUT /api/v1/academic/program-year-levels
 *
 * Exemple : Génie Informatique + 2024-2025 + niveau 4 (Licence 4)
 */
@Getter
@Setter
@NoArgsConstructor
public class ProgramYearLevelRequest {

    @NotNull(message = "L'identifiant de la filière est obligatoire")
    private Long academicProgramId;

    @NotNull(message = "L'identifiant de l'année académique est obligatoire")
    private Long academicYearId;

    @NotNull(message = "Le numéro de niveau est obligatoire")
    @Min(
            value = ValidationConstants.LEVEL_MIN,
            message = "Le niveau minimum est " + ValidationConstants.LEVEL_MIN
    )
    @Max(
            value = ValidationConstants.LEVEL_MAX,
            message = "Le niveau maximum est " + ValidationConstants.LEVEL_MAX
    )
    private Integer levelNumber;

    @NotBlank(message = "Le libellé du niveau est obligatoire")
    @Size(max = 50, message = "Le libellé ne doit pas dépasser 50 caractères")
    private String levelLabel;
}
