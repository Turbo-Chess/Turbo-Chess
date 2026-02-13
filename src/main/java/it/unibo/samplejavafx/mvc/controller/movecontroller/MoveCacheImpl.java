package it.unibo.samplejavafx.mvc.controller.movecontroller;

import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoveCacheImpl implements MoveCache {
    private final Map<Integer, List<Point2D>> movementCache = new HashMap<>();

    @Override
    public List<Point2D> getAvailableCells(int pieceGameId) {
        return movementCache.getOrDefault(pieceGameId, Collections.emptyList());
    }

    @Override
    public void cacheAvailableCells(int pieceGameId, List<Point2D> moves) {
        movementCache.put(pieceGameId, List.copyOf(moves));
    }

    @Override
    public void clearCache() {
        movementCache.clear();
    }
}
