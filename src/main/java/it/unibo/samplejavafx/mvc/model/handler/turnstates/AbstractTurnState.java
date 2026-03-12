package it.unibo.samplejavafx.mvc.model.handler.turnstates;

import java.util.List;
import java.util.Optional;

import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.handler.TurnHandlerContext;
import it.unibo.samplejavafx.mvc.model.handler.TurnState;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

public abstract class AbstractTurnState implements TurnState {
    protected final TurnHandlerContext context;

    public AbstractTurnState(final TurnHandlerContext turnHandler) {
        this.context = turnHandler;
    }

    public abstract List<Point2D> thinking(final Point2D pos);

    public abstract void passOnStats(Optional<Piece> promotion);
}
