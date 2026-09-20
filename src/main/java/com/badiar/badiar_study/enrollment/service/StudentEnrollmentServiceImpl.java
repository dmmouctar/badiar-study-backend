package com.badiar.badiar_study.enrollment.service;

import com.badiar.badiar_study.academic.entity.AcademicProgram;
import com.badiar.badiar_study.academic.entity.AcademicYear;
import com.badiar.badiar_study.academic.entity.Gender;
import com.badiar.badiar_study.academic.repository.AcademicProgramRepository;
import com.badiar.badiar_study.academic.repository.AcademicYearRepository;
import com.badiar.badiar_study.academic.repository.GenderRepository;
import com.badiar.badiar_study.common.dto.ApiResponse;
import com.badiar.badiar_study.common.exception.BadRequestException;
import com.badiar.badiar_study.common.exception.ResourceNotFoundException;
import com.badiar.badiar_study.enrollment.dto.request.CreateStudentRequest;
import com.badiar.badiar_study.enrollment.dto.request.UpdateStudentRequest;
import com.badiar.badiar_study.enrollment.dto.response.StudentResponse;
import com.badiar.badiar_study.enrollment.entity.Student;
import com.badiar.badiar_study.enrollment.repository.StudentRepository;
import com.badiar.badiar_study.user.entity.UserAccount;
import com.badiar.badiar_study.user.entity.UserAccount.UserRole;
import com.badiar.badiar_study.user.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentEnrollmentServiceImpl implements StudentEnrollmentService {

    private final StudentRepository studentRepository;
    private final UserAccountRepository userAccountRepository;
    private final GenderRepository genderRepository;
    private final AcademicProgramRepository academicProgramRepository;
    private final AcademicYearRepository academicYearRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public ApiResponse<StudentResponse> createStudent(CreateStudentRequest request) {
        log.info("Creating student with registration number: {}", request.getRegistrationNumber());

        if (userAccountRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Un compte avec cet email existe déjà : " + request.getEmail());
        }
        if (studentRepository.existsByRegistrationNumber(request.getRegistrationNumber())) {
            throw new BadRequestException("Un étudiant avec ce matricule existe déjà : " + request.getRegistrationNumber());
        }

        AcademicProgram program = academicProgramRepository.findById(request.getAcademicProgramId())
                .orElseThrow(() -> new ResourceNotFoundException("Filière", "id", request.getAcademicProgramId()));
        AcademicYear year = academicYearRepository.findById(request.getAcademicYearId())
                .orElseThrow(() -> new ResourceNotFoundException("Année académique", "id", request.getAcademicYearId()));
        Gender gender = resolveGender(request.getGenderId());

        UserAccount userAccount = UserAccount.builder()
                .firstName(request.getFirstName().trim())
                .lastName(request.getLastName().trim())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.ETUDIANT)
                .isActive(true)
                .build();
        userAccount = userAccountRepository.save(userAccount);

        Student student = Student.builder()
                .userAccount(userAccount)
                .firstName(request.getFirstName().trim())
                .lastName(request.getLastName().trim())
                .registrationNumber(request.getRegistrationNumber())
                .dateOfBirth(request.getDateOfBirth())
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .gender(gender)
                .academicProgram(program)
                .academicYear(year)
                .currentLevel(request.getCurrentLevel())
                .build();
        student = studentRepository.save(student);

        log.info("Student created successfully with id: {}", student.getId());
        return ApiResponse.success("Étudiant créé avec succès", mapToResponse(student));
    }

    @Override
    @Transactional
    public ApiResponse<StudentResponse> updateStudent(Long id, UpdateStudentRequest request) {
        log.info("Updating student with id: {}", id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Étudiant", "id", id));
        AcademicProgram program = academicProgramRepository.findById(request.getAcademicProgramId())
                .orElseThrow(() -> new ResourceNotFoundException("Filière", "id", request.getAcademicProgramId()));
        AcademicYear year = academicYearRepository.findById(request.getAcademicYearId())
                .orElseThrow(() -> new ResourceNotFoundException("Année académique", "id", request.getAcademicYearId()));
        Gender gender = resolveGender(request.getGenderId());

        student.setFirstName(request.getFirstName().trim());
        student.setLastName(request.getLastName().trim());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setAddress(request.getAddress());
        student.setPhoneNumber(request.getPhoneNumber());
        student.setGender(gender);
        student.setAcademicProgram(program);
        student.setAcademicYear(year);
        student.setCurrentLevel(request.getCurrentLevel());

        UserAccount userAccount = student.getUserAccount();
        userAccount.setFirstName(request.getFirstName().trim());
        userAccount.setLastName(request.getLastName().trim());
        userAccountRepository.save(userAccount);

        student = studentRepository.save(student);
        log.info("Student updated successfully with id: {}", id);
        return ApiResponse.success("Étudiant mis à jour avec succès", mapToResponse(student));
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<StudentResponse> getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Étudiant", "id", id));
        return ApiResponse.success("Étudiant récupéré avec succès", mapToResponse(student));
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<StudentResponse> getStudentByRegistrationNumber(String registrationNumber) {
        Student student = studentRepository.findByRegistrationNumber(registrationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Étudiant", "matricule", registrationNumber));
        return ApiResponse.success("Étudiant récupéré avec succès", mapToResponse(student));
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<Page<StudentResponse>> getAllStudents(Pageable pageable) {
        return ApiResponse.success("Liste des étudiants récupérée avec succès",
                studentRepository.findAll(pageable).map(this::mapToResponse));
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<Page<StudentResponse>> getStudentsByProgram(Long programId, Pageable pageable) {
        if (!academicProgramRepository.existsById(programId)) {
            throw new ResourceNotFoundException("Filière", "id", programId);
        }
        return ApiResponse.success("Étudiants de la filière récupérés avec succès",
                studentRepository.findByAcademicProgramId(programId, pageable).map(this::mapToResponse));
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<Page<StudentResponse>> getStudentsByProgramAndYear(Long programId, Long yearId, Pageable pageable) {
        if (!academicProgramRepository.existsById(programId)) {
            throw new ResourceNotFoundException("Filière", "id", programId);
        }
        if (!academicYearRepository.existsById(yearId)) {
            throw new ResourceNotFoundException("Année académique", "id", yearId);
        }
        return ApiResponse.success("Étudiants récupérés avec succès",
                studentRepository.findByAcademicProgramIdAndAcademicYearId(programId, yearId, pageable).map(this::mapToResponse));
    }

    @Override
    @Transactional
    public ApiResponse<Void> deleteStudent(Long id) {
        log.info("Deleting student with id: {}", id);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Étudiant", "id", id));

        UserAccount userAccount = student.getUserAccount();
        studentRepository.delete(student);

        userAccount.setIsActive(false);
        userAccountRepository.save(userAccount);

        log.info("Student deleted and account deactivated for id: {}", id);
        return ApiResponse.success("Étudiant supprimé avec succès", null);
    }

    // ─── Helpers ────────────────────────────────────────────────────────────────

    private Gender resolveGender(Long genderId) {
        if (genderId == null) return null;
        return genderRepository.findById(genderId)
                .orElseThrow(() -> new ResourceNotFoundException("Genre", "id", genderId));
    }

    private StudentResponse mapToResponse(Student student) {
        return StudentResponse.builder()
                .id(student.getId())
                .userId(student.getUserAccount().getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getUserAccount().getEmail())
                .registrationNumber(student.getRegistrationNumber())
                .dateOfBirth(student.getDateOfBirth())
                .address(student.getAddress())
                .phoneNumber(student.getPhoneNumber())
                .genderId(student.getGender() != null ? student.getGender().getId() : null)
                .genderName(student.getGender() != null ? student.getGender().getName() : null)
                .academicProgramId(student.getAcademicProgram().getId())
                .academicProgramName(student.getAcademicProgram().getName())
                .academicYearId(student.getAcademicYear().getId())
                .academicYearName(student.getAcademicYear().getName())
                .currentLevel(student.getCurrentLevel())
                .isActive(student.getUserAccount().getIsActive())
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .build();
    }
}