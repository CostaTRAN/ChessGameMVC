package strategies;

import models.Position;

public interface MoveStrategy {
    public boolean isValidMove(Position newPosition);
    public Position getPosition();
    public void setPosition(Position position);
}
