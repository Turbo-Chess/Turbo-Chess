package it.unibo.samplejavafx.mvc.model.replay;

import it.unibo.samplejavafx.mvc.model.chessboard.BoardObserver;
import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

import java.util.function.Supplier;

/**
 * Observes the board and records events to the game history.
 */
public final class GameHistoryRecorder implements BoardObserver {
    private final Supplier<Integer> turnSupplier;
    private final GameHistory history;

    /**
     * @param turnSupplier supplies the current turn number.
     * @param history the history to record to.
     */
    public GameHistoryRecorder(final Supplier<Integer> turnSupplier, final GameHistory history) {
        this.turnSupplier = turnSupplier;
        this.history = history;
    }

    /**
     * Records a spawn event.
     *
     * @param pos the position.
     * @param entity the entity.
     */
    @Override
    public void onEntityAdded(final Point2D pos, final Entity entity) {
        history.addEvent(new SpawnEvent(turnSupplier.get(), entity, pos));
    }

    /**
     * Records a despawn event.
     *
     * @param pos the position.
     * @param entity the entity.
     */
    @Override
    public void onEntityRemoved(final Point2D pos, final Entity entity) {
        history.addEvent(new DespawnEvent(turnSupplier.get(), entity, pos));
    }

    /**
     * Records a move event.
     *
     * @param from the source position.
     * @param to the target position.
     * @param entity the entity.
     */
    @Override
    public void onEntityMoved(final Point2D from, final Point2D to, final Entity entity) {
        history.addEvent(new MoveEvent(turnSupplier.get(), entity.getName(), from, to));
    }
}
