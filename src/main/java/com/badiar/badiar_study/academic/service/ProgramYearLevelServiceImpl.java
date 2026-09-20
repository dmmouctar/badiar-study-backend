package com.badiar.badiar_study.academic.service;

import com.badiar.badiar_study.academic.dto.request.ProgramYearLevelRequest;
import com.badiar.badiar_study.academic.dto.response.ProgramYearLevelResponse;
import com.badiar.badiar_study.academic.entity.AcademicProgram;
import com.badiar.badiar_study.academic.entity.AcademicYear;
import com.badiar.badiar_study.academic.entity.ProgramYearLevel;
import com.badiar.badiar_study.academic.repository.AcademicProgramRepository;
import com.badiar.badiar_study.academic.repository.AcademicYearRepository;
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
public class ProgramYearLevelServiceImpl implements ProgramYearLevelService {

    private final ProgramYearLevelRepository pylRepository;
    private final AcademicProgramRepository programRepository;
    private final AcademicYearRepository yearRepository;

    @Override
    @Transactional
    public ProgramYearLevelResponse createProgramYearLevel(ProgramYearLevelRequest request) {
        AcademicProgram program = findProgram(request.getAcademicProgramId());
        AcademicYear year = findYear(request.getAcademicYearId());
        if (pylRepository.existsByAcademicProgramIdAndAcademicYearIdAndLevelNumber(
                request.getAcademicProgramId(), request.getAcademicYearId(), request.getLevelNumber())) {
            throw new BadRequestException("Ce niveau existe déjà pour cette filière et cette année académique");
        }
        ProgramYearLevel pyl = ProgramYearLevel.builder()
                .academicProgram(program)
                .academicYear(year)
                .levelNumber(request.getLevelNumber())
                .levelLabel(request.getLevelLabel())
                .build();
        pyl = pylRepository.save(pyl);
        log.info("Niveau créé : id={}, program={}, year={}, level={}", pyl.getId(), program.getName(), year.getName(), pyl.getLevelNumber());
        return toResponse(pyl);
    }

    @Override
    @Transactional
    public ProgramYearLevelResponse updateProgramYearLevel(Long id, ProgramYearLevelRequest request) {
        ProgramYearLevel pyl = findById(id);
        AcademicProgram program = findProgram(request.getAcademicProgramId());
        AcademicYear year = findYear(request.getAcademicYearId());
        boolean comboChanged = !pyl.getAcademicProgram().getId().equals(request.getAcademicProgramId())
                || !pyl.getAcademicYear().getId().equals(request.getAcademicYearId())
                || !pyl.getLevelNumber().equals(request.getLevelNumber());
        if (comboChanged && pylRepository.existsByAcademicProgramIdAndAcademicYearIdAndLevelNumber(
                request.getAcademicProgramId(), request.getAcademicYearId(), request.getLevelNumber())) {
            throw new BadRequestException("Ce niveau existe déjà pour cette filière et cette année académique");
        }
        pyl.setAcademicProgram(program);
        pyl.setAcademicYear(year);
        pyl.setLevelNumber(request.getLevelNumber());
        pyl.setLevelLabel(request.getLevelLabel());
        pyl = pylRepository.save(pyl);
        log.info("Niveau mis à jour : id={}", id);
        return toResponse(pyl);
    }

    @Override
    @Transactional(readOnly = true)
    public ProgramYearLevelResponse getProgramYearLevelById(Long id) {
        return toResponse(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgramYearLevelResponse> getByProgram(Long programId) {
        return pylRepository.findByAcademicProgramIdOrderByLevelNumberAsc(programId).stream()
                .map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgramYearLevelResponse> getByProgramAndYear(Long programId, Long yearId) {
        return pylRepository.findByAcademicProgramIdAndAcademicYearIdOrderByLevelNumberAsc(programId, yearId).stream()
                .map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgramYearLevelResponse> searchWithFilters(Long programId, Long yearId) {
        return pylRepository.searchWithFilters(programId, yearId).stream()
                .map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public void deleteProgramYearLevel(Long id) {
        pylRepository.delete(findById(id));
        log.info("Niveau supprimé : id={}", id);
    }

    private ProgramYearLevel findById(Long id) {
        return pylRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Niveau de programme", "id", id));
    }

    private AcademicProgram findProgram(Long id) {
        return programRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Filière", "id", id));
    }

    private AcademicYear findYear(Long id) {
        return yearRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Année académique", "id", id));
    }

    private ProgramYearLevelResponse toResponse(ProgramYearLevel pyl) {
        return ProgramYearLevelResponse.builder()
                .id(pyl.getId())
                .academicProgramId(pyl.getAcademicProgram().getId())
                .academicProgramName(pyl.getAcademicProgram().getName())
                .gradingScale(pyl.getAcademicProgram().getGradingScale())
                .academicYearId(pyl.getAcademicYear().getId())
                .academicYearName(pyl.getAcademicYear().getName())
                .levelNumber(pyl.getLevelNumber())
                .levelLabel(pyl.getLevelLabel())
                .createdAt(pyl.getCreatedAt())
                .build();
    }
}