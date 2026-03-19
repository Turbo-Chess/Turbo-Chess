package it.unibo.turbochess.model.movement.impl;

import it.unibo.turbochess.model.chessboard.board.api.ChessBoard;
import it.unibo.turbochess.model.movement.api.MoveRules;
import it.unibo.turbochess.model.movement.api.MovementStrategy;
import it.unibo.turbochess.model.point2d.Point2D;

import java.util.ArrayList;
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
     * @param start         current position of the piece.
     * @param direction     a {@link Point2D} representing the direction vector of the movement.
     * @param board         the {@link ChessBoard} of the match.
     * @return              an immutable {@link List} containing all the available positions to move to.
     */
    @Override
    public List<Point2D> calculateMoves(final Point2D start, final Point2D direction, final ChessBoard board) {
        final List<Point2D> resultPoints = new ArrayList<>();
        Point2D newPos = start.sum(direction);
        while (board.checkBounds(newPos)) {
            resultPoints.add(newPos);
            if (!board.isFree(newPos)) {
                return resultPoints;
            } else {
                newPos = newPos.sum(direction);
            }
        }
        return resultPoints;
    }
}
