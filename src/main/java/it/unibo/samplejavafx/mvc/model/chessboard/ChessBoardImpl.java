package it.unibo.samplejavafx.mvc.model.chessboard;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableBiMap;

import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Chessboard implementation of {@link ChessBoard}.
 */
public class ChessBoardImpl implements ChessBoard {
    private static final int CHESSBOARD_SIZE = 8;
    //private static final int BOARD_HEIGHT = 8;
    //private static final int BOARD_WIDTH = 8;

    private final BiMap<Point2D, Entity> cells;
    /**
     * List of observers for board changes.
     */
    private final List<BoardObserver> observers = new LinkedList<>();

    /**
     * placeholder.
     */
    public ChessBoardImpl() {
        this.cells = HashBiMap.create();
    }

    /**
     * Constructs a new chessboard with the given cells.
     *
     * @param cells the cells to initialize the board with.
     */
    public ChessBoardImpl(final BiMap<Point2D, Entity> cells) {
        this.cells = HashBiMap.create(cells);
    }

    /**
     * Returns the entity on the board.
     *
     * @param pos position of the entity.
     * @return the Optional containing the entity (or an optional empty if no entities are present).
     */
    @Override
    public Optional<Entity> getEntity(final Point2D pos) {
        return Optional.ofNullable(cells.get(pos));
    }

    /**
     * placeholder.
     *
     * @param entity placeholder.
     * @return placeholder.
     */
    @Override
    public Point2D getPosByEntity(final Entity entity) {
        return this.cells.inverse().get(entity);
    }

    /**
     * Set the entity associated with the specified position.
     *
     * @param pos position of the entity.
     * @param newEntity the new entity to be associated with the position.
     */
    @Override
    public void setEntity(final Point2D pos, final Entity newEntity) {
        if (cells.containsKey(pos)) {
            removeEntity(pos);
        }
        cells.put(pos, newEntity);
        notifyEntityAdded(pos, newEntity);
    }

    /**
     * placeholder.
     *
     * @param pos placeholder.
     */
    @Override
    public void removeEntity(final Point2D pos) {
        final Entity removed = cells.remove(pos);
        if (removed != null) {
            notifyEntityRemoved(pos, removed);
        }
    }

    /**
     * Adds an observer to the board.
     *
     * @param observer the observer to add.
     */
    @Override
    public void addObserver(final BoardObserver observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer from the board.
     *
     * @param observer the observer to remove.
     */
    @Override
    public void removeObserver(final BoardObserver observer) {
        observers.remove(observer);
    }

    private void notifyEntityAdded(final Point2D pos, final Entity entity) {
        for (final BoardObserver observer : observers) {
            observer.onEntityAdded(pos, entity);
        }
    }

    private void notifyEntityRemoved(final Point2D pos, final Entity entity) {
        for (final BoardObserver observer : observers) {
            observer.onEntityRemoved(pos, entity);
        }
    }

    /**
     * Returns if the cell is free or not.
     * If the cell is free, there will be no entry associated with that key.
     *
     * @param pos the position to check.
     * @return true if the entry does not exist (the cell is free), false otherwise.
     */
    @Override
    public boolean isFree(final Point2D pos) {
        return !this.cells.containsKey(pos);
    }

    /**
     *  placeholder.
     *
     * @param pos placeholder.
     * @return placeholder.
     */
    @Override
    public boolean checkBounds(final Point2D pos) {
        return pos.x() >= 0 && pos.y() >= 0 && pos.x() < CHESSBOARD_SIZE && pos.y() < CHESSBOARD_SIZE;
    }

    /**
     *  placeholder.
     *
     * @return placeholder.
     */
    @Override
    public BiMap<Point2D, Entity> getCells() {
        return ImmutableBiMap.copyOf(this.cells);
    }
}
