package com.badiar.badiar_study.common.constant;

/**
 * Constantes pour la validation des données saisies par l'utilisateur.
 * Utilisées dans les annotations @Size, @Pattern, @NotBlank, etc.
 * des classes Request (DTOs).
 */
public final class ValidationConstants {

    private ValidationConstants() {
        throw new UnsupportedOperationException(
                "ValidationConstants est une classe utilitaire et ne peut pas être instanciée"
        );
    }

    // ── Noms (prénom, nom) ───────────────────────────────────────
    public static final int NAME_MIN_LENGTH = 2;
    public static final int NAME_MAX_LENGTH = 100;
    public static final String NAME_PATTERN = "^[a-zA-ZÀ-ÿ\\s\\-']+$";
    public static final String NAME_PATTERN_MESSAGE =
            "Le nom ne doit contenir que des lettres, espaces, tirets ou apostrophes";

    // ── Email ────────────────────────────────────────────────────
    public static final int EMAIL_MAX_LENGTH = 150;
    public static final String EMAIL_NOT_BLANK_MESSAGE = "L'email est obligatoire";
    public static final String EMAIL_INVALID_MESSAGE = "L'adresse email n'est pas valide";

    // ── Mot de passe ─────────────────────────────────────────────
    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final int    PASSWORD_MAX_LENGTH = 255;
    public static final String PASSWORD_PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&&#])[A-Za-z\\d@$!%*?&&#]{8,}$";
    public static final String PASSWORD_PATTERN_MESSAGE =
            "Le mot de passe doit contenir au moins 8 caractères, une majuscule, " +
                    "une minuscule, un chiffre et un caractère spécial (@$!%*?&&#)";

    // ── Matricule ────────────────────────────────────────────────
    public static final int    REGISTRATION_NUMBER_MIN_LENGTH = 4;
    public static final int    REGISTRATION_NUMBER_MAX_LENGTH = 30;
    public static final String REGISTRATION_NUMBER_PATTERN   = "^[A-Z0-9\\-]+$";
    public static final String REGISTRATION_NUMBER_MESSAGE   =
            "Le matricule ne doit contenir que des majuscules, chiffres et tirets";

    // ── Téléphone ────────────────────────────────────────────────
    public static final int PHONE_MAX_LENGTH = 20;
    public static final String PHONE_PATTERN = "^[+\\d\\s\\-()]{6,20}$";
    public static final String PHONE_PATTERN_MESSAGE = "Le numéro de téléphone n'est pas valide";

    // ── Noms d'entités académiques (filière, matière, etc.) ──────
    public static final int ACADEMIC_NAME_MIN_LENGTH = 2;
    public static final int ACADEMIC_NAME_MAX_LENGTH = 150;

    // ── Description ──────────────────────────────────────────────
    public static final int DESCRIPTION_MAX_LENGTH = 500;

    // ── Adresse ──────────────────────────────────────────────────
    public static final int ADDRESS_MAX_LENGTH = 255;

    // ── Note (score) ─────────────────────────────────────────────
    public static final double SCORE_MIN = 0.0;
    public static final double SCORE_MAX_UNIVERSITY = 10.0;
    public static final double SCORE_MAX_SECONDARY = 20.0;

    // ── Niveau dans le cycle ──────────────────────────────────────
    public static final int LEVEL_MIN = 1;
    public static final int LEVEL_MAX = 10;

    // ── Pagination ───────────────────────────────────────────────
    public static final int DEFAULT_PAGE_NUMBER = 0;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 100;
}
