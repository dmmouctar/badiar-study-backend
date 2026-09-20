package com.badiar.badiar_study.grading.dto.response;

import com.badiar.badiar_study.grading.entity.Grade;
import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class GradeResponse {

    private Long id;
    private Long studentId;
    private String studentFullName;
    private Long examinationId;
    private Long subjectId;
    private String subjectName;
    private String examinationTypeName;
    private LocalDate examDate;
    private Integer examOrder;
    private BigDecimal score;

    public static GradeResponse from(Grade grade) {
        return GradeResponse.builder()
                .id(grade.getId())
                .studentId(grade.getStudent().getId())
                .studentFullName(grade.getStudent().getFirstName() + " " + grade.getStudent().getLastName())
                .examinationId(grade.getExamination().getId())
                .subjectId(grade.getExamination().getSubject().getId())
                .subjectName(grade.getExamination().getSubject().getName())
                .examinationTypeName(grade.getExamination().getExaminationType().getName())
                .examDate(grade.getExamination().getExamDate())
                .examOrder(grade.getExamination().getExamOrder())
                .score(grade.getScore())
                .build();
    }
}