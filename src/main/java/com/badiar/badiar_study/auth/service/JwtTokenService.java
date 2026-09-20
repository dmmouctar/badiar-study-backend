package com.badiar.badiar_study.auth.service;

import com.badiar.badiar_study.common.constant.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service de gestion des tokens JWT (JSON Web Token).
 *
 * Responsabilités :
 * - Générer un token à la connexion
 * - Valider un token reçu dans les requêtes
 * - Extraire les informations (email, expiration) d'un token
 */
@Slf4j
@Service
public class JwtTokenService {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private long jwtExpirationMs;

    // ── Générer un token JWT ──────────────────────────────────────

    /**
     * Génère un token JWT pour un utilisateur authentifié.
     * @param userDetails Détails de l'utilisateur Spring Security
     * @return Le token JWT sous forme de String
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();
        return buildToken(extraClaims, userDetails, jwtExpirationMs);
    }

    // Construit le token JWT avec les claims et la signature.
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expirationMs) {

        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername()) // email de l'utilisateur
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    // ── Valider un token ──────────────────────────────────────────

    /**
     * Vérifie que le token est valide pour l'utilisateur donné.
     * @return true si le token est valide et non expiré
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String emailFromToken = extractEmail(token);
            return emailFromToken.equals(userDetails.getUsername())
                    && !isTokenExpired(token);
        } catch (JwtException jwtException) {
            log.warn("Token JWT invalide : {}", jwtException.getMessage());
            return false;
        }
    }

    // Vérifie si le token est expiré.
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    // ── Extraire les informations du token ───────────────────────

    // Extrait l'email (subject) du token JWT.
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extrait la date d'expiration du token.
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extrait un claim spécifique du token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Décode et retourne tous les claims du token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Retourne la durée d'expiration configurée en millisecondes.
    public long getExpirationMs() {
        return jwtExpirationMs;
    }


     // Génère la clé de signature à partir du secret configuré.
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}