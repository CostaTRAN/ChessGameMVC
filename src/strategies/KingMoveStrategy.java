package strategies;

import models.Game;
import models.Piece;
import models.PieceType;
import models.Position;

/**
 * La classe KingMoveStrategy représente la stratégie de mouvement d'un roi dans un jeu d'échecs.
 * Elle implémente l'interface MoveStrategy pour définir les mouvements spécifiques du roi, y compris le roque.
 */
public class KingMoveStrategy implements MoveStrategy {
    private Position position;
    private boolean hasMoved;

    /**
     * Constructeur de la classe KingMoveStrategy.
     *
     * @param position la position initiale du roi.
     * @param hasMoved indique si le roi a déjà été déplacé.
     */
    public KingMoveStrategy(Position position, boolean hasMoved) {
        this.position = position;
        this.hasMoved = hasMoved;
    }

    /**
     * Vérifie si un mouvement vers une nouvelle position est valide pour le roi.
     *
     * @param newPosition la nouvelle position à vérifier.
     * @return true si le mouvement est valide, false sinon.
     */
    @Override
    public boolean isValidMove(Position newPosition) {
        int rowDiff = Math.abs(newPosition.getRow() - this.position.getRow());
        int colDiff = Math.abs(newPosition.getColumn() - this.position.getColumn());

        // Mouvement normal
        if (rowDiff <= 1 && colDiff <= 1) {
            return true;
        }

        // Roque
        if (!hasMoved && rowDiff == 0 && colDiff == 2) {
            int rookColumn = (newPosition.getColumn() > this.position.getColumn()) ? 7 : 0;
            Position rookPos = new Position(this.position.getRow(), rookColumn);
            Piece rook = Game.getGameInstance().getBoard().getPiece(rookPos);

            if (rook != null && rook.getType() == PieceType.ROOK && !rook.hasMoved()) {
                // Vérifie si le chemin est libre
                int step = (rookColumn == 7) ? 1 : -1;
                for (int col = this.position.getColumn() + step; col != rookColumn; col += step) {
                    if (Game.getGameInstance().getBoard().getPiece(new Position(this.position.getRow(), col)) != null) {
                        return false;
                    }
                }
                // Note: Il faudrait également vérifier si le roi passe par une case en échec
                return true;
            }
        }
        return false;
    }

    /**
     * Retourne la position actuelle du roi.
     *
     * @return la position actuelle du roi.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Définit la position du roi.
     *
     * @param position la nouvelle position du roi.
     */
    public void setPosition(Position position) {
        this.position = position;
    }
}
