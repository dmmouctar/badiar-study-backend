package com.badiar.badiar_study.enrollment.repository;

import com.badiar.badiar_study.enrollment.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByRegistrationNumber(String registrationNumber);
    boolean existsByRegistrationNumber(String registrationNumber);
    Optional<Student> findByUserAccountId(Long userAccountId);
    Page<Student> findByAcademicProgramId(Long academicProgramId, Pageable pageable);
    Page<Student> findByAcademicProgramIdAndAcademicYearId(Long academicProgramId, Long academicYearId, Pageable pageable);
    Page<Student> findByCurrentLevel(Integer currentLevel, Pageable pageable);
}