package com.badiar.badiar_study.academic.service;

import com.badiar.badiar_study.academic.dto.request.AcademicYearRequest;
import com.badiar.badiar_study.academic.dto.response.AcademicYearResponse;
import com.badiar.badiar_study.academic.entity.AcademicYear;
import com.badiar.badiar_study.academic.repository.AcademicYearRepository;
import com.badiar.badiar_study.common.exception.BadRequestException;
import com.badiar.badiar_study.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AcademicYearServiceImpl implements AcademicYearService {

    private final AcademicYearRepository yearRepository;

    @Override
    @Transactional
    public AcademicYearResponse createYear(AcademicYearRequest request) {
        if (yearRepository.existsByName(request.getName())) {
            throw new BadRequestException("L'année académique '" + request.getName() + "' existe déjà");
        }
        validateDates(request.getStartDate(), request.getEndDate());
        AcademicYear year = AcademicYear.builder()
                .name(request.getName())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();
        year = yearRepository.save(year);
        log.info("Année académique créée : {}", year.getName());
        return toResponse(year);
    }

    @Override
    @Transactional
    public AcademicYearResponse updateYear(Long id, AcademicYearRequest request) {
        AcademicYear year = findById(id);
        if (yearRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new BadRequestException("L'année académique '" + request.getName() + "' existe déjà");
        }
        validateDates(request.getStartDate(), request.getEndDate());
        year.setName(request.getName());
        year.setStartDate(request.getStartDate());
        year.setEndDate(request.getEndDate());
        year = yearRepository.save(year);
        log.info("Année académique mise à jour : id={}", id);
        return toResponse(year);
    }

    @Override
    @Transactional(readOnly = true)
    public AcademicYearResponse getYearById(Long id) {
        return toResponse(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AcademicYearResponse> getAllActiveYears() {
        return yearRepository.findByIsActiveTrueOrderByNameDesc().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AcademicYearResponse> searchYears(String keyword, Boolean isActive, Pageable pageable) {
        return yearRepository.searchWithFilters(keyword, isActive, pageable).map(this::toResponse);
    }

    @Override
    @Transactional
    public AcademicYearResponse toggleYearStatus(Long id) {
        AcademicYear year = findById(id);
        year.setIsActive(!year.getIsActive());
        year = yearRepository.save(year);
        log.info("Statut année académique modifié : id={}, isActive={}", id, year.getIsActive());
        return toResponse(year);
    }

    @Override
    @Transactional
    public void deleteYear(Long id) {
        yearRepository.delete(findById(id));
        log.info("Année académique supprimée : id={}", id);
    }

    private AcademicYear findById(Long id) {
        return yearRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Année académique", "id", id));
    }

    private void validateDates(LocalDate startDate, LocalDate endDate) {
        if (!endDate.isAfter(startDate)) {
            throw new BadRequestException("La date de fin doit être postérieure à la date de début");
        }
    }

    private AcademicYearResponse toResponse(AcademicYear y) {
        return AcademicYearResponse.builder()
                .id(y.getId())
                .name(y.getName())
                .startDate(y.getStartDate())
                .endDate(y.getEndDate())
                .isActive(y.getIsActive())
                .createdAt(y.getCreatedAt())
                .updatedAt(y.getUpdatedAt())
                .build();
    }
}