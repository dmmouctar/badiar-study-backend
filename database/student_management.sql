-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : mer. 16 sep. 2026 à 05:50
-- Version du serveur : 10.4.27-MariaDB
-- Version de PHP : 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `student_management`
--

-- --------------------------------------------------------

--
-- Structure de la table `academic_programs`
--

CREATE TABLE `academic_programs` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `name` varchar(150) NOT NULL COMMENT 'ex: Génie Informatique et Télécommunications',
  `description` text DEFAULT NULL,
  `grading_scale` int(11) NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT 1,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ;

--
-- Déchargement des données de la table `academic_programs`
--

INSERT INTO `academic_programs` (`id`, `name`, `description`, `grading_scale`, `is_active`, `created_at`, `updated_at`) VALUES
(1, 'Genie Informatique et Telecoms', 'Filiere informatique et telecoms', 10, 1, '2026-09-16 03:02:50', '2026-09-16 03:08:06');

-- --------------------------------------------------------

--
-- Structure de la table `academic_semesters`
--

CREATE TABLE `academic_semesters` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `program_year_level_id` bigint(20) UNSIGNED NOT NULL,
  `name` varchar(50) NOT NULL COMMENT 'ex: Module 1, Trimestre 2, Semestre 1',
  `display_order` int(11) NOT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `academic_semesters`
--

INSERT INTO `academic_semesters` (`id`, `program_year_level_id`, `name`, `display_order`, `start_date`, `end_date`, `created_at`, `updated_at`) VALUES
(1, 1, 'Module 1 - Semestre 1', 1, '2024-10-01', '2025-01-31', '2026-09-16 03:24:51', '2026-09-16 03:28:18');

-- --------------------------------------------------------

--
-- Structure de la table `academic_years`
--

CREATE TABLE `academic_years` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `name` varchar(20) NOT NULL COMMENT 'ex: 2024-2025',
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT 1,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ;

--
-- Déchargement des données de la table `academic_years`
--

INSERT INTO `academic_years` (`id`, `name`, `start_date`, `end_date`, `is_active`, `created_at`, `updated_at`) VALUES
(1, '2024-2025', '2024-09-15', '2025-07-31', 1, '2026-09-16 03:10:36', '2026-09-16 03:13:35'),
(2, '2025-2026', '2025-10-01', '2026-07-31', 0, '2026-09-16 03:11:27', '2026-09-16 03:14:36');

-- --------------------------------------------------------

--
-- Structure de la table `examinations`
--

CREATE TABLE `examinations` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `subject_id` bigint(20) UNSIGNED NOT NULL,
  `examination_type_id` bigint(20) UNSIGNED NOT NULL,
  `exam_date` date DEFAULT NULL,
  `exam_order` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `examinations`
--

INSERT INTO `examinations` (`id`, `subject_id`, `examination_type_id`, `exam_date`, `exam_order`, `created_at`, `updated_at`) VALUES
(1, 1, 2, '2024-11-20', 1, '2026-09-16 03:37:56', '2026-09-16 03:44:46'),
(2, 1, 2, '2024-12-10', 2, '2026-09-16 03:39:24', '2026-09-16 03:39:24');

-- --------------------------------------------------------

--
-- Structure de la table `examination_types`
--

CREATE TABLE `examination_types` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `name` varchar(50) NOT NULL COMMENT 'ex: Contrôle Continu, Examen Final, Devoir Mensuel, Rattrapage',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `examination_types`
--

INSERT INTO `examination_types` (`id`, `name`, `created_at`) VALUES
(2, 'Examen Final', '2026-07-01 17:20:17'),
(3, 'Devoir Mensuel', '2026-07-01 17:20:17'),
(4, 'Rattrapage', '2026-07-01 17:20:17'),
(5, 'Devoir annuel', '2026-09-16 02:57:35');

-- --------------------------------------------------------

--
-- Structure de la table `genders`
--

CREATE TABLE `genders` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `name` varchar(30) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `genders`
--

INSERT INTO `genders` (`id`, `name`, `created_at`) VALUES
(1, 'Masculin', '2026-07-01 17:20:17'),
(2, 'Féminin', '2026-07-01 17:20:17');

-- --------------------------------------------------------

--
-- Structure de la table `grades`
--

CREATE TABLE `grades` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `student_id` bigint(20) UNSIGNED NOT NULL,
  `examination_id` bigint(20) UNSIGNED NOT NULL,
  `score` decimal(5,2) NOT NULL COMMENT 'Note obtenue — doit respecter le barème de la filière',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `program_year_levels`
--

CREATE TABLE `program_year_levels` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `academic_program_id` bigint(20) UNSIGNED NOT NULL,
  `academic_year_id` bigint(20) UNSIGNED NOT NULL,
  `level_number` int(11) NOT NULL,
  `level_label` varchar(50) NOT NULL COMMENT 'ex: Licence 1, Licence 4, 12ème année',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `program_year_levels`
--

INSERT INTO `program_year_levels` (`id`, `academic_program_id`, `academic_year_id`, `level_number`, `level_label`, `created_at`) VALUES
(1, 1, 1, 1, 'L1 - Premiere Annee', '2026-09-16 03:17:09');

-- --------------------------------------------------------

--
-- Structure de la table `report_cards`
--

CREATE TABLE `report_cards` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `student_id` bigint(20) UNSIGNED NOT NULL,
  `report_card_type` enum('SEMESTRE','ANNUEL') NOT NULL DEFAULT 'SEMESTRE',
  `semester_id` bigint(20) UNSIGNED DEFAULT NULL COMMENT 'NULL si type ANNUEL',
  `academic_year_id` bigint(20) UNSIGNED NOT NULL COMMENT 'Année académique concernée',
  `overall_average` decimal(5,2) DEFAULT NULL COMMENT 'Moyenne générale calculée automatiquement',
  `is_validated` tinyint(1) NOT NULL DEFAULT 0,
  `validated_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `report_card_validations`
--

CREATE TABLE `report_card_validations` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `report_card_id` bigint(20) UNSIGNED NOT NULL,
  `validated_by` bigint(20) UNSIGNED NOT NULL COMMENT 'ID du user (ADMIN ou SUPER_ADMIN) qui a validé',
  `is_validated` tinyint(1) NOT NULL DEFAULT 0,
  `validated_at` timestamp NULL DEFAULT NULL,
  `notes` text DEFAULT NULL COMMENT 'Commentaire optionnel de l administrateur',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `students`
--

CREATE TABLE `students` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `user_id` bigint(20) UNSIGNED NOT NULL COMMENT 'Lien vers le compte de connexion',
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL,
  `registration_number` varchar(30) NOT NULL COMMENT 'Matricule unique — remplace le CIN',
  `date_of_birth` date DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `gender_id` bigint(20) UNSIGNED DEFAULT NULL,
  `academic_program_id` bigint(20) UNSIGNED NOT NULL COMMENT 'Filière de l étudiant',
  `academic_year_id` bigint(20) UNSIGNED NOT NULL COMMENT 'Année académique en cours',
  `current_level` tinyint(3) UNSIGNED NOT NULL COMMENT 'Niveau actuel : 1=L1/10ème, 2=L2/11ème...',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `subjects`
--

CREATE TABLE `subjects` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `academic_program_id` bigint(20) UNSIGNED NOT NULL COMMENT 'Lien direct vers la filière',
  `semester_id` bigint(20) UNSIGNED NOT NULL COMMENT 'Lien vers le semestre/module/trimestre précis',
  `name` varchar(150) NOT NULL COMMENT 'ex: Mathématiques, Algorithme, Physique...',
  `coefficient` decimal(4,2) NOT NULL DEFAULT 1.00 COMMENT 'Coefficient pour le calcul de la moyenne pondérée',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `subjects`
--

INSERT INTO `subjects` (`id`, `academic_program_id`, `semester_id`, `name`, `coefficient`, `created_at`, `updated_at`) VALUES
(1, 1, 1, 'Mathematiques Avancees', '3.50', '2026-09-16 03:29:56', '2026-09-16 03:35:26'),
(2, 1, 1, 'Algorithmique', '2.50', '2026-09-16 03:30:36', '2026-09-16 03:30:36');

-- --------------------------------------------------------

--
-- Structure de la table `subject_averages`
--

CREATE TABLE `subject_averages` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `report_card_id` bigint(20) UNSIGNED NOT NULL,
  `subject_id` bigint(20) UNSIGNED NOT NULL,
  `average_score` decimal(5,2) DEFAULT NULL COMMENT 'Moyenne calculée pour cette matière dans ce bulletin',
  `weighted_score` decimal(5,2) DEFAULT NULL COMMENT 'average_score × coefficient',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

CREATE TABLE `users` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL,
  `email` varchar(150) NOT NULL COMMENT 'Email unique — sert au login ET au profil',
  `password` varchar(255) NOT NULL COMMENT 'Toujours hashé avec BCrypt — jamais en clair',
  `role` enum('ADMIN','ETUDIANT','SUPER_ADMIN') NOT NULL,
  `photo_url` varchar(255) DEFAULT NULL COMMENT 'Chemin relatif vers la photo stockée sur le serveur',
  `is_active` tinyint(1) NOT NULL DEFAULT 1 COMMENT 'Désactiver un compte sans le supprimer',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `users`
--

INSERT INTO `users` (`id`, `first_name`, `last_name`, `email`, `password`, `role`, `photo_url`, `is_active`, `created_at`, `updated_at`) VALUES
(1, 'Super', 'Admin', 'superadmin@badiar.com', '$2a$10$A2p8SizJ9eRNFACOaDdwgOTV8BvbNijfu2z9nu3.cfiFA5PTUDx1S', 'SUPER_ADMIN', NULL, 1, '2026-07-03 20:55:12', '2026-07-03 20:55:12');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `academic_programs`
--
ALTER TABLE `academic_programs`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`),
  ADD KEY `idx_academic_programs_name` (`name`),
  ADD KEY `idx_academic_programs_is_active` (`is_active`);

--
-- Index pour la table `academic_semesters`
--
ALTER TABLE `academic_semesters`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_semester_order` (`program_year_level_id`,`display_order`),
  ADD KEY `idx_semesters_pyl` (`program_year_level_id`),
  ADD KEY `idx_semesters_order` (`display_order`);

--
-- Index pour la table `academic_years`
--
ALTER TABLE `academic_years`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`),
  ADD KEY `idx_academic_years_name` (`name`),
  ADD KEY `idx_academic_years_is_active` (`is_active`);

--
-- Index pour la table `examinations`
--
ALTER TABLE `examinations`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_examinations_subject` (`subject_id`),
  ADD KEY `idx_examinations_type` (`examination_type_id`),
  ADD KEY `idx_examinations_date` (`exam_date`);

--
-- Index pour la table `examination_types`
--
ALTER TABLE `examination_types`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`),
  ADD KEY `idx_exam_types_name` (`name`);

--
-- Index pour la table `genders`
--
ALTER TABLE `genders`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`),
  ADD KEY `idx_genders_name` (`name`);

--
-- Index pour la table `grades`
--
ALTER TABLE `grades`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_grade` (`student_id`,`examination_id`),
  ADD KEY `idx_grades_student` (`student_id`),
  ADD KEY `idx_grades_examination` (`examination_id`);

--
-- Index pour la table `program_year_levels`
--
ALTER TABLE `program_year_levels`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_program_year_level` (`academic_program_id`,`academic_year_id`,`level_number`),
  ADD KEY `idx_pyl_program` (`academic_program_id`),
  ADD KEY `idx_pyl_year` (`academic_year_id`),
  ADD KEY `idx_pyl_level` (`level_number`);

--
-- Index pour la table `report_cards`
--
ALTER TABLE `report_cards`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_report_semestre` (`student_id`,`semester_id`),
  ADD KEY `idx_report_cards_student` (`student_id`),
  ADD KEY `idx_report_cards_type` (`report_card_type`),
  ADD KEY `idx_report_cards_semester` (`semester_id`),
  ADD KEY `idx_report_cards_year` (`academic_year_id`),
  ADD KEY `idx_report_cards_validated` (`is_validated`);

--
-- Index pour la table `report_card_validations`
--
ALTER TABLE `report_card_validations`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `report_card_id` (`report_card_id`),
  ADD KEY `idx_rcv_report_card` (`report_card_id`),
  ADD KEY `idx_rcv_validated_by` (`validated_by`),
  ADD KEY `idx_rcv_validated_at` (`validated_at`);

--
-- Index pour la table `students`
--
ALTER TABLE `students`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `user_id` (`user_id`),
  ADD UNIQUE KEY `registration_number` (`registration_number`),
  ADD KEY `idx_students_user` (`user_id`),
  ADD KEY `idx_students_reg_number` (`registration_number`),
  ADD KEY `idx_students_program` (`academic_program_id`),
  ADD KEY `idx_students_year` (`academic_year_id`),
  ADD KEY `idx_students_level` (`current_level`),
  ADD KEY `idx_students_gender` (`gender_id`);

--
-- Index pour la table `subjects`
--
ALTER TABLE `subjects`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_subjects_program` (`academic_program_id`),
  ADD KEY `idx_subjects_semester` (`semester_id`),
  ADD KEY `idx_subjects_name` (`name`),
  ADD KEY `idx_subjects_coeff` (`coefficient`);

--
-- Index pour la table `subject_averages`
--
ALTER TABLE `subject_averages`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_subject_avg` (`report_card_id`,`subject_id`),
  ADD KEY `idx_subject_averages_report` (`report_card_id`),
  ADD KEY `idx_subject_averages_subj` (`subject_id`);

--
-- Index pour la table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD UNIQUE KEY `uq_users_email` (`email`),
  ADD KEY `idx_users_email` (`email`),
  ADD KEY `idx_users_role` (`role`),
  ADD KEY `idx_users_is_active` (`is_active`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `academic_programs`
--
ALTER TABLE `academic_programs`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `academic_semesters`
--
ALTER TABLE `academic_semesters`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `academic_years`
--
ALTER TABLE `academic_years`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `examinations`
--
ALTER TABLE `examinations`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `examination_types`
--
ALTER TABLE `examination_types`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT pour la table `genders`
--
ALTER TABLE `genders`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `grades`
--
ALTER TABLE `grades`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `program_year_levels`
--
ALTER TABLE `program_year_levels`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `report_cards`
--
ALTER TABLE `report_cards`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `report_card_validations`
--
ALTER TABLE `report_card_validations`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `students`
--
ALTER TABLE `students`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `subjects`
--
ALTER TABLE `subjects`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `subject_averages`
--
ALTER TABLE `subject_averages`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `academic_semesters`
--
ALTER TABLE `academic_semesters`
  ADD CONSTRAINT `fk_sem_pyl` FOREIGN KEY (`program_year_level_id`) REFERENCES `program_year_levels` (`id`) ON DELETE CASCADE;

--
-- Contraintes pour la table `examinations`
--
ALTER TABLE `examinations`
  ADD CONSTRAINT `fk_exam_subject` FOREIGN KEY (`subject_id`) REFERENCES `subjects` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_exam_type` FOREIGN KEY (`examination_type_id`) REFERENCES `examination_types` (`id`);

--
-- Contraintes pour la table `grades`
--
ALTER TABLE `grades`
  ADD CONSTRAINT `fk_grade_examination` FOREIGN KEY (`examination_id`) REFERENCES `examinations` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_grade_student` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`) ON DELETE CASCADE;

--
-- Contraintes pour la table `program_year_levels`
--
ALTER TABLE `program_year_levels`
  ADD CONSTRAINT `fk_pyl_program` FOREIGN KEY (`academic_program_id`) REFERENCES `academic_programs` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_pyl_year` FOREIGN KEY (`academic_year_id`) REFERENCES `academic_years` (`id`) ON DELETE CASCADE;

--
-- Contraintes pour la table `report_cards`
--
ALTER TABLE `report_cards`
  ADD CONSTRAINT `fk_rc_semester` FOREIGN KEY (`semester_id`) REFERENCES `academic_semesters` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_rc_student` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_rc_year` FOREIGN KEY (`academic_year_id`) REFERENCES `academic_years` (`id`);

--
-- Contraintes pour la table `report_card_validations`
--
ALTER TABLE `report_card_validations`
  ADD CONSTRAINT `fk_rcv_report_card` FOREIGN KEY (`report_card_id`) REFERENCES `report_cards` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_rcv_validated_by` FOREIGN KEY (`validated_by`) REFERENCES `users` (`id`);

--
-- Contraintes pour la table `students`
--
ALTER TABLE `students`
  ADD CONSTRAINT `fk_student_gender` FOREIGN KEY (`gender_id`) REFERENCES `genders` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `fk_student_program` FOREIGN KEY (`academic_program_id`) REFERENCES `academic_programs` (`id`),
  ADD CONSTRAINT `fk_student_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_student_year` FOREIGN KEY (`academic_year_id`) REFERENCES `academic_years` (`id`);

--
-- Contraintes pour la table `subjects`
--
ALTER TABLE `subjects`
  ADD CONSTRAINT `fk_subj_program` FOREIGN KEY (`academic_program_id`) REFERENCES `academic_programs` (`id`),
  ADD CONSTRAINT `fk_subj_semester` FOREIGN KEY (`semester_id`) REFERENCES `academic_semesters` (`id`) ON DELETE CASCADE;

--
-- Contraintes pour la table `subject_averages`
--
ALTER TABLE `subject_averages`
  ADD CONSTRAINT `fk_sa_report_card` FOREIGN KEY (`report_card_id`) REFERENCES `report_cards` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_sa_subject` FOREIGN KEY (`subject_id`) REFERENCES `subjects` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
