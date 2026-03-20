package it.unibo.turbochess.model.chessboard.board.api;

import it.unibo.turbochess.model.entity.api.Entity;
import it.unibo.turbochess.model.point2d.Point2D;

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

    /**
     * Triggered when an entity moves from one position to another on the board.
     *
     * @param from The starting {@link Point2D} coordinate.
     * @param to   The destination {@link Point2D} coordinate.
     */
    default void onEntityMoved(final Point2D from, final Point2D to) { }

    /**
     * Triggered when a specific entity moves from one position to another on the board.
     * Use this if the entity instance itself is needed by the observer.
     *
     * @param from   The starting {@link Point2D} coordinate.
     * @param to     The destination {@link Point2D} coordinate.
     * @param entity The {@link Entity} that has moved.
     */
    default void onEntityMoved(final Point2D from, final Point2D to, final Entity entity) {
        onEntityMoved(from, to);
    }

    /**
     * Triggered when an entity eats another entity on the board.
     *
     * @param from The starting {@link Point2D} coordinate.
     * @param to   The destination {@link Point2D} coordinate.
     */
    default void onEntityEat(final Point2D from, final Point2D to) {
        onEntityMoved(from, to);
    }
}
