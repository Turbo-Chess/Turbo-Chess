package it.unibo.samplejavafx.mvc.model.handler.turnstates;

import java.util.List;
import java.util.Optional;

import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.handler.TurnHandlerContext;
import it.unibo.samplejavafx.mvc.model.handler.TurnState;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

/**
 * Abstract implementation of {@link TurnState}, adding the building blocks that all TurnStates must have.
 */
public abstract class AbstractTurnState implements TurnState {
    protected final TurnHandlerContext context;

    /**
     * Constructor for the TurnState
     * 
     * @param turnHandler the TurnHandler of the match, taken as a {@link TurnHandlerContext}.
     */
    public AbstractTurnState(final TurnHandlerContext turnHandler) {
        this.context = turnHandler;
    }

    /**
     * {@inheritDoc}
     */
    public abstract List<Point2D> thinking(final Point2D pos);

    /**
     * {@inheritDoc}
     */
    public abstract void passOnStats(Optional<Piece> promotion);
}
