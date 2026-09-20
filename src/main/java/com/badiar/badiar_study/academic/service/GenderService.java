package com.badiar.badiar_study.academic.service;

import com.badiar.badiar_study.academic.dto.response.GenderResponse;
import java.util.List;

public interface GenderService {
    List<GenderResponse> getAllGenders();
    GenderResponse getGenderById(Long id);
}