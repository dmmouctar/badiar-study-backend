package com.badiar.badiar_study.auth.dto;

import com.badiar.badiar_study.user.entity.UserAccount.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DTO retourné après une connexion réussie.
 * token JWT et les informations de base de l'utilisateur.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationResponse {

    private String   token;
    private String   tokenType;
    private Long     expiresIn;   // Durée de validité en millisecondes
    private Long     userId;
    private String   firstName;
    private String   lastName;
    private String   fullName;
    private String   email;
    private UserRole role;
    private String   photoUrl;
}
