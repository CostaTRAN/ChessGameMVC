package strategies;

import models.Game;
import models.Position;

public abstract class LinearMoveStrategy implements MoveStrategy {
    private Position position;
    private boolean straight;
    private boolean diagonal;

    public LinearMoveStrategy(Position position, boolean straight, boolean diagonal) {
        this.position = position;
        this.straight = straight;
        this.diagonal = diagonal;
    }

    @Override
    public boolean isValidMove(Position newPosition) {
        return isLinearMove(newPosition);
    }

    public boolean isLinearMove(Position newPosition) {
        int rowDiff = newPosition.getRow() - this.position.getRow();
        int colDiff = newPosition.getColumn() - this.position.getColumn();
        
        boolean isStraightMove = rowDiff == 0 || colDiff == 0;
        boolean isDiagonalMove = Math.abs(rowDiff) == Math.abs(colDiff);
        
        if (!(this.straight && isStraightMove) && !(this.diagonal && isDiagonalMove)) {
            return false;
        }
        
        // Check path for obstacles
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

    @Override
    public Position getPosition() {
        return this.position;
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;
    }
}
