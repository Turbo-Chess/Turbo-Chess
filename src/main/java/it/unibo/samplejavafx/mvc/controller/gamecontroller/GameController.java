package it.unibo.samplejavafx.mvc.controller.gamecontroller;

import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

import java.util.List;

public interface GameController {
    List<Point2D> getAvailableCells(int pieceGameId);
    void cacheAvailableCells(int pieceGameId, List<Point2D> moves);
    void clearCache();
}
