package it.unibo.samplejavafx.mvc.controller.movecontroller;

import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

import java.util.List;

/**
 * Defines a caching mechanism for storing and retrieving valid movement paths for chess pieces.
 * <p>
 * This interface is designed to optimize performance by storing calculated moves for pieces,
 * preventing the need to recalculate them multiple times within the same turn or context
 * when the board state implies they haven't changed.
 * </p>
 */
public interface MoveCache {
    /**
     * Retrieves the cached list of valid moves for a specific piece.
     *
     * @param pieceGameId the unique identifier of the piece in the current game context.
     * @return a list of valid {@link Point2D} destinations, or an empty list if no moves are found in the cache.
     */
    List<Point2D> getAvailableCells(int pieceGameId);

    /**
     * Stores the calculated valid moves for a specific piece in the cache.
     *
     * @param pieceGameId the unique identifier of the piece.
     * @param moves       the list of valid moves to be cached.
     */
    void cacheAvailableCells(int pieceGameId, List<Point2D> moves);

    /**
     * Clears all cached move data.
     * <p>
     * This method should be called whenever the board state changes (e.g., after a move is executed),
     * as previous calculations become invalid.
     * </p>
     */
    void clearCache();
}
