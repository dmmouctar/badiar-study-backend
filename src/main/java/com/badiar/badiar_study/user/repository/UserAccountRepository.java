package com.badiar.badiar_study.user.repository;

import com.badiar.badiar_study.user.entity.UserAccount;
import com.badiar.badiar_study.user.entity.UserAccount.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    // ── Recherches par email ──────────────────────────────────────

    @Transactional(readOnly = true)
    Optional<UserAccount> findByEmail(String email);


    // Vérifie si un email existe déjà en base (pour eviter les doublons à l'inscription)
    @Transactional(readOnly = true)
    boolean existsByEmail(String email);

    // Vérifie si un email existe pour un autre utilisateur (lors de la modification).
    @Transactional(readOnly = true)
    boolean existsByEmailAndIdNot(String email, Long id);

    // ── Recherches par rôle ───────────────────────────────────────


    // Liste tous les comptes d'un rôle donné.
    @Transactional(readOnly = true)
    List<UserAccount> findByRole(UserRole role);

    // Compte le nombre de SUPER_ADMIN existants.
    @Transactional(readOnly = true)
    long countByRole(UserRole role);

    // ── Recherches avec filtre ────────────────────────────────────

    // Recherche par nom ou prénom (insensible à la casse)
    @Transactional(readOnly = true)
    @Query("""
        SELECT u FROM UserAccount u
        WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(u.lastName)  LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(u.email)     LIKE LOWER(CONCAT('%', :keyword, '%'))
        ORDER BY u.lastName ASC
    """)
    List<UserAccount> searchByKeyword(@Param("keyword") String keyword);


    // Liste tous les comptes actifs.
    @Transactional(readOnly = true)
    List<UserAccount> findByIsActiveTrue();


    // Trouve un compte actif par email (pour l'authentification).
    @Transactional(readOnly = true)
    Optional<UserAccount> findByEmailAndIsActiveTrue(String email);
}
