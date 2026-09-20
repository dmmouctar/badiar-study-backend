package com.badiar.badiar_study.auth.dto;

import com.badiar.badiar_study.common.constant.ValidationConstants;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO pour créer un nouveau compte ADMIN.
 * Utilisé par POST /api/v1/auth/admin/create
 * Accessible uniquement aux SUPER_ADMIN et ADMIN.
 */
@Getter
@Setter
@NoArgsConstructor
public class CreateAdminRequest {

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(
            min = ValidationConstants.NAME_MIN_LENGTH,
            max = ValidationConstants.NAME_MAX_LENGTH,
            message = "Le prénom doit contenir entre "
                    + ValidationConstants.NAME_MIN_LENGTH + " et "
                    + ValidationConstants.NAME_MAX_LENGTH + " caractères"
    )
    @Pattern(
            regexp = ValidationConstants.NAME_PATTERN,
            message = ValidationConstants.NAME_PATTERN_MESSAGE
    )
    private String firstName;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(
            min = ValidationConstants.NAME_MIN_LENGTH,
            max = ValidationConstants.NAME_MAX_LENGTH,
            message = "Le nom doit contenir entre "
                    + ValidationConstants.NAME_MIN_LENGTH + " et "
                    + ValidationConstants.NAME_MAX_LENGTH + " caractères"
    )
    @Pattern(
            regexp = ValidationConstants.NAME_PATTERN,
            message = ValidationConstants.NAME_PATTERN_MESSAGE
    )
    private String lastName;

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
    @Pattern(
            regexp = ValidationConstants.PASSWORD_PATTERN,
            message = ValidationConstants.PASSWORD_PATTERN_MESSAGE
    )
    private String password;
}
