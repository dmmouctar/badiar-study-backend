package com.badiar.badiar_study.academic.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor
public class ExaminationTypeRequest {

    @NotBlank(message = "Le nom du type d'examen est obligatoire")
    @Size(max = 50, message = "Le nom ne doit pas dépasser 50 caractères")
    private String name;
}