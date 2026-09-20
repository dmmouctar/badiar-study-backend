package com.badiar.badiar_study.user.service;

import com.badiar.badiar_study.user.dto.ChangePasswordRequest;
import com.badiar.badiar_study.user.dto.UpdateProfileRequest;
import com.badiar.badiar_study.user.dto.UserAccountResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Interface du service de gestion des profils utilisateurs.
 *
 * Principe de découplage : le controller dépend de cette interface,
 * pas de l'implémentation concrète. Cela facilite les tests unitaires
 * et permet de changer l'implémentation sans toucher au controller.
 */
public interface UserProfileService {

    /**
     * Récupère le profil de l'utilisateur actuellement connecté.
     * @param email Email de l'utilisateur connecté (extrait du token JWT)
     * @return Les données du profil sans le mot de passe
     */
    UserAccountResponse getCurrentUserProfile(String email);

    /**
     * Met à jour le prénom et le nom de l'utilisateur connecté.
     * @param email   Email de l'utilisateur connecté
     * @param request Nouvelles données du profil (prénom, nom)
     * @return Le profil mis à jour
     */
    UserAccountResponse updateProfile(String email, UpdateProfileRequest request);

    /**
     * Change le mot de passe de l'utilisateur connecté.
     * Vérifie l'ancien mot de passe avant de changer.
     * @param email   Email de l'utilisateur connecté
     * @param request Ancien et nouveau mot de passe
     */
    void changePassword(String email, ChangePasswordRequest request);

    /**
     * Upload et sauvegarde la photo de profil de l'utilisateur.
     * @param email Email de l'utilisateur connecté
     * @param file  Fichier image (JPG, PNG, GIF, WEBP — max 5MB)
     * @return L'URL de la nouvelle photo
     */
    String uploadProfilePhoto(String email, MultipartFile file) throws IOException;

    /**
     * Upload la photo de profil d'un étudiant spécifique (admin seulement).
     * @param userId ID du compte utilisateur cible
     * @param file   Fichier image
     * @return L'URL de la nouvelle photo
     */
    String uploadPhotoForUser(Long userId, MultipartFile file) throws IOException;
}
