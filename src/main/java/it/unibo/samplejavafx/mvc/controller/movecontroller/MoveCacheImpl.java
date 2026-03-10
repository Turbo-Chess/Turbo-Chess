package it.unibo.samplejavafx.mvc.controller.movecontroller;

import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A concrete implementation of {@link MoveCache} using a {@link HashMap}.
 *
 * <p>
 * This implementation stores valid moves mapped by the piece's unique identifier.
 * It provides constant-time access to cached moves.
 * </p>
 */
public final class MoveCacheImpl implements MoveCache {
    private final Map<Integer, List<Point2D>> movementCache = new HashMap<>();

    public MoveCacheImpl() {
        // Empty costructor, no paramere
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Point2D> getAvailableCells(final int pieceGameId) {
        return movementCache.getOrDefault(pieceGameId, Collections.emptyList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cacheAvailableCells(final int pieceGameId, final List<Point2D> moves) {
        movementCache.put(pieceGameId, List.copyOf(moves));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearCache() {
        movementCache.clear();
    }
}
