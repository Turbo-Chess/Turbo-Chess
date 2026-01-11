package it.unibo.samplejavafx.mvc.model.movement;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import java.util.List;

/**
 * A move rule is a rule that determines the direction and the constraints of all the possible moves of a piece.
 */
@FunctionalInterface
public interface MoveRules {
    /**
     * The method that calculates all available cells a piece can move to.
     * It will be then filtered by {@link MoveRules}.
     *
     * @param start         actual point of the piece.
     * @param board         the {@link ChessBoard} of the match.
     * @param isWhite       {@code true} if the piece belongs to the white player.
     * @return              an immutable {@link List} containing all the available positions to move to.
     */
    List<Point2D> getValidMoves(Point2D start, ChessBoard board, boolean isWhite);
}
