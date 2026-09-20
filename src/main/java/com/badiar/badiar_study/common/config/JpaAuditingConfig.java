package com.badiar.badiar_study.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Active le JPA Auditing pour remplir automatiquement
 * les champs @CreatedDate et @LastModifiedDate dans toutes les entités.
 *
 * Sans cette configuration, ces champs resteraient null.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
    // Aucun bean nécessaire — @EnableJpaAuditing suffit
}
