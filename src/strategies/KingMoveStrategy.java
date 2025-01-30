package strategies;

import models.Game;
import models.Piece;
import models.PieceType;
import models.Position;

public class KingMoveStrategy implements MoveStrategy {
    private Position position;
    private boolean hasMoved;

    public KingMoveStrategy(Position position, boolean hasMoved) {
        this.position = position;
        this.hasMoved = hasMoved;
    }

    @Override
    public boolean isValidMove(Position newPosition) {
        int rowDiff = Math.abs(newPosition.getRow() - this.position.getRow());
        int colDiff = Math.abs(newPosition.getColumn() - this.position.getColumn());
        
        // Normal move
        if (rowDiff <= 1 && colDiff <= 1) {
            return true;
        }
        
        // Castling
        if (!hasMoved && rowDiff == 0 && colDiff == 2) {
            int rookColumn = (newPosition.getColumn() > this.position.getColumn()) ? 7 : 0;
            Position rookPos = new Position(this.position.getRow(), rookColumn);
            Piece rook = Game.getGameInstance().getBoard().getPiece(rookPos);
            
            if (rook != null && rook.getType() == PieceType.ROOK && !rook.hasMoved()) {
                // Check if path is clear
                int step = (rookColumn == 7) ? 1 : -1;
                for (int col = this.position.getColumn() + step; col != rookColumn; col += step) {
                    if (Game.getGameInstance().getBoard().getPiece(new Position(this.position.getRow(), col)) != null) {
                        return false;
                    }
                }
                // Note: Should also check if king passes through check
                return true;
            }
        }
        return false;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
