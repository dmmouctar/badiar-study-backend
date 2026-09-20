package com.badiar.badiar_study.academic.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO de réponse pour un examen.
 * Retourné par GET /api/v1/academic/examinations
 *
 * Contient toutes les infos nécessaires au frontend :
 * type d'examen, matière, semestre, ordre de la note.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExaminationResponse {

    private Long    id;
    private Integer examOrder; // 1=note1, 2=note2, 3=note3
    private LocalDate examDate;

    // Infos du type d'examen
    private Long   examinationTypeId;
    private String examinationTypeName;

    // Infos de la matière
    private Long    subjectId;
    private String  subjectName;
    private java.math.BigDecimal subjectCoefficient;

    // Infos du semestre (remontées pour faciliter l'affichage)
    private Long    semesterId;
    private String  semesterName;
    private Integer semesterDisplayOrder;

    // Infos de la filière
    private Long    academicProgramId;
    private String  academicProgramName;
    private Integer gradingScale;

    // Infos de l'année académique
    private Long   academicYearId;
    private String academicYearName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}