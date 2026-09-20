package com.badiar.badiar_study.grading.service;

import com.badiar.badiar_study.academic.entity.AcademicSemester;
import com.badiar.badiar_study.academic.entity.AcademicYear;
import com.badiar.badiar_study.academic.entity.Subject;
import com.badiar.badiar_study.academic.repository.AcademicSemesterRepository;
import com.badiar.badiar_study.academic.repository.AcademicYearRepository;
import com.badiar.badiar_study.academic.repository.SubjectRepository;
import com.badiar.badiar_study.common.exception.BadRequestException;
import com.badiar.badiar_study.common.exception.ResourceNotFoundException;
import com.badiar.badiar_study.enrollment.entity.Student;
import com.badiar.badiar_study.enrollment.repository.StudentRepository;
import com.badiar.badiar_study.grading.dto.response.ReportCardResponse;
import com.badiar.badiar_study.grading.entity.Grade;
import com.badiar.badiar_study.grading.entity.ReportCard;
import com.badiar.badiar_study.grading.entity.ReportCard.ReportCardType;
import com.badiar.badiar_study.grading.entity.ReportCardValidation;
import com.badiar.badiar_study.grading.entity.SubjectAverage;
import com.badiar.badiar_study.grading.repository.GradeRepository;
import com.badiar.badiar_study.grading.repository.ReportCardRepository;
import com.badiar.badiar_study.grading.repository.ReportCardValidationRepository;
import com.badiar.badiar_study.grading.repository.SubjectAverageRepository;
import com.badiar.badiar_study.user.entity.UserAccount;
import com.badiar.badiar_study.user.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportCardServiceImpl implements ReportCardService {

    private final ReportCardRepository reportCardRepository;
    private final SubjectAverageRepository subjectAverageRepository;
    private final ReportCardValidationRepository validationRepository;
    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final AcademicSemesterRepository semesterRepository;
    private final AcademicYearRepository academicYearRepository;
    private final SubjectRepository subjectRepository;
    private final UserAccountRepository userAccountRepository;

    // ------------------------------------------------------------------ //
    //  GÉNÉRATION BULLETIN SEMESTRE                                        //
    // ------------------------------------------------------------------ //

    @Override
    @Transactional
    public ReportCardResponse generateSemesterReportCard(Long studentId, Long semesterId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));

        AcademicSemester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new ResourceNotFoundException("AcademicSemester", "id", semesterId));

        ReportCard reportCard = reportCardRepository
                .findByStudentIdAndSemesterId(studentId, semesterId)
                .orElseGet(() -> ReportCard.builder()
                        .student(student)
                        .semester(semester)
                        .academicYear(semester.getProgramYearLevel().getAcademicYear())
                        .reportCardType(ReportCardType.SEMESTRE)
                        .build());

        if (reportCard.isValidated()) {
            throw new BadRequestException("Ce bulletin est validé et ne peut plus être recalculé.");
        }

        // Persister d'abord pour obtenir l'ID (nécessaire pour SubjectAverage FK)
        reportCard = reportCardRepository.save(reportCard);

        List<Subject> subjects = subjectRepository.findBySemesterIdOrderByNameAsc(semesterId);
        if (subjects.isEmpty()) {
            throw new BadRequestException("Aucune matière associée à ce semestre.");
        }

        List<Grade> grades = gradeRepository.findAllByStudentIdAndSemesterId(studentId, semesterId);
        Map<Long, List<Grade>> gradesBySubject = grades.stream()
                .collect(Collectors.groupingBy(g -> g.getExamination().getSubject().getId()));

        List<SubjectAverage> subjectAverages = new ArrayList<>();
        BigDecimal totalWeighted = BigDecimal.ZERO;
        BigDecimal totalCoefficients = BigDecimal.ZERO;

        for (Subject subject : subjects) {
            List<Grade> sg = gradesBySubject.getOrDefault(subject.getId(), Collections.emptyList());

            BigDecimal avg = null;
            BigDecimal weighted = null;

            if (!sg.isEmpty()) {
                BigDecimal sum = sg.stream().map(Grade::getScore).reduce(BigDecimal.ZERO, BigDecimal::add);
                avg = sum.divide(BigDecimal.valueOf(sg.size()), 2, RoundingMode.HALF_UP);
                weighted = avg.multiply(subject.getCoefficient()).setScale(2, RoundingMode.HALF_UP);
                totalWeighted = totalWeighted.add(weighted);
                totalCoefficients = totalCoefficients.add(subject.getCoefficient());
            }

            subjectAverages.add(SubjectAverage.builder()
                    .reportCard(reportCard)
                    .subject(subject)
                    .averageScore(avg)
                    .weightedScore(weighted)
                    .build());
        }

        BigDecimal overallAverage = totalCoefficients.compareTo(BigDecimal.ZERO) > 0
                ? totalWeighted.divide(totalCoefficients, 2, RoundingMode.HALF_UP)
                : null;

        subjectAverageRepository.deleteByReportCardId(reportCard.getId());
        subjectAverageRepository.saveAll(subjectAverages);
        reportCard.setOverallAverage(overallAverage);
        ReportCard saved = reportCardRepository.save(reportCard);

        log.info("Bulletin SEMESTRE généré : étudiant={}, semestre={}, moyenne={}",
                studentId, semesterId, overallAverage);
        reportCardRepository.flush();
        return ReportCardResponse.from(
                reportCardRepository.findByIdWithDetails(saved.getId()).orElseThrow());
    }

    // ------------------------------------------------------------------ //
    //  GÉNÉRATION BULLETIN ANNUEL                                          //
    // ------------------------------------------------------------------ //

    @Override
    @Transactional
    public ReportCardResponse generateAnnualReportCard(Long studentId, Long academicYearId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));

        AcademicYear year = academicYearRepository.findById(academicYearId)
                .orElseThrow(() -> new ResourceNotFoundException("AcademicYear", "id", academicYearId));

        List<ReportCard> semesterCards = reportCardRepository
                .findByStudentIdAndAcademicYearIdAndReportCardType(studentId, academicYearId, ReportCardType.SEMESTRE);

        if (semesterCards.isEmpty()) {
            throw new BadRequestException(
                    "Aucun bulletin semestriel trouvé. Générez d'abord les bulletins de semestre.");
        }

        // Moyenne annuelle = moyenne simple des moyennes semestrielles (si calculées)
        List<BigDecimal> averages = semesterCards.stream()
                .map(ReportCard::getOverallAverage)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        BigDecimal annualAverage = null;
        if (!averages.isEmpty()) {
            BigDecimal sum = averages.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            annualAverage = sum.divide(BigDecimal.valueOf(averages.size()), 2, RoundingMode.HALF_UP);
        }

        List<ReportCard> existing = reportCardRepository
                .findByStudentIdAndAcademicYearIdAndReportCardType(studentId, academicYearId, ReportCardType.ANNUEL);

        ReportCard annualCard;
        if (!existing.isEmpty()) {
            annualCard = existing.get(0);
            if (annualCard.isValidated()) {
                throw new BadRequestException("Le bulletin annuel est validé et ne peut plus être recalculé.");
            }
        } else {
            annualCard = ReportCard.builder()
                    .student(student)
                    .academicYear(year)
                    .reportCardType(ReportCardType.ANNUEL)
                    .build();
        }

        annualCard.setOverallAverage(annualAverage);
        ReportCard saved = reportCardRepository.save(annualCard);

        log.info("Bulletin ANNUEL généré : étudiant={}, année={}, moyenne={}",
                studentId, academicYearId, annualAverage);
        reportCardRepository.flush();
        return ReportCardResponse.from(
                reportCardRepository.findByIdWithDetails(saved.getId()).orElseThrow());
    }

    // ------------------------------------------------------------------ //
    //  VALIDATION                                                          //
    // ------------------------------------------------------------------ //

    @Override
    @Transactional
    public ReportCardResponse validateReportCard(Long id, String adminEmail, String notes) {
        ReportCard reportCard = reportCardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReportCard", "id", id));

        if (reportCard.isValidated()) {
            throw new BadRequestException("Ce bulletin est déjà validé.");
        }

        UserAccount admin = userAccountRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new ResourceNotFoundException("UserAccount", "email", adminEmail));

        LocalDateTime now = LocalDateTime.now();

        ReportCardValidation validation = validationRepository.findByReportCardId(id)
                .orElseGet(() -> ReportCardValidation.builder().reportCard(reportCard).build());

        validation.setValidatedBy(admin);
        validation.setValidated(true);
        validation.setValidatedAt(now);
        validation.setNotes(notes);
        validationRepository.save(validation);

        reportCard.setValidated(true);
        reportCard.setValidatedAt(now);
        ReportCard saved = reportCardRepository.save(reportCard);

        log.info("Bulletin validé : id={}, admin={}", id, adminEmail);
        return ReportCardResponse.from(
                reportCardRepository.findByIdWithDetails(saved.getId()).orElseThrow());
    }

    // ------------------------------------------------------------------ //
    //  LECTURE                                                             //
    // ------------------------------------------------------------------ //

    @Override
    @Transactional(readOnly = true)
    public ReportCardResponse findById(Long id) {
        return ReportCardResponse.from(
                reportCardRepository.findByIdWithDetails(id)
                        .orElseThrow(() -> new ResourceNotFoundException("ReportCard", "id", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReportCardResponse> findByStudent(Long studentId, Pageable pageable) {
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Student", "id", studentId);
        }
        return reportCardRepository.findByStudentId(studentId, pageable)
                .map(ReportCardResponse::fromSummary);
    }
}