package com.badiar.badiar_study.academic.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Type d'examen : Contrôle Continu, Examen Final, Devoir Mensuel, Rattrapage.
 * Table séparée pour permettre l'ajout de nouveaux types sans modifier le schéma.
 */
@Entity
@Table(name = "examination_types")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExaminationType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom du type d'examen est obligatoire")
    @Size(max = 50, message = "Le nom ne doit pas dépasser 50 caractères")
    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ---- Relations ----------------------------------------------
    @OneToMany(mappedBy = "examinationType", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Examination> examinations = new ArrayList<>();
}
