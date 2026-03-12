package it.unibo.samplejavafx.mvc.model.handler;

import java.util.List;
import java.util.Optional;

import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

/**
 * The {@link TurnState} interface expresses the methods that are essential for a TurnState, which is how
 * the {@link TurnHandler} should behave according to the current {@link GameState}.
 */
public interface TurnState {

    /**
     * The behaviour of the TurnHandler during a player's own turn.
     * 
     * @param pos the {@link Point2D} of the clicked cell.
     * @return a List of {@link Point2D} of all possible moves.
     */
    List<Point2D> thinking(Point2D pos);

    /**
     * Updates the values needed by the TurnHandler.
     * 
     * @param promotion the {@link Optional} where we want to save a promoting piece.
     */
    void passOnStats(Optional<Piece> promotion);
}
