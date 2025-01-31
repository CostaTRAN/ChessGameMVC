package strategies;

import models.Game;
import models.Position;

/**
 * La classe abstraite LinearMoveStrategy représente une stratégie de mouvement linéaire pour les pièces d'échecs.
 * Elle implémente l'interface MoveStrategy et définit les mouvements linéaires (droits et diagonaux) pour les pièces.
 */
public abstract class LinearMoveStrategy implements MoveStrategy {
    private Position position;
    private boolean straight;
    private boolean diagonal;

    /**
     * Constructeur de la classe LinearMoveStrategy.
     *
     * @param position la position initiale de la pièce.
     * @param straight indique si la pièce peut se déplacer en ligne droite.
     * @param diagonal indique si la pièce peut se déplacer en diagonale.
     */
    public LinearMoveStrategy(Position position, boolean straight, boolean diagonal) {
        this.position = position;
        this.straight = straight;
        this.diagonal = diagonal;
    }

    /**
     * Vérifie si un mouvement vers une nouvelle position est valide pour la pièce.
     *
     * @param newPosition la nouvelle position à vérifier.
     * @return true si le mouvement est valide, false sinon.
     */
    @Override
    public boolean isValidMove(Position newPosition) {
        return isLinearMove(newPosition);
    }

    /**
     * Vérifie si un mouvement vers une nouvelle position est un mouvement linéaire valide.
     *
     * @param newPosition la nouvelle position à vérifier.
     * @return true si le mouvement est un mouvement linéaire valide, false sinon.
     */
    public boolean isLinearMove(Position newPosition) {
        int rowDiff = newPosition.getRow() - this.position.getRow();
        int colDiff = newPosition.getColumn() - this.position.getColumn();

        boolean isStraightMove = rowDiff == 0 || colDiff == 0;
        boolean isDiagonalMove = Math.abs(rowDiff) == Math.abs(colDiff);

        if (!(this.straight && isStraightMove) && !(this.diagonal && isDiagonalMove)) {
            return false;
        }

        // Vérifie le chemin pour les obstacles
        int rowStep = Integer.compare(rowDiff, 0);
        int colStep = Integer.compare(colDiff, 0);

        Position current = new Position(this.position.getRow() + rowStep, this.position.getColumn() + colStep);
        while (!current.equals(newPosition)) {
            if (Game.getGameInstance().getBoard().getPiece(current) != null) {
                return false;
            }
            current = new Position(current.getRow() + rowStep, current.getColumn() + colStep);
        }
        return true;
    }

    /**
     * Retourne la position actuelle de la pièce.
     *
     * @return la position actuelle de la pièce.
     */
    @Override
    public Position getPosition() {
        return this.position;
    }

    /**
     * Définit la position de la pièce.
     *
     * @param position la nouvelle position de la pièce.
     */
    @Override
    public void setPosition(Position position) {
        this.position = position;
    }
}
