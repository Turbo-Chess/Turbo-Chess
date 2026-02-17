package it.unibo.samplejavafx.mvc.controller.gamecontroller;

import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * placeholder.
 */
public final class GameControllerImpl implements GameController {
    private final Map<Integer, List<Point2D>> movementCache = new HashMap<>();

    @Override
    public List<Point2D> getAvailableCells(final int pieceGameId) {
        return movementCache.getOrDefault(pieceGameId, Collections.emptyList());
    }

    @Override
    public void cacheAvailableCells(final int pieceGameId, final List<Point2D> moves) {
        movementCache.put(pieceGameId, List.copyOf(moves));
    }

    @Override
    public void clearCache() {
        movementCache.clear();
    }
}
