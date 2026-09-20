package com.badiar.badiar_study.academic.service;

import com.badiar.badiar_study.academic.dto.response.GenderResponse;
import com.badiar.badiar_study.academic.entity.Gender;
import com.badiar.badiar_study.academic.repository.GenderRepository;
import com.badiar.badiar_study.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenderServiceImpl implements GenderService {

    private final GenderRepository genderRepository;

    @Override
    @Transactional(readOnly = true)
    public List<GenderResponse> getAllGenders() {
        return genderRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public GenderResponse getGenderById(Long id) {
        Gender gender = genderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre", "id", id));
        return toResponse(gender);
    }

    private GenderResponse toResponse(Gender gender) {
        return GenderResponse.builder()
                .id(gender.getId())
                .name(gender.getName())
                .build();
    }
}