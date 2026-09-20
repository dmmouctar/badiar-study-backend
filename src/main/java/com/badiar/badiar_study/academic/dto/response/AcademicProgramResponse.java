package com.badiar.badiar_study.academic.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de réponse pour une filière académique.
 * Retourné par GET /api/v1/academic/programs
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AcademicProgramResponse {

    private Long          id;
    private String        name;
    private String        description;
    private Integer       gradingScale;
    private String        gradingScaleLabel; // "/ 10" ou "/ 20"
    private Boolean       isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
