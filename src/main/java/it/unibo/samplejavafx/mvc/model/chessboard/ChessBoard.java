package it.unibo.samplejavafx.mvc.model.chessboard;

import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

/**
 * Represents the board where the match is played.
 */
@FunctionalInterface
public interface ChessBoard {
    int CHESSBOARD_SIZE = 8;

    boolean isFree(final Point2D pos);
}
