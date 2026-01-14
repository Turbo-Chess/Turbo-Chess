package it.unibo.samplejavafx.mvc.model.chessboard;

import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

/**
 * Represents the board where the match is played.
 */
public interface ChessBoard {
    boolean isFree(final Point2D pos);

    boolean checkBounds(Point2D pos);
}
