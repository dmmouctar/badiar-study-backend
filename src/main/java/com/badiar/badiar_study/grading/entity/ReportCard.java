package com.badiar.badiar_study.grading.entity;

import com.badiar.badiar_study.academic.entity.AcademicSemester;
import com.badiar.badiar_study.academic.entity.AcademicYear;
import com.badiar.badiar_study.enrollment.entity.Student;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "report_cards",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_report_semestre",
                columnNames = {"student_id", "semester_id"}
        )
)
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReportCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_card_type", nullable = false, length = 10)
    private ReportCardType reportCardType;

    /** NULL pour les bulletins annuels */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester_id")
    private AcademicSemester semester;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "academic_year_id", nullable = false)
    private AcademicYear academicYear;

    @Column(name = "overall_average", precision = 5, scale = 2)
    private BigDecimal overallAverage;

    @Column(name = "is_validated", nullable = false)
    @Builder.Default
    private boolean validated = false;

    @Column(name = "validated_at")
    private LocalDateTime validatedAt;

    @OneToMany(mappedBy = "reportCard", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SubjectAverage> subjectAverages = new ArrayList<>();

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public enum ReportCardType {
        SEMESTRE, ANNUEL
    }
}