package com.badiar.badiar_study.academic.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entité représentant un examen pour une matière donnée.
 *
 * Université : 3 examens par matière par module (note1, note2, note3)
 * Lycée      : 1 examen par mois par matière dans le trimestre
 *              (type = "Devoir Mensuel")
 *
 * Le champ examOrder permet d'identifier la note 1, 2 ou 3
 * dans le cas de l'université.
 */
@Entity
@Table(name = "examinations")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Examination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La matière est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @NotNull(message = "Le type d'examen est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "examination_type_id", nullable = false)
    private ExaminationType examinationType;

    @Column(name = "exam_date")
    private LocalDate examDate;

    /**
     * Numéro d'ordre de la note : 1, 2 ou 3.
     * Université : note1=1, note2=2, note3=3
     * Lycée      : mois1=1, mois2=2, mois3=3 (dans le trimestre)
     */
    @NotNull(message = "L'ordre de l'examen est obligatoire")
    @Min(value = 1, message = "L'ordre minimum est 1")
    @Max(value = 3, message = "L'ordre maximum est 3")
    @Column(name = "exam_order", nullable = false)
    private Integer examOrder;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
