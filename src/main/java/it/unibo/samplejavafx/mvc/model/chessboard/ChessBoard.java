package it.unibo.samplejavafx.mvc.model.chessboard;

import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

import java.util.Optional;

import com.google.common.collect.BiMap;

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
    boolean isFree(Point2D pos);

    /**
     * placeholder.
     *
     * @param pos placeholder.
     * @param newEntity placeholder.
     */
    void setEntity(Point2D pos, Entity newEntity);

    /**
     * placeholder.
     *
     * @param pos placeholder.
     */
    void removeEntity(Point2D pos);

    /**
     * placeholder.
     *
     * @param pos placeholder.
     * @return placeholder.
     */
    Optional<Entity> getEntity(Point2D pos);

    /**
     * placeholder.
     *
     * @param entity placeholder.
     * @return placeholder.
     */
    Point2D getPosByEntity(Entity entity);

    /**
     *  placeholder.
     *
     * @param pos placeholder.
     * @return placeholder.
     */
    boolean checkBounds(Point2D pos);

    /**
     *  placeholder.
     *
     * @return placeholder.
     */
    BiMap<Point2D, Entity> getBoard();
}
