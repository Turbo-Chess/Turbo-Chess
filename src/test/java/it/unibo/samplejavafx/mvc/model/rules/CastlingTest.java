package it.unibo.samplejavafx.mvc.model.rules;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoardImpl;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

class CastlingTest {
private ChessBoard board;
    private Integer idCount;

    @BeforeEach
    void setUp() {
        this.board = new ChessBoardImpl();
        this.idCount = 0;
    }

    @Test
    void testSimpleCastling() throws StreamReadException, DatabindException, IOException {
        board.setEntity(new Point2D(4, 7), TestUtilities.createKing(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(0, 7), TestUtilities.createRook(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(7, 7), TestUtilities.createRook(PlayerColor.WHITE, idCount));
        countInc();

        assertEquals(CastleCondition.CASTLE_BOTH, AdvancedRules.castle(board, PlayerColor.WHITE));
    }

    @Test
    void testSimpleCastlingLeft() throws StreamReadException, DatabindException, IOException {
        board.setEntity(new Point2D(4, 7), TestUtilities.createKing(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(0, 7), TestUtilities.createRook(PlayerColor.WHITE, idCount));
        countInc();

        assertEquals(CastleCondition.CASTLE_LEFT, AdvancedRules.castle(board, PlayerColor.WHITE));
    }

    @Test
    void testSimpleCastlingRight() throws StreamReadException, DatabindException, IOException {
        board.setEntity(new Point2D(4, 7), TestUtilities.createKing(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(7, 7), TestUtilities.createRook(PlayerColor.WHITE, idCount));
        countInc();

        assertEquals(CastleCondition.CASTLE_RIGHT, AdvancedRules.castle(board, PlayerColor.WHITE));
    }

    @Test
    void testSimpleFalseCastling() throws StreamReadException, DatabindException, IOException {
        board.setEntity(new Point2D(4, 7), TestUtilities.createKing(PlayerColor.WHITE, idCount));
        countInc();

        assertEquals(CastleCondition.NO_CASTLE, AdvancedRules.castle(board, PlayerColor.WHITE));
    }

    @Test
    void testFalseCastling() throws StreamReadException, DatabindException, IOException {
        board.setEntity(new Point2D(4, 7), TestUtilities.createKing(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(1, 7), TestUtilities.createQueen(PlayerColor.WHITE, idCount));
        countInc();
        board.move(new Point2D(1, 7), new Point2D(0, 7));

        assertEquals(CastleCondition.NO_CASTLE, AdvancedRules.castle(board, PlayerColor.WHITE));
    }

    @Test
    void testFalseSingleCastling() throws StreamReadException, DatabindException, IOException {
        board.setEntity(new Point2D(4, 7), TestUtilities.createKing(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(0, 7), TestUtilities.createRook(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(7, 7), TestUtilities.createRook(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(2, 7), TestUtilities.createRook(PlayerColor.WHITE, idCount));
        countInc();

        assertEquals(CastleCondition.CASTLE_RIGHT, AdvancedRules.castle(board, PlayerColor.WHITE));
    }

    @Test
    void testFalseDoubleCastling() throws StreamReadException, DatabindException, IOException {
        board.setEntity(new Point2D(4, 7), TestUtilities.createKing(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(0, 7), TestUtilities.createRook(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(7, 7), TestUtilities.createRook(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(2, 7), TestUtilities.createRook(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(6, 7), TestUtilities.createRook(PlayerColor.WHITE, idCount));
        countInc();


        assertEquals(CastleCondition.NO_CASTLE, AdvancedRules.castle(board, PlayerColor.WHITE));
    }

    @Test
    void testSingleInterruptedCastling() throws StreamReadException, DatabindException, IOException {
        board.setEntity(new Point2D(4, 7), TestUtilities.createKing(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(0, 7), TestUtilities.createRook(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(7, 7), TestUtilities.createRook(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(6, 0), TestUtilities.createRook(PlayerColor.BLACK, idCount));
        countInc();


        assertEquals(CastleCondition.CASTLE_LEFT, AdvancedRules.castle(board, PlayerColor.WHITE));
    }

    @Test
    void testDoubleInterruptedCastling() throws StreamReadException, DatabindException, IOException {
        board.setEntity(new Point2D(4, 7), TestUtilities.createKing(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(0, 7), TestUtilities.createRook(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(7, 7), TestUtilities.createRook(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(2, 0), TestUtilities.createRook(PlayerColor.BLACK, idCount));
        countInc();
        board.setEntity(new Point2D(5, 0), TestUtilities.createRook(PlayerColor.BLACK, idCount));
        countInc();


        assertEquals(CastleCondition.NO_CASTLE, AdvancedRules.castle(board, PlayerColor.WHITE));
    }

    private void countInc() {
        this.idCount += 1;
    }
}
