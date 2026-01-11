package it.unibo.samplejavafx.mvc.model.Movement;

import it.unibo.samplejavafx.mvc.model.ChessBoard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.Point2D.Point2D;

import java.util.List;

/**
 * Represent the continuous movement of a piece.
 * A sliding movement is when a piece can move in a direction until it finds an obstacle
 * (another piece or the board limit).
 */
public class SlidingMovement implements MovementStrategy {
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
