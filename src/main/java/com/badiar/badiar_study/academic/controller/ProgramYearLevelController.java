package com.badiar.badiar_study.academic.controller;

import com.badiar.badiar_study.academic.dto.request.ProgramYearLevelRequest;
import com.badiar.badiar_study.academic.dto.response.ProgramYearLevelResponse;
import com.badiar.badiar_study.academic.service.ProgramYearLevelService;
import com.badiar.badiar_study.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/academic/program-year-levels")
@RequiredArgsConstructor
public class ProgramYearLevelController {

    private final ProgramYearLevelService pylService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProgramYearLevelResponse>>> searchWithFilters(
            @RequestParam(required = false) Long programId,
            @RequestParam(required = false) Long yearId) {
        return ResponseEntity.ok(ApiResponse.success(pylService.searchWithFilters(programId, yearId)));
    }

    @GetMapping("/by-program/{programId}")
    public ResponseEntity<ApiResponse<List<ProgramYearLevelResponse>>> getByProgram(
            @PathVariable Long programId) {
        return ResponseEntity.ok(ApiResponse.success(pylService.getByProgram(programId)));
    }

    @GetMapping("/by-program/{programId}/year/{yearId}")
    public ResponseEntity<ApiResponse<List<ProgramYearLevelResponse>>> getByProgramAndYear(
            @PathVariable Long programId,
            @PathVariable Long yearId) {
        return ResponseEntity.ok(ApiResponse.success(pylService.getByProgramAndYear(programId, yearId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProgramYearLevelResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(pylService.getProgramYearLevelById(id)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<ApiResponse<ProgramYearLevelResponse>> create(
            @Valid @RequestBody ProgramYearLevelRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(pylService.createProgramYearLevel(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<ApiResponse<ProgramYearLevelResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ProgramYearLevelRequest request) {
        return ResponseEntity.ok(ApiResponse.success(pylService.updateProgramYearLevel(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pylService.deleteProgramYearLevel(id);
        return ResponseEntity.noContent().build();
    }
}