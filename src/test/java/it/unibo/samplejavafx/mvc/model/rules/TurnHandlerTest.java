package it.unibo.samplejavafx.mvc.model.rules;

import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatch;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatchImpl;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.handler.GameState;
import it.unibo.samplejavafx.mvc.model.handler.TurnHandler;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl.MoveType;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import it.unibo.samplejavafx.mvc.model.rules.TestUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TurnHandlerTest {

    private ChessMatch match;
    private TurnHandler handler;
    private ChessBoard board;
    private int idCount = 0;

    @BeforeEach
    void setUp() {
        match = new ChessMatchImpl();
        handler = match.getTurnHandler();
        board = match.getBoard();
        idCount = 0;
    }

    private void countInc() {
        idCount++;
    }

    @Test
    void testInterposingPiecesArePreservedAfterTurnSwitch() throws IOException {
        // Setup:
        // White Rook at (0, 0)
        // Black King at (7, 7)
        // Black Rook at (3, 0)
        // Move White Rook to (0, 7) -> Check on Black King.
        // Attack line is along y=7 from x=0 to x=7.
        // Black Rook at (3, 0) should be able to move to (3, 7) to block.

        board.setEntity(new Point2D(0, 0), TestUtilities.createRook(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(7, 7), TestUtilities.createKing(PlayerColor.BLACK, idCount));
        countInc();
        board.setEntity(new Point2D(3, 0), TestUtilities.createRook(PlayerColor.BLACK, idCount));
        countInc();

        // 1. Select White Rook
        List<Point2D> moves = handler.thinking(new Point2D(0, 0));
        assertTrue(moves.contains(new Point2D(0, 7)), "White Rook should be able to move to (0, 7)");

        // 2. Execute move to (0, 7)
        boolean moveResult = handler.executeTurn(MoveType.MOVE_ONLY, new Point2D(0, 7));
        assertTrue(moveResult, "Move should be executed successfully");

        // 3. Verify game state
        assertEquals(PlayerColor.BLACK, match.getCurrentPlayer(), "Turn should switch to Black");
        assertEquals(GameState.CHECK, match.getGameState(), "Game state should be CHECK");

        // 4. Select Black Rook
        // This triggers doIfCheck internally
        List<Point2D> blackMoves = handler.thinking(new Point2D(3, 0));

        // 5. Verify Black Rook can move to (3, 7)
        assertFalse(blackMoves.isEmpty(), "Black Rook should have valid moves to block");
        assertTrue(blackMoves.contains(new Point2D(3, 7)), "Black Rook should be able to block at (3, 7)");
    }
}
