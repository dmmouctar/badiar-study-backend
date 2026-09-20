package com.badiar.badiar_study.grading.dto.request;

import com.badiar.badiar_study.common.constant.ValidationConstants;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreateGradeRequest {

    @NotNull(message = "L'ID de l'étudiant est obligatoire.")
    private Long studentId;

    @NotNull(message = "L'ID de l'examen est obligatoire.")
    private Long examinationId;

    @NotNull(message = "La note est obligatoire.")
    @DecimalMin(value = "0.0", message = "La note doit être ≥ " + ValidationConstants.SCORE_MIN)
    private BigDecimal score;
}