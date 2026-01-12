package it.unibo.samplejavafx.mvc.model.chessboard;

import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Chessboard implementation of {@link ChessBoard}.
 */
public class ChessBoardImpl implements ChessBoard {
    //private static final int BOARD_HEIGHT = 8;
    //private static final int BOARD_WIDTH = 8;

    private final Map<Point2D, Optional<Entity>> cells = new HashMap<>();

    /**
     * Returns the entity on the board.
     *
     * @param pos position of the entity.
     * @return the optional containing the entity (or an optional empty if no entities are present).
     */
    public Optional<Entity> getEntity(final Point2D pos) {
        return this.cells.get(pos);
    }

    /**
     * Set the entity associated with the specified position.
     *
     * @param pos position of the entity.
     * @param newEntity the new entity to be associated with the position.
     */
    public void setEntity(final Point2D pos, final Entity newEntity) {
        cells.put(pos, Optional.of(newEntity));
    }

    public boolean isFree(final Point2D pos) {
        return this.cells.get(pos).isEmpty();
    }

}
