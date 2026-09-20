package com.badiar.badiar_study.grading.dto.response;

import com.badiar.badiar_study.common.constant.AcademicConstants;
import com.badiar.badiar_study.grading.entity.ReportCard;
import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ReportCardResponse {

    private Long id;
    private Long studentId;
    private String studentFullName;
    private String registrationNumber;
    private String reportCardType;
    private Long semesterId;
    private String semesterName;
    private Long academicYearId;
    private String academicYearName;
    private BigDecimal overallAverage;
    private boolean validated;
    private LocalDateTime validatedAt;
    private List<SubjectAverageResponse> subjectAverages;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String mention;

    /** Mapping complet (avec subjectAverages) — appeler depuis findByIdWithDetails. */
    public static ReportCardResponse from(ReportCard rc) {
        return ReportCardResponse.builder()
                .id(rc.getId())
                .studentId(rc.getStudent().getId())
                .studentFullName(rc.getStudent().getFirstName() + " " + rc.getStudent().getLastName())
                .registrationNumber(rc.getStudent().getRegistrationNumber())
                .reportCardType(rc.getReportCardType().name())
                .semesterId(rc.getSemester() != null ? rc.getSemester().getId() : null)
                .semesterName(rc.getSemester() != null ? rc.getSemester().getName() : null)
                .academicYearId(rc.getAcademicYear().getId())
                .academicYearName(rc.getAcademicYear().getName())
                .overallAverage(rc.getOverallAverage())
                .validated(rc.isValidated())
                .validatedAt(rc.getValidatedAt())
                .subjectAverages(rc.getSubjectAverages() != null
                        ? rc.getSubjectAverages().stream()
                        .map(SubjectAverageResponse::from)
                        .collect(Collectors.toList())
                        : Collections.emptyList())
                .createdAt(rc.getCreatedAt())
                .updatedAt(rc.getUpdatedAt())
                .mention(computeMention(
                        rc.getOverallAverage(),
                        rc.getStudent().getAcademicProgram().getGradingScale()
                ))
                .build();
    }

    /** Mapping allégé (liste) — sans subjectAverages. */
    public static ReportCardResponse fromSummary(ReportCard rc) {
        return ReportCardResponse.builder()
                .id(rc.getId())
                .studentId(rc.getStudent().getId())
                .studentFullName(rc.getStudent().getFirstName() + " " + rc.getStudent().getLastName())
                .registrationNumber(rc.getStudent().getRegistrationNumber())
                .reportCardType(rc.getReportCardType().name())
                .semesterId(rc.getSemester() != null ? rc.getSemester().getId() : null)
                .semesterName(rc.getSemester() != null ? rc.getSemester().getName() : null)
                .academicYearId(rc.getAcademicYear().getId())
                .academicYearName(rc.getAcademicYear().getName())
                .overallAverage(rc.getOverallAverage())
                .validated(rc.isValidated())
                .validatedAt(rc.getValidatedAt())
                .subjectAverages(Collections.emptyList())
                .createdAt(rc.getCreatedAt())
                .updatedAt(rc.getUpdatedAt())
                .mention(computeMention(
                        rc.getOverallAverage(),
                        rc.getStudent().getAcademicProgram().getGradingScale()
                ))
                .build();
    }

    // Méthode statique utilitaire à ajouter dans ReportCardResponse :
    private static String computeMention(BigDecimal avg, int gradingScale) {
        if (avg == null) return null;
        double v = avg.doubleValue();
        if (gradingScale == AcademicConstants.GRADING_SCALE_UNIVERSITY) {
            if (v >= AcademicConstants.MENTION_TRES_BIEN_MIN_10)  return AcademicConstants.MENTION_TRES_BIEN;
            if (v >= AcademicConstants.MENTION_BIEN_MIN_10)       return AcademicConstants.MENTION_BIEN;
            if (v >= AcademicConstants.MENTION_ASSEZ_BIEN_MIN_10) return AcademicConstants.MENTION_ASSEZ_BIEN;
            if (v >= AcademicConstants.MENTION_PASSABLE_MIN_10)   return AcademicConstants.MENTION_PASSABLE;
            return AcademicConstants.MENTION_INSUFFISANT;
        } else {
            if (v >= AcademicConstants.MENTION_TRES_BIEN_MIN_20)  return AcademicConstants.MENTION_TRES_BIEN;
            if (v >= AcademicConstants.MENTION_BIEN_MIN_20)       return AcademicConstants.MENTION_BIEN;
            if (v >= AcademicConstants.MENTION_ASSEZ_BIEN_MIN_20) return AcademicConstants.MENTION_ASSEZ_BIEN;
            if (v >= AcademicConstants.MENTION_PASSABLE_MIN_20)   return AcademicConstants.MENTION_PASSABLE;
            return AcademicConstants.MENTION_INSUFFISANT;
        }
    }
}