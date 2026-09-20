package com.badiar.badiar_study.academic.service;

import com.badiar.badiar_study.academic.dto.request.ExaminationRequest;
import com.badiar.badiar_study.academic.dto.response.ExaminationResponse;
import com.badiar.badiar_study.academic.entity.Examination;
import com.badiar.badiar_study.academic.entity.ExaminationType;
import com.badiar.badiar_study.academic.entity.Subject;
import com.badiar.badiar_study.academic.repository.ExaminationRepository;
import com.badiar.badiar_study.academic.repository.ExaminationTypeRepository;
import com.badiar.badiar_study.academic.repository.SubjectRepository;
import com.badiar.badiar_study.common.exception.BadRequestException;
import com.badiar.badiar_study.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExaminationServiceImpl implements ExaminationService {

    private final ExaminationRepository examinationRepository;
    private final SubjectRepository subjectRepository;
    private final ExaminationTypeRepository typeRepository;

    @Override
    @Transactional
    public ExaminationResponse createExamination(ExaminationRequest request) {
        Subject subject = findSubject(request.getSubjectId());
        ExaminationType type = findType(request.getExaminationTypeId());
        if (examinationRepository.existsBySubjectIdAndExamOrder(request.getSubjectId(), request.getExamOrder())) {
            throw new BadRequestException(
                    "Un examen avec l'ordre " + request.getExamOrder() + " existe déjà pour cette matière");
        }
        Examination exam = Examination.builder()
                .subject(subject)
                .examinationType(type)
                .examDate(request.getExamDate())
                .examOrder(request.getExamOrder())
                .build();
        exam = examinationRepository.save(exam);
        log.info("Examen créé : id={}, subjectId={}, order={}", exam.getId(), request.getSubjectId(), exam.getExamOrder());
        return toResponse(exam);
    }

    @Override
    @Transactional
    public ExaminationResponse updateExamination(Long id, ExaminationRequest request) {
        Examination exam = findById(id);
        Subject subject = findSubject(request.getSubjectId());
        ExaminationType type = findType(request.getExaminationTypeId());
        if (examinationRepository.existsBySubjectIdAndExamOrderAndIdNot(
                request.getSubjectId(), request.getExamOrder(), id)) {
            throw new BadRequestException(
                    "Un examen avec l'ordre " + request.getExamOrder() + " existe déjà pour cette matière");
        }
        exam.setSubject(subject);
        exam.setExaminationType(type);
        exam.setExamDate(request.getExamDate());
        exam.setExamOrder(request.getExamOrder());
        exam = examinationRepository.save(exam);
        log.info("Examen mis à jour : id={}", id);
        return toResponse(exam);
    }

    @Override
    @Transactional(readOnly = true)
    public ExaminationResponse getExaminationById(Long id) {
        return toResponse(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExaminationResponse> getBySubject(Long subjectId) {
        return examinationRepository.findBySubjectIdOrderByExamOrderAsc(subjectId).stream()
                .map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExaminationResponse> getBySemester(Long semesterId) {
        return examinationRepository.findBySemesterId(semesterId).stream()
                .map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExaminationResponse> getByProgramAndYear(Long programId, Long yearId) {
        return examinationRepository.findByProgramAndYear(programId, yearId).stream()
                .map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExaminationResponse> searchWithFilters(Long subjectId, Long typeId, Pageable pageable) {
        return examinationRepository.searchWithFilters(subjectId, typeId, pageable).map(this::toResponse);
    }

    @Override
    @Transactional
    public void deleteExamination(Long id) {
        examinationRepository.delete(findById(id));
        log.info("Examen supprimé : id={}", id);
    }

    private Examination findById(Long id) {
        return examinationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Examen", "id", id));
    }

    private Subject findSubject(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Matière", "id", id));
    }

    private ExaminationType findType(Long id) {
        return typeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Type d'examen", "id", id));
    }

    private ExaminationResponse toResponse(Examination e) {
        Subject s = e.getSubject();
        return ExaminationResponse.builder()
                .id(e.getId())
                .examOrder(e.getExamOrder())
                .examDate(e.getExamDate())
                .examinationTypeId(e.getExaminationType().getId())
                .examinationTypeName(e.getExaminationType().getName())
                .subjectId(s.getId())
                .subjectName(s.getName())
                .subjectCoefficient(s.getCoefficient())
                .semesterId(s.getSemester().getId())
                .semesterName(s.getSemester().getName())
                .semesterDisplayOrder(s.getSemester().getDisplayOrder())
                .academicProgramId(s.getAcademicProgram().getId())
                .academicProgramName(s.getAcademicProgram().getName())
                .gradingScale(s.getAcademicProgram().getGradingScale())
                .academicYearId(s.getSemester().getProgramYearLevel().getAcademicYear().getId())
                .academicYearName(s.getSemester().getProgramYearLevel().getAcademicYear().getName())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }
}