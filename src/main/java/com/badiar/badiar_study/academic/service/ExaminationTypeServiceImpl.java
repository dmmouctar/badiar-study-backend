package com.badiar.badiar_study.academic.service;

import com.badiar.badiar_study.academic.dto.request.ExaminationTypeRequest;
import com.badiar.badiar_study.academic.dto.response.ExaminationTypeResponse;
import com.badiar.badiar_study.academic.entity.ExaminationType;
import com.badiar.badiar_study.academic.repository.ExaminationTypeRepository;
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
public class ExaminationTypeServiceImpl implements ExaminationTypeService {

    private final ExaminationTypeRepository typeRepository;

    @Override
    @Transactional
    public ExaminationTypeResponse createExaminationType(ExaminationTypeRequest request) {
        if (typeRepository.existsByNameIgnoreCase(request.getName())) {
            throw new BadRequestException("Le type d'examen '" + request.getName() + "' existe déjà");
        }
        ExaminationType type = ExaminationType.builder().name(request.getName()).build();
        type = typeRepository.save(type);
        log.info("Type d'examen créé : id={}, name={}", type.getId(), type.getName());
        return toResponse(type);
    }

    @Override
    @Transactional
    public ExaminationTypeResponse updateExaminationType(Long id, ExaminationTypeRequest request) {
        ExaminationType type = findById(id);
        if (typeRepository.existsByNameIgnoreCaseAndIdNot(request.getName(), id)) {
            throw new BadRequestException("Le type d'examen '" + request.getName() + "' existe déjà");
        }
        type.setName(request.getName());
        type = typeRepository.save(type);
        log.info("Type d'examen mis à jour : id={}", id);
        return toResponse(type);
    }

    @Override
    @Transactional(readOnly = true)
    public ExaminationTypeResponse getExaminationTypeById(Long id) {
        return toResponse(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExaminationTypeResponse> getAllExaminationTypes() {
        return typeRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public void deleteExaminationType(Long id) {
        typeRepository.delete(findById(id));
        log.info("Type d'examen supprimé : id={}", id);
    }

    private ExaminationType findById(Long id) {
        return typeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Type d'examen", "id", id));
    }

    private ExaminationTypeResponse toResponse(ExaminationType t) {
        return ExaminationTypeResponse.builder()
                .id(t.getId())
                .name(t.getName())
                .createdAt(t.getCreatedAt())
                .build();
    }
}