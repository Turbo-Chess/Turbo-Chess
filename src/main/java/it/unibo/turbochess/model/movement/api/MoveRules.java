package it.unibo.turbochess.model.movement.api;

import it.unibo.turbochess.model.chessboard.board.api.ChessBoard;
import it.unibo.turbochess.model.entity.impl.PlayerColor;
import it.unibo.turbochess.model.point2d.Point2D;
import java.util.List;

/**
 * A move rule is a rule that determines the direction and the constraints of all the possible moves of a piece.
 */
@FunctionalInterface
public interface MoveRules {
    /**
     * The method that calculates all available cells a piece can move to by filtering the cells calculated
     * by {@link MovementStrategy}.
     *
     * @param start         current position of the piece.
     * @param board         the {@link ChessBoard} of the match.
     * @param playerColor   the color of the player owning the piece.
     * @return              an immutable {@link List} containing all the available positions to move to.
     */
    List<Point2D> getValidMoves(Point2D start, ChessBoard board, PlayerColor playerColor);
}
