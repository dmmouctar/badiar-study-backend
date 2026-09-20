package com.badiar.badiar_study.grading.repository;

import com.badiar.badiar_study.grading.entity.SubjectAverage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectAverageRepository extends JpaRepository<SubjectAverage, Long> {
    void deleteByReportCardId(Long reportCardId);
}