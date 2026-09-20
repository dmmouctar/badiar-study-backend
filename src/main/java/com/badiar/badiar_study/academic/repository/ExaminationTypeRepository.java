package com.badiar.badiar_study.academic.repository;

import com.badiar.badiar_study.academic.entity.ExaminationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ExaminationTypeRepository extends JpaRepository<ExaminationType, Long> {

    @Transactional(readOnly = true)
    Optional<ExaminationType> findByNameIgnoreCase(String name);

    @Transactional(readOnly = true)
    boolean existsByNameIgnoreCase(String name);

    @Transactional(readOnly = true)
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}
