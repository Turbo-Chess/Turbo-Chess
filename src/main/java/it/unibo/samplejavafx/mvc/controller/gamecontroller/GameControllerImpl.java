package it.unibo.samplejavafx.mvc.controller.gamecontroller;

import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameControllerImpl implements GameController {
    private final Map<Integer, List<Point2D>> movementCache = new HashMap<>();

    @Override
    public List<Point2D> getAvailableCells(int pieceGameId) {
        return movementCache.getOrDefault(pieceGameId, Collections.emptyList());
    }
}
