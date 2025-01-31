package strategies;

import models.Position;

/**
 * La classe QueenMoveStrategy représente la stratégie de mouvement d'une reine dans un jeu d'échecs.
 * Elle étend la classe LinearMoveStrategy pour définir les mouvements linéaires (droits et diagonaux) de la reine.
 */
public class QueenMoveStrategy extends LinearMoveStrategy {

    /**
     * Constructeur de la classe QueenMoveStrategy.
     *
     * @param position la position initiale de la reine.
     */
    public QueenMoveStrategy(Position position) {
        super(position, true, true);
    }
}
