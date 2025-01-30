package strategies;

import models.Position;

public class QueenMoveStrategy extends LinearMoveStrategy{

    public QueenMoveStrategy(Position position) {
        super(position, true, true);
    }
}
