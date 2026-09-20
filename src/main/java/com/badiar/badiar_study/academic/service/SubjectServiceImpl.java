package com.badiar.badiar_study.academic.service;

import com.badiar.badiar_study.academic.dto.request.SubjectRequest;
import com.badiar.badiar_study.academic.dto.response.SubjectResponse;
import com.badiar.badiar_study.academic.entity.AcademicProgram;
import com.badiar.badiar_study.academic.entity.AcademicSemester;
import com.badiar.badiar_study.academic.entity.ProgramYearLevel;
import com.badiar.badiar_study.academic.entity.Subject;
import com.badiar.badiar_study.academic.repository.AcademicProgramRepository;
import com.badiar.badiar_study.academic.repository.AcademicSemesterRepository;
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
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final AcademicProgramRepository programRepository;
    private final AcademicSemesterRepository semesterRepository;

    @Override
    @Transactional
    public SubjectResponse createSubject(SubjectRequest request) {
        AcademicProgram program = findProgram(request.getAcademicProgramId());
        AcademicSemester semester = findSemester(request.getSemesterId());
        if (subjectRepository.existsByNameIgnoreCaseAndSemesterId(request.getName(), request.getSemesterId())) {
            throw new BadRequestException("La matière '" + request.getName() + "' existe déjà dans ce semestre");
        }
        Subject subject = Subject.builder()
                .academicProgram(program)
                .semester(semester)
                .name(request.getName())
                .coefficient(request.getCoefficient())
                .build();
        subject = subjectRepository.save(subject);
        log.info("Matière créée : id={}, name={}", subject.getId(), subject.getName());
        return toResponse(subject);
    }

    @Override
    @Transactional
    public SubjectResponse updateSubject(Long id, SubjectRequest request) {
        Subject subject = findById(id);
        AcademicProgram program = findProgram(request.getAcademicProgramId());
        AcademicSemester semester = findSemester(request.getSemesterId());
        if (subjectRepository.existsByNameIgnoreCaseAndSemesterIdAndIdNot(request.getName(), request.getSemesterId(), id)) {
            throw new BadRequestException("La matière '" + request.getName() + "' existe déjà dans ce semestre");
        }
        subject.setAcademicProgram(program);
        subject.setSemester(semester);
        subject.setName(request.getName());
        subject.setCoefficient(request.getCoefficient());
        subject = subjectRepository.save(subject);
        log.info("Matière mise à jour : id={}", id);
        return toResponse(subject);
    }

    @Override
    @Transactional(readOnly = true)
    public SubjectResponse getSubjectById(Long id) {
        return toResponse(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubjectResponse> getBySemester(Long semesterId) {
        return subjectRepository.findBySemesterIdOrderByNameAsc(semesterId).stream()
                .map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubjectResponse> getByProgram(Long programId) {
        return subjectRepository.findByAcademicProgramIdOrderByNameAsc(programId).stream()
                .map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubjectResponse> searchWithFilters(String keyword, Long programId, Long semId, Pageable pageable) {
        return subjectRepository.searchWithFilters(keyword, programId, semId, pageable).map(this::toResponse);
    }

    @Override
    @Transactional
    public void deleteSubject(Long id) {
        subjectRepository.delete(findById(id));
        log.info("Matière supprimée : id={}", id);
    }

    private Subject findById(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Matière", "id", id));
    }

    private AcademicProgram findProgram(Long id) {
        return programRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Filière", "id", id));
    }

    private AcademicSemester findSemester(Long id) {
        return semesterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Semestre", "id", id));
    }

    private SubjectResponse toResponse(Subject s) {
        ProgramYearLevel pyl = s.getSemester().getProgramYearLevel();
        return SubjectResponse.builder()
                .id(s.getId())
                .name(s.getName())
                .coefficient(s.getCoefficient())
                .academicProgramId(s.getAcademicProgram().getId())
                .academicProgramName(s.getAcademicProgram().getName())
                .gradingScale(s.getAcademicProgram().getGradingScale())
                .semesterId(s.getSemester().getId())
                .semesterName(s.getSemester().getName())
                .semesterDisplayOrder(s.getSemester().getDisplayOrder())
                .programYearLevelId(pyl.getId())
                .levelLabel(pyl.getLevelLabel())
                .levelNumber(pyl.getLevelNumber())
                .academicYearId(pyl.getAcademicYear().getId())
                .academicYearName(pyl.getAcademicYear().getName())
                .createdAt(s.getCreatedAt())
                .updatedAt(s.getUpdatedAt())
                .build();
    }
}