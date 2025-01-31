package strategies;

import models.Position;

/**
 * La classe BishopMoveStrategy représente la stratégie de mouvement d'un fou dans un jeu d'échecs.
 * Elle étend la classe LinearMoveStrategy pour définir les mouvements diagonaux du fou.
 */
public class BishopMoveStrategy extends LinearMoveStrategy {

    /**
     * Constructeur de la classe BishopMoveStrategy.
     *
     * @param position la position initiale du fou.
     */
    public BishopMoveStrategy(Position position) {
        super(position, false, true);
    }
}
