package com.badiar.badiar_study.enrollment.service;

import com.badiar.badiar_study.common.dto.ApiResponse;
import com.badiar.badiar_study.enrollment.dto.request.CreateStudentRequest;
import com.badiar.badiar_study.enrollment.dto.request.UpdateStudentRequest;
import com.badiar.badiar_study.enrollment.dto.response.StudentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentEnrollmentService {
    ApiResponse<StudentResponse> createStudent(CreateStudentRequest request);
    ApiResponse<StudentResponse> updateStudent(Long id, UpdateStudentRequest request);
    ApiResponse<StudentResponse> getStudentById(Long id);
    ApiResponse<StudentResponse> getStudentByRegistrationNumber(String registrationNumber);
    ApiResponse<Page<StudentResponse>> getAllStudents(Pageable pageable);
    ApiResponse<Page<StudentResponse>> getStudentsByProgram(Long programId, Pageable pageable);
    ApiResponse<Page<StudentResponse>> getStudentsByProgramAndYear(Long programId, Long yearId, Pageable pageable);
    ApiResponse<Void> deleteStudent(Long id);
}