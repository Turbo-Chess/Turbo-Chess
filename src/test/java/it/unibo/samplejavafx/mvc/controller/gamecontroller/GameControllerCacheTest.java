package it.unibo.samplejavafx.mvc.controller.gamecontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoardImpl;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PieceDefinition;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Test class to verify the GameController's movementCache functionality.
 * Tests that pieces loaded from JSON correctly return move lists and that the cache can store, retrieve and clear data.
 */
class GameControllerCacheTest {

    /**
     * Comprehensive test that loads a piece from JSON, calculates moves, and tests cache store/retrieve/clear.
     *
     * @throws IOException if there is an error reading the JSON file
     */
    @Test
    void testMovementCache() throws IOException {
        // Load piece definition from JSON
        final String resourcePath = "src/main/resources/EntityResources/StandardChessPieces/pieces/Knight.json";
        final ObjectMapper mapper = new ObjectMapper();
        final PieceDefinition knightDef;

        knightDef = mapper.readValue(new File(resourcePath), PieceDefinition.class);

        assertEquals("knight", knightDef.getId(), "Knight ID should match");

        // Create a piece from the loaded definition
        final Piece knight = new Piece.Builder()
                .entityDefinition(knightDef)
                .gameId(42)
                .playerColor(PlayerColor.WHITE)
                .build();

        final GameController controller = new GameControllerImpl();
        final ChessBoard board = new ChessBoardImpl();

        final Point2D knightPos = new Point2D(4, 4);
        board.setEntity(knightPos, knight);

        final List<Point2D> calculatedMoves = knight.getValidMoves(knightPos, board);

        assertNotNull(calculatedMoves, "Calculated moves should not be null");
        assertFalse(calculatedMoves.isEmpty(), "Knight should have valid moves from position (4, 4)");

        controller.cacheAvailableCells(knight.getGameId(), calculatedMoves);

        final List<Point2D> cachedMoves = controller.getAvailableCells(knight.getGameId());

        assertNotNull(cachedMoves, "Cached moves should not be null");
        assertEquals(calculatedMoves.size(), cachedMoves.size(),
                "Cached moves count should match calculated moves count");
        assertTrue(cachedMoves.containsAll(calculatedMoves),
                "All calculated moves should be retrievable from cache");

        assertFalse(cachedMoves.isEmpty(), "Cache should contain moves after storing");

        controller.clearCache();

        final List<Point2D> movesAfterClear = controller.getAvailableCells(knight.getGameId());
        assertNotNull(movesAfterClear, "Should return non-null list after clear");
        assertTrue(movesAfterClear.isEmpty(), "Cache should be empty after clear");
    }
}



