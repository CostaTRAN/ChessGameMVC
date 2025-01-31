package strategies;

import models.Position;

/**
 * La classe KnightMoveStrategy représente la stratégie de mouvement d'un cavalier dans un jeu d'échecs.
 * Elle implémente l'interface MoveStrategy pour définir les mouvements spécifiques du cavalier.
 */
public class KnightMoveStrategy implements MoveStrategy {
    private Position position;

    /**
     * Constructeur de la classe KnightMoveStrategy.
     *
     * @param position la position initiale du cavalier.
     */
    public KnightMoveStrategy(Position position) {
        this.position = position;
    }

    /**
     * Vérifie si un mouvement vers une nouvelle position est valide pour le cavalier.
     *
     * @param newPosition la nouvelle position à vérifier.
     * @return true si le mouvement est valide, false sinon.
     */
    @Override
    public boolean isValidMove(Position newPosition) {
        int rowDiff = Math.abs(newPosition.getRow() - this.position.getRow());
        int colDiff = Math.abs(newPosition.getColumn() - this.position.getColumn());
        return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
    }

    /**
     * Retourne la position actuelle du cavalier.
     *
     * @return la position actuelle du cavalier.
     */
    @Override
    public Position getPosition() {
        return this.position;
    }

    /**
     * Définit la position du cavalier.
     *
     * @param position la nouvelle position du cavalier.
     */
    @Override
    public void setPosition(Position position) {
        this.position = position;
    }
}
