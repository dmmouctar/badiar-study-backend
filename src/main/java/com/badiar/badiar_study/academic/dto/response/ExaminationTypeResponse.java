package com.badiar.badiar_study.academic.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExaminationTypeResponse {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
}