package strategies;

import models.Position;

/**
 * La classe RookMoveStrategy représente la stratégie de mouvement d'une tour dans un jeu d'échecs.
 * Elle étend la classe LinearMoveStrategy pour définir les mouvements linéaires (droits) de la tour.
 */
public class RookMoveStrategy extends LinearMoveStrategy {

    /**
     * Constructeur de la classe RookMoveStrategy.
     *
     * @param position la position initiale de la tour.
     */
    public RookMoveStrategy(Position position) {
        super(position, true, false);
    }
}
