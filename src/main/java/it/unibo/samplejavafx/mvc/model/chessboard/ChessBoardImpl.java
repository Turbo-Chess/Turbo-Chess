package it.unibo.samplejavafx.mvc.model.chessboard;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;

import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

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
     * placeholder.
     */
    public ChessBoardImpl() {
        this.cells = HashBiMap.create();
    }

    public ChessBoardImpl(final BiMap<Point2D, Entity> cells) {
        this.cells = HashBiMap.create(cells);
    }

    /**
     * Returns the entity on the board.
     *
     * @param pos position of the entity.
     * @return the optional containing the entity (or an optional empty if no entities are present).
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
        cells.put(pos, newEntity);
    }

    /**
     * placeholder.
     *
     * @param pos placeholder.
     */
    @Override
    public void removeEntity(final Point2D pos) {
        cells.remove(pos);
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
    public BiMap<Point2D, Entity> getBoard() {
        return Maps.unmodifiableBiMap(cells);
    }
}
