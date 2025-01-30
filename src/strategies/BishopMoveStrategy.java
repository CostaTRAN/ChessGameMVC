package strategies;

import models.Position;

public class BishopMoveStrategy extends LinearMoveStrategy {

    public BishopMoveStrategy(Position position)
    {
        super(position, false, true);
    }
}
