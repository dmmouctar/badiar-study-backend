package com.badiar.badiar_study.academic.service;

import com.badiar.badiar_study.academic.dto.request.AcademicProgramRequest;
import com.badiar.badiar_study.academic.dto.response.AcademicProgramResponse;
import com.badiar.badiar_study.academic.entity.AcademicProgram;
import com.badiar.badiar_study.academic.repository.AcademicProgramRepository;
import com.badiar.badiar_study.common.constant.AcademicConstants;
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
public class AcademicProgramServiceImpl implements AcademicProgramService {

    private final AcademicProgramRepository programRepository;

    @Override
    @Transactional
    public AcademicProgramResponse createProgram(AcademicProgramRequest request) {
        if (programRepository.existsByNameIgnoreCase(request.getName())) {
            throw new BadRequestException("Une filière avec le nom '" + request.getName() + "' existe déjà");
        }
        AcademicProgram program = AcademicProgram.builder()
                .name(request.getName())
                .description(request.getDescription())
                .gradingScale(request.getGradingScale())
                .build();
        program = programRepository.save(program);
        log.info("Filière créée : id={}, name={}", program.getId(), program.getName());
        return toResponse(program);
    }

    @Override
    @Transactional
    public AcademicProgramResponse updateProgram(Long id, AcademicProgramRequest request) {
        AcademicProgram program = findById(id);
        if (programRepository.existsByNameIgnoreCaseAndIdNot(request.getName(), id)) {
            throw new BadRequestException("Une filière avec le nom '" + request.getName() + "' existe déjà");
        }
        program.setName(request.getName());
        program.setDescription(request.getDescription());
        program.setGradingScale(request.getGradingScale());
        program = programRepository.save(program);
        log.info("Filière mise à jour : id={}", id);
        return toResponse(program);
    }

    @Override
    @Transactional(readOnly = true)
    public AcademicProgramResponse getProgramById(Long id) {
        return toResponse(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AcademicProgramResponse> getAllActivePrograms() {
        return programRepository.findByIsActiveTrueOrderByNameAsc().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AcademicProgramResponse> searchPrograms(String keyword, Integer gradingScale, Boolean isActive, Pageable pageable) {
        return programRepository.searchWithFilters(keyword, gradingScale, isActive, pageable)
                .map(this::toResponse);
    }

    @Override
    @Transactional
    public AcademicProgramResponse toggleProgramStatus(Long id) {
        AcademicProgram program = findById(id);
        program.setIsActive(!program.getIsActive());
        program = programRepository.save(program);
        log.info("Statut filière modifié : id={}, isActive={}", id, program.getIsActive());
        return toResponse(program);
    }

    @Override
    @Transactional
    public void deleteProgram(Long id) {
        programRepository.delete(findById(id));
        log.info("Filière supprimée : id={}", id);
    }

    private AcademicProgram findById(Long id) {
        return programRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Filière", "id", id));
    }

    private AcademicProgramResponse toResponse(AcademicProgram p) {
        String label = p.getGradingScale() == AcademicConstants.GRADING_SCALE_UNIVERSITY ? "/ 10" : "/ 20";
        return AcademicProgramResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .gradingScale(p.getGradingScale())
                .gradingScaleLabel(label)
                .isActive(p.getIsActive())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }
}