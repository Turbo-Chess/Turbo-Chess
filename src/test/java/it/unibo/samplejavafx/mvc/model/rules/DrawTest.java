package it.unibo.samplejavafx.mvc.model.rules;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoardImpl;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

public class DrawTest {
    private ChessBoard board;
    private Integer idCount;

    @BeforeEach
    void setUp() {
        this.board = new ChessBoardImpl();
        this.idCount = 0;
    }

    @Test
    void testSimpleDraw() throws StreamReadException, DatabindException, IOException {
        board.setEntity(new Point2D(0, 0), TestUtilities.createKing(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(1, 7), TestUtilities.createRook(PlayerColor.BLACK, idCount));
        countInc();
        board.setEntity(new Point2D(7, 1), TestUtilities.createRook(PlayerColor.BLACK, idCount));
        countInc();

        assertTrue(AdvancedRules.draw(board, PlayerColor.WHITE));
    }

    @Test
    void testSimpleFalseDraw() throws StreamReadException, DatabindException, IOException {
        board.setEntity(new Point2D(0, 0), TestUtilities.createKing(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(2, 7), TestUtilities.createRook(PlayerColor.BLACK, idCount));
        countInc();
        board.setEntity(new Point2D(7, 1), TestUtilities.createRook(PlayerColor.BLACK, idCount));
        countInc();

        assertFalse(AdvancedRules.draw(board, PlayerColor.WHITE));
    }

    @Test
    void testSimpleDrawWithPiece() throws StreamReadException, DatabindException, IOException {
        board.setEntity(new Point2D(0, 0), TestUtilities.createKing(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(1, 7), TestUtilities.createRook(PlayerColor.BLACK, idCount));
        countInc();
        board.setEntity(new Point2D(7, 1), TestUtilities.createRook(PlayerColor.BLACK, idCount));
        countInc();
        board.setEntity(new Point2D(2, 0), TestUtilities.createPawn(PlayerColor.BLACK, idCount));
        countInc();

        assertTrue(AdvancedRules.draw(board, PlayerColor.WHITE));
    }

    @Test
    void testPieceDraw() throws StreamReadException, DatabindException, IOException {
        board.setEntity(new Point2D(0, 0), TestUtilities.createKing(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(1, 7), TestUtilities.createKing(PlayerColor.BLACK, idCount));
        countInc();

        assertTrue(AdvancedRules.draw(board, PlayerColor.WHITE));
    }

    @Test
    void testImpossibleCheckDraw1() throws StreamReadException, DatabindException, IOException {
        board.setEntity(new Point2D(0, 0), TestUtilities.createKing(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(1, 7), TestUtilities.createKing(PlayerColor.BLACK, idCount));
        countInc();
        board.setEntity(new Point2D(4, 5), TestUtilities.createKnight(PlayerColor.BLACK, idCount));
        countInc();

        assertTrue(AdvancedRules.draw(board, PlayerColor.WHITE));
    }

    @Test
    void testImpossibleCheckDraw2() throws StreamReadException, DatabindException, IOException {
        board.setEntity(new Point2D(0, 0), TestUtilities.createKing(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(1, 7), TestUtilities.createKing(PlayerColor.BLACK, idCount));
        countInc();
        board.setEntity(new Point2D(4, 5), TestUtilities.createBishop(PlayerColor.BLACK, idCount));
        countInc();

        assertTrue(AdvancedRules.draw(board, PlayerColor.WHITE));
    }

    @Test
    void testFalseImpossibleCheckDraw() throws StreamReadException, DatabindException, IOException {
        board.setEntity(new Point2D(0, 0), TestUtilities.createKing(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(1, 7), TestUtilities.createKing(PlayerColor.BLACK, idCount));
        countInc();
        board.setEntity(new Point2D(4, 5), TestUtilities.createRook(PlayerColor.BLACK, idCount));
        countInc();

        assertFalse(AdvancedRules.draw(board, PlayerColor.WHITE));
    }

    // Implement test for PowerUps whenever they are fully functioning

    private void countInc() {
        this.idCount += 1;
    }
}
