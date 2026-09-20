package com.badiar.badiar_study.academic.service;

import com.badiar.badiar_study.academic.dto.request.ProgramYearLevelRequest;
import com.badiar.badiar_study.academic.dto.response.ProgramYearLevelResponse;
import java.util.List;

public interface ProgramYearLevelService {
    ProgramYearLevelResponse createProgramYearLevel(ProgramYearLevelRequest request);
    ProgramYearLevelResponse updateProgramYearLevel(Long id, ProgramYearLevelRequest request);
    ProgramYearLevelResponse getProgramYearLevelById(Long id);
    List<ProgramYearLevelResponse> getByProgram(Long programId);
    List<ProgramYearLevelResponse> getByProgramAndYear(Long programId, Long yearId);
    List<ProgramYearLevelResponse> searchWithFilters(Long programId, Long yearId);
    void deleteProgramYearLevel(Long id);
}