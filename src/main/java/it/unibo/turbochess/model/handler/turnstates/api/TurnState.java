package it.unibo.turbochess.model.handler.turnstates.api;

import java.util.List;

import it.unibo.turbochess.model.handler.impl.GameState;
import it.unibo.turbochess.model.handler.api.TurnHandler;
import it.unibo.turbochess.model.point2d.Point2D;

/**
 * The {@link TurnState} interface expresses the methods that are essential for a TurnState, which is how
 * the {@link TurnHandler} should behave according to the current {@link GameState}.
 */
@SuppressWarnings("PMD.ImplicitFunctionalInterface") // interface isn't meant to be used as a functional interface
public interface TurnState {

    /**
     * The behavior of the TurnHandler during a player's own turn.
     * 
     * @param pos the {@link Point2D} of the clicked cell.
     * @return a List of {@link Point2D} of all possible moves.
     */
    List<Point2D> thinking(Point2D pos);
}
