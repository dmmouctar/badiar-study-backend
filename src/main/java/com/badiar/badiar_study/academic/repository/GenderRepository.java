package com.badiar.badiar_study.academic.repository;

import com.badiar.badiar_study.academic.entity.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface GenderRepository extends JpaRepository<Gender, Long> {

    @Transactional(readOnly = true)
    Optional<Gender> findByNameIgnoreCase(String name);

    @Transactional(readOnly = true)
    boolean existsByNameIgnoreCase(String name);
}
