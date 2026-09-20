package com.badiar.badiar_study.auth.security;

import com.badiar.badiar_study.user.entity.UserAccount;
import com.badiar.badiar_study.user.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implémentation de UserDetailsService pour Spring Security.
 * Charge les détails d'un utilisateur depuis la base de données
 * à partir de son email (utilisé comme "username").
 *
 * Spring Security appelle cette classe automatiquement lors de
 * l'authentification pour vérifier les credentials.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;


    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        UserAccount userAccount = userAccountRepository
                .findByEmailAndIsActiveTrue(email)
                .orElseThrow(() -> {
                    log.warn("Tentative de connexion avec un email inconnu ou inactif");
                    return new UsernameNotFoundException(
                            "Identifiants incorrects"
                    );
                });

        // Construction du rôle avec le préfixe ROLE_ requis par Spring Security
        String roleWithPrefix = "ROLE_" + userAccount.getRole().name();

        return new User(
                userAccount.getEmail(),
                userAccount.getPassword(),
                List.of(new SimpleGrantedAuthority(roleWithPrefix))
        );
    }
}
