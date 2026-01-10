package it.unibo.samplejavafx.mvcexample.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ChessBoardImpl implements ChessBoard {
    private static final int BOARD_HEIGHT = 8;
    private static final int BOARD_WIDTH = 8;

    private final Map<Point2D, Optional<Entity>> cells = new HashMap<>();

    public Optional<Entity> getEntity(final Point2D pos) {
        return this.cells.get(pos);
    }

    public void setEntity(final Point2D pos, final Entity newEntity) {
        cells.put(pos, Optional.of(newEntity));
    }

}
