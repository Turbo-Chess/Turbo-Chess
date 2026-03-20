// CHECKSTYLE: MagicNumber OFF

package it.unibo.turbochess.model.rules;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;

import it.unibo.turbochess.model.chessboard.board.api.ChessBoard;
import it.unibo.turbochess.model.chessboard.board.impl.ChessBoardImpl;
import it.unibo.turbochess.model.point2d.Point2D;
import it.unibo.turbochess.model.entity.impl.PlayerColor;

class CastlingTest {
    private ChessBoard board;
    private Integer idCount;

    @BeforeEach
    void setUp() {
        this.board = new ChessBoardImpl();
        this.idCount = 0;
    }

    @Test
    void testSimpleCastling() throws IOException {
        board.setEntity(new Point2D(4, 7), TestUtilities.createKing(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(0, 7), TestUtilities.createRook(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(7, 7), TestUtilities.createRook(PlayerColor.WHITE, idCount));
        countInc();

        assertEquals(CastleCondition.CASTLE_BOTH, AdvancedRules.castle(board, PlayerColor.WHITE));
    }

    @Test
    void testLeftInterruptedCastling() throws StreamReadException, DatabindException, IOException {
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
    void testRightInterruptedCastling() throws IOException {
        board.setEntity(new Point2D(4, 7), TestUtilities.createKing(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(0, 7), TestUtilities.createRook(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(7, 7), TestUtilities.createRook(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(6, 7), TestUtilities.createRook(PlayerColor.WHITE, idCount));
        countInc();

        assertEquals(CastleCondition.CASTLE_LEFT, AdvancedRules.castle(board, PlayerColor.WHITE));
    }

    @Test
    void testBothInterruptedCastling() throws StreamReadException, DatabindException, IOException {
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

// CHECKSTYLE: MagicNumber ON
