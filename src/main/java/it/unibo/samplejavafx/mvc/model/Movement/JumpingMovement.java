package it.unibo.samplejavafx.mvc.model.Movement;

import it.unibo.samplejavafx.mvc.model.ChessBoard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.Point2D.Point2D;

import java.util.List;

/**
 * Represent the movement made of "steps" of a piece.
 * A jumping movement is when a piece can move of only a step and jump over other pieces (like the horse).
 */
public class JumpingMovement implements MovementStrategy {
    /**
     * Calculate all the position in which the piece could move.
     * These will be filtrated by the {@link MoveRules} class.
     *
     * @param start         actual point of the piece.
     * @param direction     a {@link Point2D} representing the direction vector of the movement.
     * @param board         the {@link ChessBoard} of the match.
     * @return              an immutable {@link List} containing all the available positions to move to.
     */
    @Override
    public List<Point2D> calculateMoves(final Point2D start, final Point2D direction, final ChessBoard board) {
        // TO-DO: implement method
        return List.of();
    }
}
