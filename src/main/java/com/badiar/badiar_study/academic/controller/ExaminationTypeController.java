package com.badiar.badiar_study.academic.controller;

import com.badiar.badiar_study.academic.dto.request.ExaminationTypeRequest;
import com.badiar.badiar_study.academic.dto.response.ExaminationTypeResponse;
import com.badiar.badiar_study.academic.service.ExaminationTypeService;
import com.badiar.badiar_study.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/academic/examination-types")
@RequiredArgsConstructor
public class ExaminationTypeController {

    private final ExaminationTypeService typeService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ExaminationTypeResponse>>> getAllExaminationTypes() {
        return ResponseEntity.ok(ApiResponse.success(typeService.getAllExaminationTypes()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExaminationTypeResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(typeService.getExaminationTypeById(id)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<ApiResponse<ExaminationTypeResponse>> create(
            @Valid @RequestBody ExaminationTypeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(typeService.createExaminationType(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<ApiResponse<ExaminationTypeResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ExaminationTypeRequest request) {
        return ResponseEntity.ok(ApiResponse.success(typeService.updateExaminationType(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        typeService.deleteExaminationType(id);
        return ResponseEntity.noContent().build();
    }
}