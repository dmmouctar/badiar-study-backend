package com.badiar.badiar_study.academic.entity;

import com.badiar.badiar_study.common.constant.ValidationConstants;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Table de liaison entre Filière + Année Académique + Niveau.
 * Représente par exemple :
 * - "Génie Informatique, 2024-2025, Licence 4 (niveau 4)"
 * - "Sciences Expérimentales, 2018-2019, 12ème année (niveau 12)"
 *
 * C'est à travers cette entité qu'on accède aux semestres
 * et donc aux matières d'un étudiant.
 */
@Entity
@Table(
        name = "program_year_levels",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_program_year_level",
                        columnNames = {"academic_program_id", "academic_year_id", "level_number"}
                )
        }
)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgramYearLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La filière est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_program_id", nullable = false)
    private AcademicProgram academicProgram;

    @NotNull(message = "L'année académique est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_year_id", nullable = false)
    private AcademicYear academicYear;

    /**
     * Numéro de niveau dans le cycle.
     * Université : 1=L1, 2=L2, 3=L3, 4=L4
     * Lycée      : 10=10ème, 11=11ème, 12=12ème
     */
    @NotNull(message = "Le numéro de niveau est obligatoire")
    @Min(value = ValidationConstants.LEVEL_MIN,
            message = "Le niveau minimum est " + ValidationConstants.LEVEL_MIN)
    @Max(value = ValidationConstants.LEVEL_MAX,
            message = "Le niveau maximum est " + ValidationConstants.LEVEL_MAX)
    @Column(name = "level_number", nullable = false)
    private Integer levelNumber;

    /**
     * Libellé lisible du niveau.
     * Exemple : "Licence 1", "Licence 4", "12ème année"
     */
    @NotBlank(message = "Le libellé du niveau est obligatoire")
    @Size(max = 50)
    @Column(name = "level_label", nullable = false, length = 50)
    private String levelLabel;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ── Relations ────────────────────────────────────────────────
    @OneToMany(mappedBy = "programYearLevel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<AcademicSemester> semesters = new ArrayList<>();
}
