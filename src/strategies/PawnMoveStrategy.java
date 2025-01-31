package strategies;

import models.Color;
import models.Game;
import models.Piece;
import models.Position;

/**
 * La classe PawnMoveStrategy représente la stratégie de mouvement d'un pion dans un jeu d'échecs.
 * Elle implémente l'interface MoveStrategy pour définir les mouvements spécifiques du pion, y compris les mouvements en avant et les captures.
 */
public class PawnMoveStrategy implements MoveStrategy {
    private Color color;
    private Position position;
    private boolean hasMoved;

    /**
     * Constructeur de la classe PawnMoveStrategy.
     *
     * @param color la couleur du pion.
     * @param position la position initiale du pion.
     * @param hasMoved indique si le pion a déjà été déplacé.
     */
    public PawnMoveStrategy(Color color, Position position, boolean hasMoved) {
        this.color = color;
        this.position = position;
        this.hasMoved = hasMoved;
    }

    /**
     * Vérifie si un mouvement vers une nouvelle position est valide pour le pion.
     *
     * @param newPosition la nouvelle position à vérifier.
     * @return true si le mouvement est valide, false sinon.
     */
    @Override
    public boolean isValidMove(Position newPosition) {
        int direction = (this.color == Color.WHITE) ? 1 : -1;
        int rowDiff = newPosition.getRow() - this.position.getRow();
        int colDiff = Math.abs(newPosition.getColumn() - this.position.getColumn());

        // Mouvement en avant
        if (colDiff == 0) {
            // Avancer d'une case
            if (rowDiff == direction && Game.getGameInstance().getBoard().getPiece(newPosition) == null) {
                return true;
            }
            // Avancer de deux cases initialement
            if (!hasMoved && rowDiff == 2 * direction) {
                Position intermediate = new Position(this.position.getRow() + direction, this.position.getColumn());
                return Game.getGameInstance().getBoard().getPiece(intermediate) == null && Game.getGameInstance().getBoard().getPiece(newPosition) == null;
            }
        }
        // Capture
        else if (colDiff == 1 && rowDiff == direction) {
            Piece targetPiece = Game.getGameInstance().getBoard().getPiece(newPosition);
            return targetPiece != null && targetPiece.getColor() != color;
            // Note: La prise en passant pourrait être implémentée ici
        }
        return false;
    }

    /**
     * Retourne la position actuelle du pion.
     *
     * @return la position actuelle du pion.
     */
    @Override
    public Position getPosition() {
        return position;
    }

    /**
     * Définit la position du pion.
     *
     * @param position la nouvelle position du pion.
     */
    @Override
    public void setPosition(Position position) {
        this.position = position;
    }
}
