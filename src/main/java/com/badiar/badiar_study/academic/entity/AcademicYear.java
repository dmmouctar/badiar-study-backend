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
 * Année académique GLOBALE (ex: "2024-2025").
 * Partagée par TOUTES les filières — indépendante de la filière.
 * Plusieurs étudiants de filières différentes peuvent partager
 * la même année académique.
 */
@Entity
@Table(
        name = "academic_years",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_academic_year_name", columnNames = "name")
        }
)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicYear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom de l'année académique est obligatoire")
    @Size(max = 20, message = "Le nom ne doit pas dépasser 20 caractères")
    @Pattern(
            regexp = "^\\d{4}-\\d{4}$",
            message = "Le format doit être AAAA-AAAA (ex: 2024-2025)"
    )
    @Column(name = "name", nullable = false, unique = true, length = 20)
    private String name;

    @NotNull(message = "La date de début est obligatoire")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull(message = "La date de fin est obligatoire")
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

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
    @OneToMany(mappedBy = "academicYear", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ProgramYearLevel> programYearLevels = new ArrayList<>();
}
