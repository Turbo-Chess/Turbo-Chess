package it.unibo.samplejavafx.mvc.controller.movecontroller;

import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple cache for move calculation results to avoid recomputing moves for the same piece and board state.
 *
 * <p>
 * This handles caching of valid moves for a piece at a specific position on the board.
 * </p>
 */
public class MoveCacheImpl implements MoveCache {
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
