package it.unibo.samplejavafx.mvc.model.chessboard;

import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

/**
 * The {@code BoardObserver} interface defines a mechanism for objects to listen and react to changes
 * occurring on the {@link ChessBoard}.
 *
 * <p>
 * Implementing classes can receive notifications when entities are added, removed, or moved on the board,
 * allowing for decoupling between the board's data and dependent systems such as the UI or game logic.
 * </p>
 */
public interface BoardObserver {
    /**
     * Triggered when a new entity is placed on the board.
     *
     * @param pos    The {@link Point2D} coordinate where the entity serves as the destination.
     * @param entity The {@link Entity} that has been added to the board.
     */
    void onEntityAdded(Point2D pos, Entity entity);

    /**
     * Triggered when an entity is removed from the board.
     *
     * @param pos    The {@link Point2D} coordinate from which the entity was removed.
     * @param entity The {@link Entity} that was removed.
     */
    void onEntityRemoved(Point2D pos, Entity entity);

    // TODO: this two methods maybe can be merged into one

    /**
     * placeholder.
     *
     * @param from placeholder.
     * @param to placeholder.
     */
    default void onEntityMoved(final Point2D from, final Point2D to) {
    }

    /**
     * placeholder.
     *
     * @param from placeholder.
     * @param to placeholder.
     * @param entity placeholder.
     */
    void onEntityMoved(Point2D from, Point2D to, Entity entity);

}
