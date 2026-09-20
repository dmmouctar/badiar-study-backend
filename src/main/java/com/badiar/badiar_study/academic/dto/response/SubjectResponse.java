package com.badiar.badiar_study.academic.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de réponse pour une matière.
 * Retourné par GET /api/v1/academic/subjects
 *
 * Contient toutes les infos nécessaires au frontend :
 * nom, coefficient, filière, semestre, année, niveau.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubjectResponse {

    private Long       id;
    private String     name;
    private BigDecimal coefficient;

    // Infos de la filière (double lien — lien direct)
    private Long    academicProgramId;
    private String  academicProgramName;
    private Integer gradingScale;

    // Infos du semestre
    private Long    semesterId;
    private String  semesterName;
    private Integer semesterDisplayOrder;

    // Infos du niveau (remontées pour faciliter l'affichage)
    private Long    programYearLevelId;
    private String  levelLabel;
    private Integer levelNumber;

    // Infos de l'année académique
    private Long   academicYearId;
    private String academicYearName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
