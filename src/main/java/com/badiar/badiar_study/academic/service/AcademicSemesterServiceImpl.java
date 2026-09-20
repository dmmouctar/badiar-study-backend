package com.badiar.badiar_study.academic.service;

import com.badiar.badiar_study.academic.dto.request.AcademicSemesterRequest;
import com.badiar.badiar_study.academic.dto.response.AcademicSemesterResponse;
import com.badiar.badiar_study.academic.entity.AcademicSemester;
import com.badiar.badiar_study.academic.entity.ProgramYearLevel;
import com.badiar.badiar_study.academic.repository.AcademicSemesterRepository;
import com.badiar.badiar_study.academic.repository.ProgramYearLevelRepository;
import com.badiar.badiar_study.common.exception.BadRequestException;
import com.badiar.badiar_study.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AcademicSemesterServiceImpl implements AcademicSemesterService {

    private final AcademicSemesterRepository semesterRepository;
    private final ProgramYearLevelRepository pylRepository;

    @Override
    @Transactional
    public AcademicSemesterResponse createSemester(AcademicSemesterRequest request) {
        ProgramYearLevel pyl = findPyl(request.getProgramYearLevelId());
        if (semesterRepository.existsByProgramYearLevelIdAndDisplayOrder(
                request.getProgramYearLevelId(), request.getDisplayOrder())) {
            throw new BadRequestException(
                    "Un semestre avec l'ordre " + request.getDisplayOrder() + " existe déjà pour ce niveau");
        }
        AcademicSemester semester = AcademicSemester.builder()
                .programYearLevel(pyl)
                .name(request.getName())
                .displayOrder(request.getDisplayOrder())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();
        semester = semesterRepository.save(semester);
        log.info("Semestre créé : id={}, name={}", semester.getId(), semester.getName());
        return toResponse(semester);
    }

    @Override
    @Transactional
    public AcademicSemesterResponse updateSemester(Long id, AcademicSemesterRequest request) {
        AcademicSemester semester = findById(id);
        ProgramYearLevel pyl = findPyl(request.getProgramYearLevelId());
        boolean orderChanged = !semester.getProgramYearLevel().getId().equals(request.getProgramYearLevelId())
                || !semester.getDisplayOrder().equals(request.getDisplayOrder());
        if (orderChanged && semesterRepository.existsByProgramYearLevelIdAndDisplayOrder(
                request.getProgramYearLevelId(), request.getDisplayOrder())) {
            throw new BadRequestException(
                    "Un semestre avec l'ordre " + request.getDisplayOrder() + " existe déjà pour ce niveau");
        }
        semester.setProgramYearLevel(pyl);
        semester.setName(request.getName());
        semester.setDisplayOrder(request.getDisplayOrder());
        semester.setStartDate(request.getStartDate());
        semester.setEndDate(request.getEndDate());
        semester = semesterRepository.save(semester);
        log.info("Semestre mis à jour : id={}", id);
        return toResponse(semester);
    }

    @Override
    @Transactional(readOnly = true)
    public AcademicSemesterResponse getSemesterById(Long id) {
        return toResponse(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AcademicSemesterResponse> getByProgramYearLevel(Long programYearLevelId) {
        return semesterRepository.findByProgramYearLevelIdOrderByDisplayOrderAsc(programYearLevelId).stream()
                .map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AcademicSemesterResponse> getByProgramAndYear(Long programId, Long yearId) {
        return semesterRepository.findByProgramAndYear(programId, yearId).stream()
                .map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public void deleteSemester(Long id) {
        semesterRepository.delete(findById(id));
        log.info("Semestre supprimé : id={}", id);
    }

    private AcademicSemester findById(Long id) {
        return semesterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Semestre", "id", id));
    }

    private ProgramYearLevel findPyl(Long id) {
        return pylRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Niveau de programme", "id", id));
    }

    private AcademicSemesterResponse toResponse(AcademicSemester s) {
        ProgramYearLevel pyl = s.getProgramYearLevel();
        return AcademicSemesterResponse.builder()
                .id(s.getId())
                .name(s.getName())
                .displayOrder(s.getDisplayOrder())
                .programYearLevelId(pyl.getId())
                .levelLabel(pyl.getLevelLabel())
                .academicProgramId(pyl.getAcademicProgram().getId())
                .academicProgramName(pyl.getAcademicProgram().getName())
                .academicYearId(pyl.getAcademicYear().getId())
                .academicYearName(pyl.getAcademicYear().getName())
                .startDate(s.getStartDate())
                .endDate(s.getEndDate())
                .createdAt(s.getCreatedAt())
                .updatedAt(s.getUpdatedAt())
                .build();
    }
}