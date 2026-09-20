package com.badiar.badiar_study.academic.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de réponse pour une liaison Filière + Année + Niveau.
 * Retourné par GET /api/v1/academic/program-year-levels
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProgramYearLevelResponse {

    private Long   id;

    // Infos de la filière
    private Long   academicProgramId;
    private String academicProgramName;
    private Integer gradingScale;

    // Infos de l'année académique
    private Long   academicYearId;
    private String academicYearName;

    // Infos du niveau
    private Integer       levelNumber;
    private String        levelLabel;
    private LocalDateTime createdAt;
}
