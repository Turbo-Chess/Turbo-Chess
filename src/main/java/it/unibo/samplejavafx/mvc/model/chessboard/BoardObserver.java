package it.unibo.samplejavafx.mvc.model.chessboard;

import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

/**
 * Observer interface for monitoring changes on the ChessBoard.
 */
public interface BoardObserver {
    /**
     * Called when an entity is added to the board.
     *
     * @param pos the position where the entity was added.
     * @param entity the entity added.
     */
    void onEntityAdded(Point2D pos, Entity entity);

    /**
     * Called when an entity is removed from the board.
     *
     * @param pos the position where the entity was removed.
     * @param entity the entity removed.
     */
    void onEntityRemoved(Point2D pos, Entity entity);
}
