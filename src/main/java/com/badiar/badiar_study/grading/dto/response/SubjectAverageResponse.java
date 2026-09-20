package com.badiar.badiar_study.grading.dto.response;

import com.badiar.badiar_study.grading.entity.SubjectAverage;
import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
@Builder
public class SubjectAverageResponse {

    private Long id;
    private Long subjectId;
    private String subjectName;
    private BigDecimal coefficient;
    private BigDecimal averageScore;
    private BigDecimal weightedScore;

    public static SubjectAverageResponse from(SubjectAverage sa) {
        return SubjectAverageResponse.builder()
                .id(sa.getId())
                .subjectId(sa.getSubject().getId())
                .subjectName(sa.getSubject().getName())
                .coefficient(sa.getSubject().getCoefficient())
                .averageScore(sa.getAverageScore())
                .weightedScore(sa.getWeightedScore())
                .build();
    }
}