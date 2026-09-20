package com.badiar.badiar_study.academic.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenderResponse {
    private Long id;
    private String name;
}