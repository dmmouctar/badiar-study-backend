package com.badiar.badiar_study.auth.service;

import com.badiar.badiar_study.auth.dto.AuthenticationResponse;
import com.badiar.badiar_study.auth.dto.CreateAdminRequest;
import com.badiar.badiar_study.auth.dto.LoginRequest;
import com.badiar.badiar_study.common.constant.SecurityConstants;
import com.badiar.badiar_study.common.exception.BadRequestException;
import com.badiar.badiar_study.user.dto.UserAccountResponse;
import com.badiar.badiar_study.user.entity.UserAccount;
import com.badiar.badiar_study.user.entity.UserAccount.UserRole;
import com.badiar.badiar_study.user.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implémentation du service d'authentification.
 * Gère la connexion JWT et la création de comptes admins.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder       passwordEncoder;
    private final JwtTokenService       jwtTokenService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService    userDetailsService;

    // ── Connexion ─────────────────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public AuthenticationResponse login(LoginRequest loginRequest) {

        // Spring Security vérifie email + mot de passe
        // Lance BadCredentialsException si incorrects
        // (interceptée par GlobalExceptionHandler → 401)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        // Charger l'utilisateur pour générer le token
        UserDetails userDetails = userDetailsService
                .loadUserByUsername(loginRequest.getEmail());

        // Charger les infos complètes depuis la base
        UserAccount userAccount = userAccountRepository
                .findByEmailAndIsActiveTrue(loginRequest.getEmail())
                .orElseThrow(() -> new BadRequestException("Compte introuvable ou inactif"));

        // Générer le token JWT
        String jwtToken = jwtTokenService.generateToken(userDetails);

        log.info("Connexion réussie pour : {}", loginRequest.getEmail());

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .tokenType(SecurityConstants.JWT_TOKEN_PREFIX.trim())
                .expiresIn(jwtTokenService.getExpirationMs())
                .userId(userAccount.getId())
                .firstName(userAccount.getFirstName())
                .lastName(userAccount.getLastName())
                .fullName(userAccount.getFullName())
                .email(userAccount.getEmail())
                .role(userAccount.getRole())
                .photoUrl(userAccount.getPhotoUrl())
                .build();
    }

    // ── Créer un compte Admin ─────────────────────────────────────
    @Override
    @Transactional
    public UserAccountResponse createAdminAccount(CreateAdminRequest request) {

        // Vérifier que l'email n'existe pas déjà
        if (userAccountRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException(
                    "Un compte avec l'email '" + request.getEmail() + "' existe déjà"
            );
        }

        // Créer le nouveau compte ADMIN
        UserAccount newAdminAccount = UserAccount.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.ADMIN)
                .isActive(true)
                .build();

        UserAccount savedAdminAccount = userAccountRepository.save(newAdminAccount);

        // Ne jamais logger le mot de passe
        log.info("Nouveau compte ADMIN créé : {}", request.getEmail());

        return UserAccountResponse.builder()
                .id(savedAdminAccount.getId())
                .firstName(savedAdminAccount.getFirstName())
                .lastName(savedAdminAccount.getLastName())
                .fullName(savedAdminAccount.getFullName())
                .email(savedAdminAccount.getEmail())
                .role(savedAdminAccount.getRole())
                .isActive(savedAdminAccount.getIsActive())
                .createdAt(savedAdminAccount.getCreatedAt())
                .build();
    }
}
