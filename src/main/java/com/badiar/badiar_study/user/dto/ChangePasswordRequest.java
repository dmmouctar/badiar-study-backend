package com.badiar.badiar_study.user.dto;

import com.badiar.badiar_study.common.constant.ValidationConstants;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class ChangePasswordRequest {

    @NotBlank(message = "L'ancien mot de passe est obligatoire")
    private String currentPassword;

    @NotBlank(message = "Le nouveau mot de passe est obligatoire")
    @Size(
            min = ValidationConstants.PASSWORD_MIN_LENGTH,
            max = ValidationConstants.PASSWORD_MAX_LENGTH,
            message = "Le mot de passe doit contenir entre "
                    + ValidationConstants.PASSWORD_MIN_LENGTH + " et "
                    + ValidationConstants.PASSWORD_MAX_LENGTH + " caractères"
    )
    @Pattern(
            regexp = ValidationConstants.PASSWORD_PATTERN,
            message = ValidationConstants.PASSWORD_PATTERN_MESSAGE
    )
    private String newPassword;

    @NotBlank(message = "La confirmation du mot de passe est obligatoire")
    private String confirmPassword;
}
