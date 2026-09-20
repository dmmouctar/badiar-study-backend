package com.badiar.badiar_study.grading.service;

import com.badiar.badiar_study.academic.entity.Examination;
import com.badiar.badiar_study.academic.repository.ExaminationRepository;
import com.badiar.badiar_study.common.exception.BadRequestException;
import com.badiar.badiar_study.common.exception.ResourceNotFoundException;
import com.badiar.badiar_study.enrollment.entity.Student;
import com.badiar.badiar_study.enrollment.repository.StudentRepository;
import com.badiar.badiar_study.grading.dto.request.CreateGradeRequest;
import com.badiar.badiar_study.grading.dto.request.UpdateGradeRequest;
import com.badiar.badiar_study.grading.dto.response.GradeResponse;
import com.badiar.badiar_study.grading.entity.Grade;
import com.badiar.badiar_study.grading.repository.GradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GradeServiceImpl implements GradeService {

    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final ExaminationRepository examinationRepository;

    @Override
    @Transactional
    public GradeResponse saveGrade(CreateGradeRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", request.getStudentId()));

        Examination examination = examinationRepository.findById(request.getExaminationId())
                .orElseThrow(() -> new ResourceNotFoundException("Examination", "id", request.getExaminationId()));

        validateScore(request.getScore(), examination);

        if (gradeRepository.existsByStudentIdAndExaminationId(request.getStudentId(), request.getExaminationId())) {
            throw new BadRequestException("Une note existe déjà pour cet étudiant et cet examen.");
        }

        Grade saved = gradeRepository.save(Grade.builder()
                .student(student)
                .examination(examination)
                .score(request.getScore())
                .build());

        log.info("Note créée : étudiant={}, examen={}, note={}", saved.getStudent().getId(),
                saved.getExamination().getId(), saved.getScore());
        return GradeResponse.from(saved);
    }

    @Override
    @Transactional
    public GradeResponse updateGrade(Long id, UpdateGradeRequest request) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grade", "id", id));

        validateScore(request.getScore(), grade.getExamination());
        grade.setScore(request.getScore());

        log.info("Note mise à jour : id={}, score={}", id, request.getScore());
        return GradeResponse.from(gradeRepository.save(grade));
    }

    @Override
    @Transactional
    public void deleteGrade(Long id) {
        if (!gradeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Grade", "id", id);
        }
        gradeRepository.deleteById(id);
        log.info("Note supprimée : id={}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public GradeResponse findById(Long id) {
        return GradeResponse.from(gradeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grade", "id", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GradeResponse> findByStudentAndSemester(Long studentId, Long semesterId) {
        return gradeRepository.findAllByStudentIdAndSemesterId(studentId, semesterId)
                .stream().map(GradeResponse::from).collect(Collectors.toList());
    }

    private void validateScore(BigDecimal score, Examination examination) {
        int scale = examination.getSubject().getAcademicProgram().getGradingScale();
        if (score.compareTo(BigDecimal.ZERO) < 0
                || score.compareTo(BigDecimal.valueOf(scale)) > 0) {
            throw new BadRequestException(
                    String.format("La note doit être comprise entre 0 et %d.", scale));
        }
    }
}