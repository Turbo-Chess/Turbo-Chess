package it.unibo.samplejavafx.mvc.model.movement;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

import java.util.List;

/**
 * The movement strategy represents the way a piece can move.
 * It can be implemented in various ways, though the main will be {@link JumpingMovement} and {@link SlidingMovement}.
 */
public interface MovementStrategy {
    /**
     * The method that calculates all available cells a piece can move to.
     * It will be then filtered by {@link MoveRules}.
     *
     * @param start         actual point of the piece.
     * @param direction     a {@link Point2D} representing the direction vector of the movement.
     * @param board         the {@link ChessBoard} of the match.
     * @return              an immutable {@link List} containing all the available positions to move to.
     */
    List<Point2D> calculateMoves(Point2D start, Point2D direction, ChessBoard board);
}
