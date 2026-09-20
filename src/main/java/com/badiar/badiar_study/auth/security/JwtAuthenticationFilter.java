package com.badiar.badiar_study.auth.security;

import com.badiar.badiar_study.auth.service.JwtTokenService;
import com.badiar.badiar_study.common.constant.SecurityConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtre JWT — intercepte CHAQUE requête HTTP pour vérifier le token.
 *
 * Fonctionnement :
 * 1. Lit le header "Authorization: Bearer <token>"
 * 2. Extrait et valide le token JWT
 * 3. Si valide → authentifie l'utilisateur dans le SecurityContext
 * 4. Si invalide ou absent → laisse passer (Spring Security refusera après)
 *
 * Ce filtre s'exécute UNE SEULE fois par requête (OncePerRequestFilter).
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenService         jwtTokenService;
    private final UserDetailsServiceImpl  userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest  request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain         filterChain)
            throws ServletException, IOException {

        // 1. Lire le header Authorization
        final String authorizationHeader = request.getHeader(
                SecurityConstants.JWT_HEADER_NAME
        );

        // 2. Si le header est absent ou ne commence pas par "Bearer ", on passe
        if (authorizationHeader == null
                || !authorizationHeader.startsWith(SecurityConstants.JWT_TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extraire le token (supprimer le préfixe "Bearer ")
        final String jwtToken = authorizationHeader.substring(
                SecurityConstants.JWT_TOKEN_PREFIX.length()
        );

        // 4. Extraire l'email depuis le token
        final String userEmail;
        try {
            userEmail = jwtTokenService.extractEmail(jwtToken);
        } catch (Exception tokenParsingException) {
            // Token malformé — on laisse passer sans authentification
            log.warn("Token JWT malformé : {}", tokenParsingException.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        // 5. Si l'email est valide et l'utilisateur pas encore authentifié
        if (userEmail != null
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            // 6. Valider le token
            if (jwtTokenService.isTokenValid(jwtToken, userDetails)) {

                // 7. Créer l'objet d'authentification et l'injecter dans le contexte
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext()
                        .setAuthentication(authenticationToken);

                log.debug("Utilisateur authentifié via JWT : {}", userEmail);
            }
        }

        // 8. Continuer la chaîne de filtres
        filterChain.doFilter(request, response);
    }
}
