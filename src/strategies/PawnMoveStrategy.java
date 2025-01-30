package strategies;

import models.Color;
import models.Game;
import models.Piece;
import models.Position;

public class PawnMoveStrategy implements MoveStrategy {
    private Color color;
    private Position position;
    private boolean hasMoved;

    public PawnMoveStrategy(Color color, Position position, boolean hasMoved) {
        this.color = color;
        this.position = position;
        this.hasMoved = hasMoved;
    }

    @Override
    public boolean isValidMove(Position newPosition) {
        int direction = (this.color == Color.WHITE) ? 1 : -1;
        int rowDiff = newPosition.getRow() - this.position.getRow();
        int colDiff = Math.abs(newPosition.getColumn() - this.position.getColumn());
        
        // Forward movement
        if (colDiff == 0) {
            // Single square forward
            if (rowDiff == direction && Game.getGameInstance().getBoard().getPiece(newPosition) == null) {
                return true;
            }
            // Initial two square move
            if (!hasMoved && rowDiff == 2 * direction) {
                Position intermediate = new Position(this.position.getRow() + direction, this.position.getColumn());
                return Game.getGameInstance().getBoard().getPiece(intermediate) == null && Game.getGameInstance().getBoard().getPiece(newPosition) == null;
            }
        }
        // Capture
        else if (colDiff == 1 && rowDiff == direction) {
            Piece targetPiece = Game.getGameInstance().getBoard().getPiece(newPosition);
            return targetPiece != null && targetPiece.getColor() != color;
            // Note: En passant could be implemented here
        }
        return false;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;
    }
}
