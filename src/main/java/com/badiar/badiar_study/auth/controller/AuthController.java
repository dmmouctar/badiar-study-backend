package com.badiar.badiar_study.auth.controller;

import com.badiar.badiar_study.auth.dto.AuthenticationResponse;
import com.badiar.badiar_study.auth.dto.CreateAdminRequest;
import com.badiar.badiar_study.auth.dto.LoginRequest;
import com.badiar.badiar_study.auth.service.AuthenticationService;
import com.badiar.badiar_study.common.dto.ApiResponse;
import com.badiar.badiar_study.user.dto.UserAccountResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller d'authentification.
 *
 * Routes PUBLIQUES (pas de token requis) :
 * - POST /api/v1/auth/login
 *
 * Routes PROTÉGÉES :
 * - POST /api/v1/auth/admin/create (SUPER_ADMIN ou ADMIN seulement)
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    // ---------- POST /api/v1/auth/login (Route publique) ----------------------------
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(
            @Valid @RequestBody LoginRequest loginRequest) {

        AuthenticationResponse authResponse = authenticationService.login(loginRequest);

        return ResponseEntity.ok(
                ApiResponse.success(authResponse)
        );
    }

    // ---- POST /api/v1/auth/admin/create(SUPER_ADMIN et ADMIN) -----------------------------
    @PostMapping("/admin/create")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<ApiResponse<UserAccountResponse>> createAdminAccount(
            @Valid @RequestBody CreateAdminRequest request) {

        UserAccountResponse createdAdmin = authenticationService.createAdminAccount(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(createdAdmin));
    }
}























