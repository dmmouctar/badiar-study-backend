package com.badiar.badiar_study.grading.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ValidateReportCardRequest {

    @Size(max = 1000, message = "Le commentaire ne peut pas dépasser 1000 caractères.")
    private String notes;
}