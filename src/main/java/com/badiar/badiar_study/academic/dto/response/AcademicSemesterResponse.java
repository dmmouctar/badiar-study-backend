package com.badiar.badiar_study.academic.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO de réponse pour un semestre/module/trimestre.
 * Retourné par GET /api/v1/academic/semesters
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AcademicSemesterResponse {

    private Long   id;
    private String name;
    private Integer displayOrder;

    // Infos du niveau de programme parent
    private Long   programYearLevelId;
    private String levelLabel;

    // Infos de la filière (remontées pour faciliter l'affichage)
    private Long   academicProgramId;
    private String academicProgramName;

    // Infos de l'année académique
    private Long   academicYearId;
    private String academicYearName;

    private LocalDate     startDate;
    private LocalDate     endDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
