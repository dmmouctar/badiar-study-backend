package com.badiar.badiar_study.auth.service;

import com.badiar.badiar_study.auth.dto.AuthenticationResponse;
import com.badiar.badiar_study.auth.dto.CreateAdminRequest;
import com.badiar.badiar_study.auth.dto.LoginRequest;
import com.badiar.badiar_study.user.dto.UserAccountResponse;

/**
 * Interface du service d'authentification.
 * Gère la connexion et la création de comptes administrateurs.
 */
public interface AuthenticationService {

    /**
     * Authentifie un utilisateur avec son email et mot de passe.
     * Retourne un token JWT si les credentials sont corrects.
     * @param loginRequest Email et mot de passe
     * @return Token JWT + informations de l'utilisateur
     */
    AuthenticationResponse login(LoginRequest loginRequest);

    /**
     * Crée un nouveau compte ADMIN.
     * Accessible uniquement aux SUPER_ADMIN et ADMIN.
     * @param request Données du nouvel admin
     * @return Les informations du compte créé
     */
    UserAccountResponse createAdminAccount(CreateAdminRequest request);
}
