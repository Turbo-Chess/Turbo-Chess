package it.unibo.samplejavafx.mvc.model.chessboard;

import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

import java.util.Optional;

/**
 * Represents the board where the match is played.
 */
public interface ChessBoard {

    /**
     *  placeholder.
     *
     * @param pos placeholder.
     * @return placeholder.
     */
    boolean isFree(final Point2D pos);

    /**
     *
     * @param pos placeholder.
     * @param newEntity placeholder.
     */
    void setEntity(final Point2D pos, final Entity newEntity);

    /**
     * placeholder.
     *
     * @param pos placeholder.
     * @return placeholder.
     */
    Optional<Entity> getEntity(final Point2D pos);

    /**
     *  placeholder.
     *
     * @param pos placeholder.
     * @return placeholder.
     */
    boolean checkBounds(Point2D pos);
}
