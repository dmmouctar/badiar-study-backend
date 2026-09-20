package com.badiar.badiar_study.grading.controller;

import com.badiar.badiar_study.common.dto.ApiResponse;
import com.badiar.badiar_study.grading.dto.request.CreateGradeRequest;
import com.badiar.badiar_study.grading.dto.request.UpdateGradeRequest;
import com.badiar.badiar_study.grading.dto.response.GradeResponse;
import com.badiar.badiar_study.grading.service.GradeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/grading/grades")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<GradeResponse>> create(@Valid @RequestBody CreateGradeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Note enregistrée avec succès.", gradeService.saveGrade(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<GradeResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateGradeRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Note mise à jour.", gradeService.updateGrade(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        gradeService.deleteGrade(id);
        return ResponseEntity.ok(ApiResponse.success("Note supprimée."));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<GradeResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(gradeService.findById(id)));
    }

    @GetMapping("/student/{studentId}/semester/{semesterId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<GradeResponse>>> findByStudentAndSemester(
            @PathVariable Long studentId,
            @PathVariable Long semesterId) {
        return ResponseEntity.ok(ApiResponse.success(
                gradeService.findByStudentAndSemester(studentId, semesterId)));
    }
}