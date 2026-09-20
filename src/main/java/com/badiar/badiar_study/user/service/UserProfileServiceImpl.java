package com.badiar.badiar_study.user.service;

import com.badiar.badiar_study.common.constant.SecurityConstants;
import com.badiar.badiar_study.common.exception.BadRequestException;
import com.badiar.badiar_study.common.exception.ResourceNotFoundException;
import com.badiar.badiar_study.user.dto.ChangePasswordRequest;
import com.badiar.badiar_study.user.dto.UpdateProfileRequest;
import com.badiar.badiar_study.user.dto.UserAccountResponse;
import com.badiar.badiar_study.user.entity.UserAccount;
import com.badiar.badiar_study.user.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

/**
 * Implémentation concrète de UserProfileService.
 * Contient toute la logique métier pour la gestion des profils.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder       passwordEncoder;

    @Value("${app.upload.dir}")
    private String uploadDirectory;

    // ── Récupérer le profil connecté ─────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public UserAccountResponse getCurrentUserProfile(String email) {
        UserAccount userAccount = findActiveUserByEmail(email);
        return mapToResponse(userAccount);
    }

    // ── Mettre à jour le profil ───────────────────────────────────
    @Override
    @Transactional
    public UserAccountResponse updateProfile(String email, UpdateProfileRequest request) {
        UserAccount userAccount = findActiveUserByEmail(email);

        userAccount.setFirstName(request.getFirstName());
        userAccount.setLastName(request.getLastName());

        UserAccount savedAccount = userAccountRepository.save(userAccount);
        log.info("Profil mis à jour pour l'utilisateur : {}", email);

        return mapToResponse(savedAccount);
    }

    // ── Changer le mot de passe ───────────────────────────────────
    @Override
    @Transactional
    public void changePassword(String email, ChangePasswordRequest request) {
        UserAccount userAccount = findActiveUserByEmail(email);

        // Vérifier que l'ancien mot de passe est correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), userAccount.getPassword())) {
            throw new BadRequestException("L'ancien mot de passe est incorrect");
        }

        // Vérifier que les deux nouveaux mots de passe correspondent
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException(
                    "Le nouveau mot de passe et sa confirmation ne correspondent pas"
            );
        }

        // Vérifier que le nouveau mot de passe est différent de l'ancien
        if (passwordEncoder.matches(request.getNewPassword(), userAccount.getPassword())) {
            throw new BadRequestException(
                    "Le nouveau mot de passe doit être différent de l'ancien"
            );
        }

        // Hasher et sauvegarder le nouveau mot de passe
        userAccount.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userAccountRepository.save(userAccount);

        // IMPORTANT : ne jamais logger le mot de passe même hashé
        log.info("Mot de passe changé avec succès pour l'utilisateur : {}", email);
    }

    // ── Upload photo de profil (utilisateur connecté) ─────────────
    @Override
    @Transactional
    public String uploadProfilePhoto(String email, MultipartFile file) throws IOException {
        UserAccount userAccount = findActiveUserByEmail(email);
        String photoUrl = savePhotoFile(file, userAccount);
        userAccount.setPhotoUrl(photoUrl);
        userAccountRepository.save(userAccount);
        log.info("Photo de profil mise à jour pour l'utilisateur : {}", email);
        return photoUrl;
    }

    // ── Upload photo pour un utilisateur spécifique (admin) ───────
    @Override
    @Transactional
    public String uploadPhotoForUser(Long userId, MultipartFile file) throws IOException {
        UserAccount userAccount = userAccountRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", userId));

        String photoUrl = savePhotoFile(file, userAccount);
        userAccount.setPhotoUrl(photoUrl);
        userAccountRepository.save(userAccount);
        log.info("Photo mise à jour pour l'utilisateur ID : {}", userId);
        return photoUrl;
    }

    // ── Méthodes privées ─────────────────────────────────────────

    /**
     * Trouve un utilisateur actif par email.
     * Centralise la gestion de l'erreur "utilisateur introuvable".
     */
    private UserAccount findActiveUserByEmail(String email) {
        return userAccountRepository.findByEmailAndIsActiveTrue(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Utilisateur", "email", email
                ));
    }

    /**
     * Sauvegarde physiquement le fichier photo sur le serveur.
     * Supprime l'ancienne photo si elle existe.
     * @return L'URL relative de la nouvelle photo
     */
    private String savePhotoFile(MultipartFile file, UserAccount userAccount) throws IOException {
        // Validation du type de fichier
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith(SecurityConstants.ALLOWED_PHOTO_TYPES)) {
            throw new BadRequestException("Seules les images sont acceptées (JPG, PNG, GIF, WEBP)");
        }

        // Validation de la taille
        if (file.getSize() > SecurityConstants.MAX_PHOTO_SIZE_BYTES) {
            throw new BadRequestException("La photo ne doit pas dépasser 5MB");
        }

        // Créer le dossier si inexistant
        Path uploadPath = Paths.get(uploadDirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Générer un nom de fichier unique pour éviter les conflits
        String fileExtension = getFileExtension(file.getOriginalFilename());
        String uniqueFileName = UUID.randomUUID().toString() + "." + fileExtension;
        Path filePath = uploadPath.resolve(uniqueFileName);

        // Supprimer l'ancienne photo si elle existe
        deleteOldPhoto(userAccount.getPhotoUrl(), uploadPath);

        // Sauvegarder le nouveau fichier
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "/api/v1/profile/photos/" + uniqueFileName;
    }

    /**
     * Supprime l'ancienne photo du serveur si elle existe.
     */
    private void deleteOldPhoto(String existingPhotoUrl, Path uploadPath) {
        if (existingPhotoUrl == null || existingPhotoUrl.isBlank()) return;
        try {
            String oldFileName = existingPhotoUrl.replace("/api/v1/profile/photos/", "");
            Files.deleteIfExists(uploadPath.resolve(oldFileName));
        } catch (IOException ioException) {
            // On logue juste un warning — ce n'est pas bloquant
            log.warn("Impossible de supprimer l'ancienne photo : {}", existingPhotoUrl);
        }
    }

    /**
     * Extrait l'extension d'un nom de fichier.
     */
    private String getFileExtension(String originalFilename) {
        if (originalFilename == null || !originalFilename.contains(".")) {
            return "jpg";
        }
        return originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * Convertit une entité UserAccount en DTO de réponse.
     *JAMAIS le mot de passe dans la réponse.
     */
    private UserAccountResponse mapToResponse(UserAccount userAccount) {
        return UserAccountResponse.builder()
                .id(userAccount.getId())
                .firstName(userAccount.getFirstName())
                .lastName(userAccount.getLastName())
                .fullName(userAccount.getFullName())
                .email(userAccount.getEmail())
                .role(userAccount.getRole())
                .photoUrl(userAccount.getPhotoUrl())
                .isActive(userAccount.getIsActive())
                .createdAt(userAccount.getCreatedAt())
                .updatedAt(userAccount.getUpdatedAt())
                .build();
    }
}
