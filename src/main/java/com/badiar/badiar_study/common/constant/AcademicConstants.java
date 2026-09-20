package com.badiar.badiar_study.common.constant;


public final class AcademicConstants {

    private AcademicConstants() {
        throw new UnsupportedOperationException(
                "AcademicConstants est une classe utilitaire et ne peut pas être instanciée"
        );
    }

    // ── Barèmes autorisés ────────────────────────────────────────
    public static final int GRADING_SCALE_UNIVERSITY = 10; // Université
    public static final int GRADING_SCALE_SECONDARY  = 20; // Lycée / Collège

    // ── Seuils de mentions pour barème /20 (lycée/collège) ───────
    public static final double MENTION_TRES_BIEN_MIN_20  = 16.0;
    public static final double MENTION_BIEN_MIN_20       = 14.0;
    public static final double MENTION_ASSEZ_BIEN_MIN_20 = 12.0;
    public static final double MENTION_PASSABLE_MIN_20   = 10.0;

    // ── Seuils de mentions pour barème /10 (université) ──────────
    public static final double MENTION_TRES_BIEN_MIN_10  = 8.0;
    public static final double MENTION_BIEN_MIN_10       = 7.0;
    public static final double MENTION_ASSEZ_BIEN_MIN_10 = 6.0;
    public static final double MENTION_PASSABLE_MIN_10   = 5.0;

    // ── Libellés des mentions ────────────────────────────────────
    public static final String MENTION_TRES_BIEN  = "Très Bien";
    public static final String MENTION_BIEN       = "Bien";
    public static final String MENTION_ASSEZ_BIEN = "Assez Bien";
    public static final String MENTION_PASSABLE   = "Passable";
    public static final String MENTION_INSUFFISANT = "Insuffisant";

    // ── Nombre de notes par matière par semestre ─────────────────
    public static final int NOTES_PAR_MATIERE_UNIVERSITE = 3; // (note1, note2, note3)
    // Pour le lycée : 1 note par mois par matière dans le trimestre

    // ── Coefficient minimum et maximum autorisé ───────────────────
    public static final double COEFFICIENT_MIN = 0.5;
    public static final double COEFFICIENT_MAX = 10.0;

    // ── Types de bulletins ───────────────────────────────────────
    public static final String BULLETIN_TYPE_SEMESTRE = "SEMESTRE";
    public static final String BULLETIN_TYPE_ANNUEL   = "ANNUEL";
}
