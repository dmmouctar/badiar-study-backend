package com.badiar.badiar_study.grading.controller;

import com.badiar.badiar_study.common.dto.ApiResponse;
import com.badiar.badiar_study.grading.dto.request.ValidateReportCardRequest;
import com.badiar.badiar_study.grading.dto.response.ReportCardResponse;
import com.badiar.badiar_study.grading.service.ReportCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/grading/report-cards")
@RequiredArgsConstructor
public class ReportCardController {

    private final ReportCardService reportCardService;

    @PostMapping("/semester")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<ReportCardResponse>> generateSemester(
            @RequestParam Long studentId,
            @RequestParam Long semesterId) {
        return ResponseEntity.ok(ApiResponse.success(
                "Bulletin semestriel généré avec succès.",
                reportCardService.generateSemesterReportCard(studentId, semesterId)));
    }

    @PostMapping("/annual")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<ReportCardResponse>> generateAnnual(
            @RequestParam Long studentId,
            @RequestParam Long academicYearId) {
        return ResponseEntity.ok(ApiResponse.success(
                "Bulletin annuel généré avec succès.",
                reportCardService.generateAnnualReportCard(studentId, academicYearId)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'ETUDIANT')")
    public ResponseEntity<ApiResponse<ReportCardResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(reportCardService.findById(id)));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Page<ReportCardResponse>>> findByStudent(
            @PathVariable Long studentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(ApiResponse.success(
                reportCardService.findByStudent(studentId, pageable)));
    }

    @PostMapping("/{id}/validate")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<ReportCardResponse>> validate(
            @PathVariable Long id,
            @Valid @RequestBody(required = false) ValidateReportCardRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        String notes = request != null ? request.getNotes() : null;
        return ResponseEntity.ok(ApiResponse.success(
                "Bulletin validé avec succès.",
                reportCardService.validateReportCard(id, userDetails.getUsername(), notes)));
    }
}