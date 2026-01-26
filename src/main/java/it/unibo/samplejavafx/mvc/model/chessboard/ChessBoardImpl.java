package it.unibo.samplejavafx.mvc.model.chessboard;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

import java.util.List;
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

    private final BiMap<Point2D, Optional<Entity>> cells;

    /**
     * placeholder.
     */
    public ChessBoardImpl() {
        this.cells = IntStream.range(0, CHESSBOARD_SIZE)
                .boxed()
                .flatMap(x -> IntStream.range(0, CHESSBOARD_SIZE)
                        .mapToObj(y -> new Point2D(x, y)))
                .collect(ImmutableBiMap.toImmutableBiMap(
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
    @Override
    public Optional<Entity> getEntity(final Point2D pos) {
        return this.cells.get(pos);
    }

    /**
     * Set the entity associated with the specified position.
     *
     * @param pos position of the entity.
     * @param newEntity the new entity to be associated with the position.
     */
    @Override
    public void setEntity(final Point2D pos, final Entity newEntity) {
        cells.put(pos, Optional.of(newEntity));
    }

    /**
     *  placeholder.
     *
     * @param pos placeholder.
     * @return placeholder.
     */
    @Override
    public boolean isFree(final Point2D pos) {
        return this.cells.get(pos).isEmpty();
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
    public List<Point2D> getCells() {
        return cells.keySet().stream().toList();
    }
}
