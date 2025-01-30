package strategies;

import models.Position;

public class KnightMoveStrategy implements MoveStrategy {
    private Position position;

    public KnightMoveStrategy(Position position) {
        this.position = position;
    }

    @Override
    public boolean isValidMove(Position newPosition) {
        int rowDiff = Math.abs(newPosition.getRow() - this.position.getRow());
        int colDiff = Math.abs(newPosition.getColumn() - this.position.getColumn());
        return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
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
