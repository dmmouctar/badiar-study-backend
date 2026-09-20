package com.badiar.badiar_study.academic.entity;

import com.badiar.badiar_study.common.constant.AcademicConstants;
import com.badiar.badiar_study.common.constant.ValidationConstants;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant une matière académique.
 *
 * DOUBLE LIEN :
 * - semester_id    : semestre/module/trimestre précis auquel elle appartient
 * - academic_program_id : filière directement (pour retrouver toutes les
 *   matières d'une filière sans passer par les semestres)
 *
 * Le coefficient est utilisé pour le calcul de la moyenne pondérée.
 * Exemple : Mathématiques coeff 3, Anglais coeff 1
 */
@Entity
@Table(name = "subjects")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Lien direct vers la filière (premier lien).
     * Permet de lister toutes les matières d'une filière directement.
     */
    @NotNull(message = "La filière est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_program_id", nullable = false)
    private AcademicProgram academicProgram;

    /**
     * Lien vers le semestre/module/trimestre précis (second lien).
     * Permet de savoir dans quel semestre cette matière est enseignée.
     */
    @NotNull(message = "Le semestre est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester_id", nullable = false)
    private AcademicSemester semester;

    @NotBlank(message = "Le nom de la matière est obligatoire")
    @Size(
            min = ValidationConstants.ACADEMIC_NAME_MIN_LENGTH,
            max = ValidationConstants.ACADEMIC_NAME_MAX_LENGTH,
            message = "Le nom doit contenir entre "
                    + ValidationConstants.ACADEMIC_NAME_MIN_LENGTH + " et "
                    + ValidationConstants.ACADEMIC_NAME_MAX_LENGTH + " caractères"
    )
    @Column(name = "name", nullable = false, length = 150)
    private String name;

    /**
     * Coefficient de la matière pour le calcul de la moyenne pondérée.
     * Formule : Σ(moyenne_matière × coefficient) / Σ(coefficients)
     */
    @NotNull(message = "Le coefficient est obligatoire")
    @DecimalMin(
            value = "0.5",
            message = "Le coefficient minimum est " + AcademicConstants.COEFFICIENT_MIN
    )
    @DecimalMax(
            value = "10.0",
            message = "Le coefficient maximum est " + AcademicConstants.COEFFICIENT_MAX
    )
    @Column(name = "coefficient", nullable = false, precision = 4, scale = 2)
    private BigDecimal coefficient;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ------- Relations ---------------------------------------------
    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Examination> examinations = new ArrayList<>();
}
