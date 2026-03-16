package it.unibo.samplejavafx.mvc.model.handler.turnstates;

import java.util.List;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.samplejavafx.mvc.model.handler.TurnHandlerContext;
import it.unibo.samplejavafx.mvc.model.handler.TurnState;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

/**
 * Abstract implementation of {@link TurnState}, adding the building blocks that all TurnStates must have.
 */
public abstract class AbstractTurnState implements TurnState {
    private final TurnHandlerContext context;

    /**
     * Constructor for the TurnState.
     * 
     * @param turnHandler the TurnHandler of the match, taken as a {@link TurnHandlerContext}.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "this field is part of the State Pattern structure")
    public AbstractTurnState(final TurnHandlerContext turnHandler) {
        this.context = turnHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract List<Point2D> thinking(Point2D pos);

    /**
     * Getter for the current TurnHandlerContext.
     * 
     * @return the {@link TurnHandlerContext}.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "this field is part of the State Pattern structure")
    public TurnHandlerContext getContext() {
        return context;
    }
}
