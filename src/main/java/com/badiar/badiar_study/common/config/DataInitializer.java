package com.badiar.badiar_study.common.config;

import com.badiar.badiar_study.academic.entity.ExaminationType;
import com.badiar.badiar_study.academic.entity.Gender;
import com.badiar.badiar_study.academic.repository.ExaminationTypeRepository;
import com.badiar.badiar_study.academic.repository.GenderRepository;
import com.badiar.badiar_study.user.entity.UserAccount;
import com.badiar.badiar_study.user.entity.UserAccount.UserRole;
import com.badiar.badiar_study.user.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserAccountRepository     userAccountRepository;
    private final PasswordEncoder           passwordEncoder;
    private final GenderRepository          genderRepository;
    private final ExaminationTypeRepository examinationTypeRepository;

    @Value("${app.super-admin.email}")
    private String superAdminEmail;

    @Value("${app.super-admin.password}")
    private String superAdminPassword;

    @Value("${app.super-admin.first-name}")
    private String superAdminFirstName;

    @Value("${app.super-admin.last-name}")
    private String superAdminLastName;

    @Override
    @Transactional
    public void run(String... args) {
        createSuperAdminIfNotExists();
        initializeGenders();
        initializeExaminationTypes();
    }

    private void createSuperAdminIfNotExists() {
        if (userAccountRepository.countByRole(UserRole.SUPER_ADMIN) == 0) {
            userAccountRepository.save(UserAccount.builder()
                    .firstName(superAdminFirstName)
                    .lastName(superAdminLastName)
                    .email(superAdminEmail)
                    .password(passwordEncoder.encode(superAdminPassword))
                    .role(UserRole.SUPER_ADMIN)
                    .isActive(true)
                    .build());

            log.info("======================================================");
            log.info("   Compte SUPER_ADMIN cree avec succes");
            log.info("   Email    : {}", superAdminEmail);
            log.info("-------------------------------------------------------");
        } else {
            log.info("SUPER_ADMIN deja existant — initialisation ignoree");
        }
    }

    private void initializeGenders() {
        if (genderRepository.count() == 0) {
            genderRepository.saveAll(List.of(
                    Gender.builder().name("Masculin").build(),
                    Gender.builder().name("Feminin").build()
            ));
            log.info("Genres initialises.");
        } else {
            log.debug("Genres deja presents — initialisation ignoree.");
        }
    }

    private void initializeExaminationTypes() {
        if (examinationTypeRepository.count() == 0) {
            examinationTypeRepository.saveAll(List.of(
                    ExaminationType.builder().name("Controle Continu").build(),
                    ExaminationType.builder().name("Examen Final").build(),
                    ExaminationType.builder().name("Devoir Mensuel").build(),
                    ExaminationType.builder().name("Rattrapage").build()
            ));
            log.info("Types d'examens initialises.");
        } else {
            log.debug("Types d'examens deja presents — initialisation ignoree.");
        }
    }
}