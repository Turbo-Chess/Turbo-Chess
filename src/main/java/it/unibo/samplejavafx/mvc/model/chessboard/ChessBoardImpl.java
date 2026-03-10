package it.unibo.samplejavafx.mvc.model.chessboard;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableBiMap;

import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A concrete implementation of the {@link ChessBoard} interface.
 *
 * <p>
 * This class uses a bidirectional map ({@link BiMap}) to efficiently manage the spatial relationships
 * between positions and entities. It handles the core logic for entity placement, removal, and movement,
 * and notifies registered observers of any state changes.
 * </p>
 */
public class ChessBoardImpl implements ChessBoard {
    private static final int CHESSBOARD_SIZE = 8;
    //private static final int BOARD_HEIGHT = 8;
    //private static final int BOARD_WIDTH = 8;

    private final BiMap<Point2D, Entity> cells;
    /**
     * The list of registered observers monitoring the board state.
     */
    private final List<BoardObserver> observers = new LinkedList<>();

    /**
     * Constructs a new empty chessboard.
     *
     * <p>
     * Initializes the internal data structure for tracking entity positions.
     * </p>
     */
    public ChessBoardImpl() {
        this.cells = HashBiMap.create();
    }

    /**
     * Constructs a new chessboard initialized with a predefined set of entities.
     *
     * <p>
     * This constructor is useful for loading saved games or setting up specific board configurations.
     * </p>
     *
     * @param cells A {@link BiMap} containing the initial distribution of entities on the board.
     */
    public ChessBoardImpl(final BiMap<Point2D, Entity> cells) {
        this.cells = HashBiMap.create(cells);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Uses the underlying map to retrieve the entity.
     * </p>
     */
    @Override
    public Optional<Entity> getEntity(final Point2D pos) {
        return Optional.ofNullable(cells.get(pos));
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Leverages the bidirectional nature of the map to perform a reverse lookup.
     * </p>
     */
    @Override
    public Point2D getPosByEntity(final Entity entity) {
        return this.cells.inverse().get(entity);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Updates the board state. If the position is already occupied, the existing entity is removed first to ensure
     * data consistency, and the appropriate notifications are sent to observers.
     * </p>
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
     * {@inheritDoc}
     *
     * <p>
     * Removes the entity at the specified position and notifies observers of the removal.
     * </p>
     */
    @Override
    public void removeEntity(final Point2D pos) {
        final Entity removed = cells.remove(pos);
        if (removed != null) {
            notifyEntityRemoved(pos, removed);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addObserver(final BoardObserver observer) {
        observers.add(observer);
    }

    /**
     * {@inheritDoc}
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

    private void notifyEntityMoved(final Point2D from, final Point2D to) {
        for (final BoardObserver observer : observers) {
            observer.onEntityMoved(from, to);
        }
    }

    private void notifyEntityMoved(final Point2D from, final Point2D to, final Entity entity) {
        for (final BoardObserver observer : observers) {
            observer.onEntityMoved(from, to, entity);
        }
    }



    /**
     * {@inheritDoc}
     *
     * <p>
     * Returns {@code true} if the internal map does not contain the specified position key.
     * </p>
     */
    @Override
    public boolean isFree(final Point2D pos) {
        return !this.cells.containsKey(pos);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Validates that the coordinates are within the standard 8x8 chessboard range (0-7).
     * </p>
     */
    @Override
    public boolean checkBounds(final Point2D pos) {
        return pos.x() >= 0 && pos.y() >= 0 && pos.x() < CHESSBOARD_SIZE && pos.y() < CHESSBOARD_SIZE;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Returns an immutable copy of the current board state to prevent external modification
     * of the internal data structure.
     * </p>
     */
    @Override
    public BiMap<Point2D, Entity> getBoard() {
        return ImmutableBiMap.copyOf(this.cells);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BiMap<Point2D, Entity> copyCells() {
        final Map<Point2D, Entity> newMap = new HashMap<>();
        for (final Point2D pos : this.cells.keySet()) {
            newMap.put(pos, this.cells.get(pos).cloneEntity());
        }
        return HashBiMap.create(newMap);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Moves an entity from the start position to the end position.
     * Updates the entity's moved state if it is {@link it.unibo.samplejavafx.mvc.model.entity.Moveable}.
     * Triggers move notifications but does not handle capture logic suitable for situations where the target is empty.
     * </p>
     */
    @Override
    public void move(final Point2D start, final Point2D end) {
        final Entity temp = this.getEntity(start).get();
        if (temp.asMoveable().isPresent()) {
            temp.asMoveable().get().setHasMoved();
        }
        this.removeEntity(start);
        this.setEntity(end, temp);
        notifyEntityMoved(start, end, temp);
        notifyEntityMoved(start, end);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Executes a capture maneuver. Explicitly removes the entity at the destination before moving
     * the acting piece. Updates movement state and triggers move notifications.
     * </p>
     */
    @Override
    public void eat(final Point2D start, final Point2D end) {
        final Entity temp = this.getEntity(start).get();
        if (temp.asMoveable().isPresent()) {
            temp.asMoveable().get().setHasMoved();
        }
        this.removeEntity(end);
        this.removeEntity(start);
        this.setEntity(end, temp);
        notifyEntityMoved(start, end, temp);
        notifyEntityMoved(start, end);
    }
}
