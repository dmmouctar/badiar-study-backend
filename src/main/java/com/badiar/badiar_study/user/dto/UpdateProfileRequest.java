package com.badiar.badiar_study.user.dto;

import com.badiar.badiar_study.common.constant.ValidationConstants;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class UpdateProfileRequest {

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
}
