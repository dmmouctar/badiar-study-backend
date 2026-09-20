package com.badiar.badiar_study.common.constant;


public final class SecurityConstants {

    // ── Empêcher l'instanciation de cette classe utilitaire ──────
    private SecurityConstants() {
        throw new UnsupportedOperationException(
                "SecurityConstants est une classe utilitaire et ne peut pas être instanciée"
        );
    }

    // ── JWT ──────────────────────────────────────────────────────
    public static final String JWT_TOKEN_PREFIX = "Bearer ";
    public static final String JWT_HEADER_NAME = "Authorization";
    public static final long   JWT_DEFAULT_EXPIRATION_MS = 86_400_000L; // 24 H

    // ── Préfixe des rôles Spring Security ───────────────────────
    public static final String ROLE_PREFIX = "ROLE_";

    // ── Routes publiques (pas besoin de token) ───────────────────
    public static final String AUTH_BASE_URL  = "/api/v1/auth/**";
    public static final String PHOTOS_PUBLIC_URL = "/api/v1/profile/photos/**";

    // ── Upload photos ────────────────────────────────────────────
    public static final long   MAX_PHOTO_SIZE_BYTES = 5 * 1024 * 1024L; // 5 MB
    public static final String ALLOWED_PHOTO_TYPES = "image/";
}
