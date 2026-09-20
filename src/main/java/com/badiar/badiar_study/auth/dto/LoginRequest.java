package com.badiar.badiar_study.auth.dto;

import com.badiar.badiar_study.common.constant.ValidationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO pour la requête de connexion.
 * Utilisé par POST /api/v1/auth/login
 */
@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = ValidationConstants.EMAIL_NOT_BLANK_MESSAGE)
    @Email(message = ValidationConstants.EMAIL_INVALID_MESSAGE)
    @Size(max = ValidationConstants.EMAIL_MAX_LENGTH)
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(
            min = ValidationConstants.PASSWORD_MIN_LENGTH,
            max = ValidationConstants.PASSWORD_MAX_LENGTH,
            message = "Le mot de passe doit contenir entre "
                    + ValidationConstants.PASSWORD_MIN_LENGTH + " et "
                    + ValidationConstants.PASSWORD_MAX_LENGTH + " caractères"
    )
    private String password;
}
