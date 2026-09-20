package com.badiar.badiar_study.academic.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant un semestre, module ou trimestre.
 *
 * Université : "Module 1", "Module 2" (2 par année)
 * Lycée      : "Trimestre 1", "Trimestre 2", "Trimestre 3" (3 par année)
 * Collège    : idem lycée
 *
 * C'est à travers le semestre qu'on accède aux matières
 * d'un niveau donné dans une filière donnée.
 */
@Entity
@Table(
        name = "academic_semesters",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_semester_order",
                        columnNames = {"program_year_level_id", "display_order"}
                )
        }
)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicSemester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Le niveau de programme est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_year_level_id", nullable = false)
    private ProgramYearLevel programYearLevel;

    /**
     * Nom du semestre.
     * Exemples : "Module 1", "Module 2", "Trimestre 1", "Trimestre 3"
     */
    @NotBlank(message = "Le nom du semestre est obligatoire")
    @Size(max = 50, message = "Le nom ne doit pas dépasser 50 caractères")
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    /**
     * Ordre d'affichage : 1, 2, 3...
     * Permet de trier les semestres dans l'ordre chronologique.
     */
    @NotNull(message = "L'ordre d'affichage est obligatoire")
    @Min(value = 1, message = "L'ordre minimum est 1")
    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ── Relations ────────────────────────────────────────────────
    @OneToMany(mappedBy = "semester", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Subject> subjects = new ArrayList<>();
}
