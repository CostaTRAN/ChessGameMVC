package models;

/**
 * L'énumération GameStatus représente les différents états possibles d'une partie d'échecs.
 */
public enum GameStatus {
    /**
     * La partie est en cours.
     */
    ACTIVE,

    /**
     * La partie est inactive.
     */
    INACTIVE,

    /**
     * Un roi est en échec.
     */
    CHECK,

    /**
     * Un roi est en échec et mat.
     */
    CHECKMATE,

    /**
     * La partie est en pat (aucun mouvement légal possible et pas en échec).
     */
    STALEMATE
}