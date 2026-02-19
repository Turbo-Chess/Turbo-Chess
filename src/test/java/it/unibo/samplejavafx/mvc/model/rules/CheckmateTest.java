package it.unibo.samplejavafx.mvc.model.rules;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoardImpl;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.handler.GameState;
import it.unibo.samplejavafx.mvc.model.handler.TurnHandler;
import it.unibo.samplejavafx.mvc.model.handler.TurnHandlerImpl;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

public class CheckmateTest {
    private ChessBoard board;
    private TurnHandler handler;
    private Integer idCount;

    @BeforeEach
    void setUp() {
        this.board = new ChessBoardImpl();
        this.handler = new TurnHandlerImpl(0, board);
        this.idCount = 0;
    }

    @Test
    void testSimpleCheckmate() throws StreamReadException, DatabindException, IOException {
        board.setEntity(new Point2D(4, 0), TestUtilities.createKing(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(3, 0), TestUtilities.createRook(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(5, 0), TestUtilities.createRook(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(3, 1), TestUtilities.createPawn(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(5, 1), TestUtilities.createPawn(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(4, 7), TestUtilities.createRook(PlayerColor.BLACK, idCount));
        countInc();

        final Map<Piece, List<Point2D>> intPieces = new HashMap<>();
        assertTrue(AdvancedRules.checkmate(board, PlayerColor.WHITE, GameState.CHECK, intPieces));
        assertTrue(intPieces.keySet().isEmpty());
    }

    @Test
    void testSimpleFalseCheckmate() throws StreamReadException, DatabindException, IOException {
        board.setEntity(new Point2D(4, 0), TestUtilities.createKing(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(3, 0), TestUtilities.createRook(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(5, 0), TestUtilities.createRook(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(3, 1), TestUtilities.createPawn(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(4, 7), TestUtilities.createRook(PlayerColor.BLACK, idCount));
        countInc();

        final Map<Piece, List<Point2D>> intPieces = new HashMap<>();
        assertFalse(AdvancedRules.checkmate(board, PlayerColor.WHITE, GameState.CHECK, intPieces));
        assertTrue(intPieces.keySet().isEmpty());
    }

    @Test
    void testInterposingCheckmate() throws StreamReadException, DatabindException, IOException {
        board.setEntity(new Point2D(4, 0), TestUtilities.createKing(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(3, 0), TestUtilities.createRook(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(5, 0), TestUtilities.createRook(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(3, 1), TestUtilities.createPawn(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(5, 1), TestUtilities.createBishop(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(4, 7), TestUtilities.createRook(PlayerColor.BLACK, idCount));
        countInc();

        final Map<Piece, List<Point2D>> intPieces = new HashMap<>();
        assertFalse(AdvancedRules.checkmate(board, PlayerColor.WHITE, GameState.CHECK, intPieces));
        assertFalse(intPieces.keySet().isEmpty());
    }

    @Test
    void testMultipleInterposingCheckmate() throws StreamReadException, DatabindException, IOException {
        board.setEntity(new Point2D(4, 0), TestUtilities.createKing(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(3, 0), TestUtilities.createRook(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(5, 0), TestUtilities.createRook(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(3, 1), TestUtilities.createPawn(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(5, 1), TestUtilities.createBishop(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(4, 7), TestUtilities.createRook(PlayerColor.BLACK, idCount));
        countInc();
        board.setEntity(new Point2D(0, 4), TestUtilities.createRook(PlayerColor.WHITE, idCount));
        countInc();

        final Map<Piece, List<Point2D>> intPieces = new HashMap<>();
        assertFalse(AdvancedRules.checkmate(board, PlayerColor.WHITE, GameState.CHECK, intPieces));
        assertTrue(intPieces.keySet().size() == 2);
    }

    @Test
    void testSimpleDoubleCheckmate() throws StreamReadException, DatabindException, IOException {
        board.setEntity(new Point2D(4, 0), TestUtilities.createKing(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(3, 0), TestUtilities.createRook(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(5, 0), TestUtilities.createRook(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(3, 1), TestUtilities.createPawn(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(4, 7), TestUtilities.createRook(PlayerColor.BLACK, idCount));
        countInc();
        board.setEntity(new Point2D(7, 3), TestUtilities.createBishop(PlayerColor.BLACK, idCount));
        countInc();

        final Map<Piece, List<Point2D>> intPieces = new HashMap<>();
        assertTrue(AdvancedRules.checkmate(board, PlayerColor.WHITE, GameState.DOUBLE_CHECK, intPieces));
    }

    @Test
    void testSimpleFalseDoubleCheckmate() throws StreamReadException, DatabindException, IOException {
        board.setEntity(new Point2D(4, 0), TestUtilities.createKing(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(5, 0), TestUtilities.createRook(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(3, 1), TestUtilities.createPawn(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(4, 7), TestUtilities.createRook(PlayerColor.BLACK, idCount));
        countInc();
        board.setEntity(new Point2D(7, 3), TestUtilities.createBishop(PlayerColor.BLACK, idCount));
        countInc();

        final Map<Piece, List<Point2D>> intPieces = new HashMap<>();
        assertFalse(AdvancedRules.checkmate(board, PlayerColor.WHITE, GameState.DOUBLE_CHECK, intPieces));
    }

    private void countInc() {
        this.idCount += 1;
    }
}
