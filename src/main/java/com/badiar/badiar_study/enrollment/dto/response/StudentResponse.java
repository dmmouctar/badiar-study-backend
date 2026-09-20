package com.badiar.badiar_study.enrollment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponse {
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String registrationNumber;
    private LocalDate dateOfBirth;
    private String address;
    private String phoneNumber;
    private Long genderId;
    private String genderName;
    private Long academicProgramId;
    private String academicProgramName;
    private Long academicYearId;
    private String academicYearName;
    private Integer currentLevel;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}