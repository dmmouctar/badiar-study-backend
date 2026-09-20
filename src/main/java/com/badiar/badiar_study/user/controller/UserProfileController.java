package com.badiar.badiar_study.user.controller;

import com.badiar.badiar_study.common.dto.ApiResponse;
import com.badiar.badiar_study.user.dto.ChangePasswordRequest;
import com.badiar.badiar_study.user.dto.UpdateProfileRequest;
import com.badiar.badiar_study.user.dto.UserAccountResponse;
import com.badiar.badiar_study.user.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Controller de gestion des profils utilisateurs.
 * Routes versionnées : /api/v1/profile/...
 *
 * PRINCIPE : ce controller ne contient AUCUNE logique métier.
 * Il délègue tout au UserProfileService.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    // ── GET /api/v1/profile/me ────────────────────────────────────
    // Récupérer le profil de l'utilisateur connecté
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'ETUDIANT')")
    public ResponseEntity<ApiResponse<UserAccountResponse>> getCurrentUserProfile(
            Authentication authentication) {

        String email = authentication.getName();
        UserAccountResponse profileResponse = userProfileService.getCurrentUserProfile(email);

        return ResponseEntity.ok(
                ApiResponse.success(profileResponse)
        );
    }

    // ── PUT /api/v1/profile/me ────────────────────────────────────
    // Mettre à jour le profil (prénom, nom)
    @PutMapping("/me")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'ETUDIANT')")
    public ResponseEntity<ApiResponse<UserAccountResponse>> updateProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateProfileRequest request) {

        String email = authentication.getName();
        UserAccountResponse updatedProfile = userProfileService.updateProfile(email, request);

        return ResponseEntity.ok(
                ApiResponse.success(updatedProfile)
        );
    }

    // ── PUT /api/v1/profile/change-password ───────────────────────
    // Changer le mot de passe
    @PutMapping("/change-password")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'ETUDIANT')")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            Authentication authentication,
            @Valid @RequestBody ChangePasswordRequest request) {

        String email = authentication.getName();
        userProfileService.changePassword(email, request);

        return ResponseEntity.ok(
                ApiResponse.success("Mot de passe changé avec succès")
        );
    }

    // ── POST /api/v1/profile/photo ────────────────────────────────
    // Upload la photo de profil de l'utilisateur connecté
    @PostMapping("/photo")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'ETUDIANT')")
    public ResponseEntity<ApiResponse<String>> uploadProfilePhoto(
            Authentication authentication,
            @RequestParam("file") MultipartFile file) throws IOException {

        String email = authentication.getName();
        String photoUrl = userProfileService.uploadProfilePhoto(email, file);

        return ResponseEntity.ok(
                ApiResponse.success(photoUrl)
        );
    }

    // ── POST /api/v1/profile/photo/user/{userId} ──────────────────
    // Upload la photo d'un utilisateur spécifique (admin seulement)
    @PostMapping("/photo/user/{userId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<ApiResponse<String>> uploadPhotoForUser(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file) throws IOException {

        String photoUrl = userProfileService.uploadPhotoForUser(userId, file);

        return ResponseEntity.ok(
                ApiResponse.success(photoUrl)
        );
    }

    // ── GET /api/v1/profile/photos/{filename} ─────────────────────
    // Servir les photos stockées (route PUBLIQUE — pas besoin de token)
    @GetMapping("/photos/{filename}")
    public ResponseEntity<Resource> servePhoto(
            @PathVariable String filename,
            @RequestParam(value = "uploadDir",
                    defaultValue = "uploads/photos") String uploadDir) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = MediaType.IMAGE_JPEG_VALUE;
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .body(resource);

        } catch (MalformedURLException malformedUrlException) {
            log.error("URL invalide pour la photo : {}", filename);
            return ResponseEntity.badRequest().build();
        } catch (IOException ioException) {
            log.error("Erreur lors de la lecture de la photo : {}", filename);
            return ResponseEntity.internalServerError().build();
        }
    }
}
