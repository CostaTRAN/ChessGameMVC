package strategies;

import models.Position;

public class RookMoveStrategy extends LinearMoveStrategy {

    public RookMoveStrategy(Position position) {
        super(position, true, false);
    }
}
