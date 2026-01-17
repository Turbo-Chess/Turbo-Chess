package it.unibo.samplejavafx.mvc.model.chessboard;

import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Chessboard implementation of {@link ChessBoard}.
 */
public class ChessBoardImpl implements ChessBoard {
    private static final int CHESSBOARD_SIZE = 8;
    //private static final int BOARD_HEIGHT = 8;
    //private static final int BOARD_WIDTH = 8;

    private final Map<Point2D, Optional<Entity>> cells;

    public ChessBoardImpl() {
        this.cells = IntStream.range(0, CHESSBOARD_SIZE)
                .boxed()
                .flatMap(x -> IntStream.range(0, CHESSBOARD_SIZE)
                        .mapToObj(y -> new Point2D(x, y)))
                .collect(Collectors.toMap(
                        pos -> pos,
                        pos -> Optional.empty()
                ));
    }

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

    @Override
    public boolean checkBounds(Point2D pos) {
        return pos.x() >= 0 && pos.y() >= 0 && pos.x() < CHESSBOARD_SIZE && pos.y() < CHESSBOARD_SIZE;
    }
}
