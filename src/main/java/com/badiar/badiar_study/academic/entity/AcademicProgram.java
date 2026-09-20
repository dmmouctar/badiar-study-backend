package com.badiar.badiar_study.academic.entity;

import com.badiar.badiar_study.common.constant.AcademicConstants;
import com.badiar.badiar_study.common.constant.ValidationConstants;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant une filière académique (ex: Génie Informatique).
 * Couvre tout le cycle (L1→L4 ou 10ème→12ème).
 * Porte le barème : 10 (université) ou 20 (lycée/collège).
 */
@Entity
@Table(
        name = "academic_programs",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_program_name", columnNames = "name")
        }
)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom de la filière est obligatoire")
    @Size(
            min = ValidationConstants.ACADEMIC_NAME_MIN_LENGTH,
            max = ValidationConstants.ACADEMIC_NAME_MAX_LENGTH,
            message = "Le nom doit contenir entre "
                    + ValidationConstants.ACADEMIC_NAME_MIN_LENGTH + " et "
                    + ValidationConstants.ACADEMIC_NAME_MAX_LENGTH + " caractères"
    )
    @Column(name = "name", nullable = false, unique = true, length = 150)
    private String name;

    @Size(max = ValidationConstants.DESCRIPTION_MAX_LENGTH)
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * Barème de notation : 10 (université) ou 20 (lycée/collège).
     * Validé par une contrainte CHECK dans la base de données.
     */
    @NotNull(message = "Le barème est obligatoire")
    @Min(value = 10, message = "Le barème minimum est 10")
    @Max(value = 20, message = "Le barème maximum est 20")
    @Column(name = "grading_scale", nullable = false)
    private Integer gradingScale;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ---- Relations -----------------------------------------------
    @OneToMany(mappedBy = "academicProgram", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ProgramYearLevel> programYearLevels = new ArrayList<>();

    // ---- Méthodes utilitaires ------------------------------------


    // Vérifie si cette filière utilise le barème universitaire (/10)
    public boolean isUniversityScale() {
        return AcademicConstants.GRADING_SCALE_UNIVERSITY == this.gradingScale;
    }


    // Vérifie si cette filière utilise le barème secondaire (/20)
    public boolean isSecondaryScale() {
        return AcademicConstants.GRADING_SCALE_SECONDARY == this.gradingScale;
    }
}
