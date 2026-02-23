package it.unibo.samplejavafx.mvc.model.entity;

import java.util.List;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

/**
 * Placeholder.
 */
public interface Moveable {

    /**
     * Calculates and returns the possible coordinates for the piece to move.
     *
     * @param board  placeholder
     * @param start  placeholder
     * @return a non-null unmodifiable {@link List} of {@link Point2D} coordinates. It could be empty if no moves are possible.
     */
    List<Point2D> getValidMoves(Point2D start, ChessBoard board);

    /**
     * placeholder.
     *
     * @return placeholder.
     */
    List<Point2D> getAvailableCells();

    /**
     * Placeholder.
     */
    void setHasMoved();
}
