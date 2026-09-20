package com.badiar.badiar_study.enrollment.controller;

import com.badiar.badiar_study.common.dto.ApiResponse;
import com.badiar.badiar_study.enrollment.dto.request.CreateStudentRequest;
import com.badiar.badiar_study.enrollment.dto.request.UpdateStudentRequest;
import com.badiar.badiar_study.enrollment.dto.response.StudentResponse;
import com.badiar.badiar_study.enrollment.service.StudentEnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/enrollment/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentEnrollmentService studentEnrollmentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<StudentResponse>> createStudent(
            @Valid @RequestBody CreateStudentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(studentEnrollmentService.createStudent(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Page<StudentResponse>>> getAllStudents(
            @PageableDefault(size = 20, sort = "lastName") Pageable pageable) {
        return ResponseEntity.ok(studentEnrollmentService.getAllStudents(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<StudentResponse>> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentEnrollmentService.getStudentById(id));
    }

    @GetMapping("/registration/{registrationNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<StudentResponse>> getStudentByRegistrationNumber(
            @PathVariable String registrationNumber) {
        return ResponseEntity.ok(studentEnrollmentService.getStudentByRegistrationNumber(registrationNumber));
    }

    @GetMapping("/program/{programId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Page<StudentResponse>>> getStudentsByProgram(
            @PathVariable Long programId,
            @PageableDefault(size = 20, sort = "lastName") Pageable pageable) {
        return ResponseEntity.ok(studentEnrollmentService.getStudentsByProgram(programId, pageable));
    }

    @GetMapping("/program/{programId}/year/{yearId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Page<StudentResponse>>> getStudentsByProgramAndYear(
            @PathVariable Long programId,
            @PathVariable Long yearId,
            @PageableDefault(size = 20, sort = "lastName") Pageable pageable) {
        return ResponseEntity.ok(studentEnrollmentService.getStudentsByProgramAndYear(programId, yearId, pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<StudentResponse>> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStudentRequest request) {
        return ResponseEntity.ok(studentEnrollmentService.updateStudent(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable Long id) {
        return ResponseEntity.ok(studentEnrollmentService.deleteStudent(id));
    }
}