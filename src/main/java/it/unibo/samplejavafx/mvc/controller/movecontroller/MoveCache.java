package it.unibo.samplejavafx.mvc.controller.movecontroller;

import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

import java.util.List;

/**
 * placeholder.
 */
public interface MoveCache {
    /**
     * placeholder.
     *
     * @param pieceGameId placeholder.
     * @return placeholder.
     */
    List<Point2D> getAvailableCells(int pieceGameId);

    /**
     * placeholder.
     *
     * @param pieceGameId placeholder.
     * @param moves placeholder.
     */
    void cacheAvailableCells(int pieceGameId, List<Point2D> moves);

    /**
     * placeholder.
     */
    void clearCache();
}
