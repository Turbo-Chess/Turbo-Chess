package it.unibo.samplejavafx.mvc.model.rules;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoardImpl;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CheckCalculatorTest {
    private ChessBoard board;
    private Integer idCount;

    @BeforeEach
    void setUp() {
        board = new ChessBoardImpl();
        idCount = 0;
    }

    @Test
    void testHorizontalCheckInterposition() throws StreamReadException, DatabindException, IOException {
        board.setEntity(new Point2D(0, 0), TestUtilities.createKing(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(7, 0), TestUtilities.createRook(PlayerColor.BLACK, idCount));
        countInc();
        board.setEntity(new Point2D(4, 4), TestUtilities.createRook(PlayerColor.WHITE, idCount));

        final List<Piece> blockers = CheckCalculator.getInterposingPieces(board, PlayerColor.WHITE).keySet().stream().toList();
        assertEquals(1, blockers.size());
        assertEquals(new Point2D(4, 4), board.getPosByEntity(blockers.get(0)));
    }

    @Test
    void testVerticalCheckInterposition() throws StreamReadException, DatabindException, IOException {
        board.setEntity(new Point2D(4, 0), TestUtilities.createKing(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(4, 7), TestUtilities.createQueen(PlayerColor.BLACK, idCount));
        countInc();
        board.setEntity(new Point2D(2, 2), TestUtilities.createBishop(PlayerColor.WHITE, idCount));

        final List<Piece> blockers = CheckCalculator.getInterposingPieces(board, PlayerColor.WHITE).keySet().stream().toList();
        assertEquals(1, blockers.size());
        assertEquals(new Point2D(2, 2), board.getPosByEntity(blockers.get(0)));
    }

    @Test
    void testDiagonalCheckInterposition() throws StreamReadException, DatabindException, IOException {
        board.setEntity(new Point2D(0, 0), TestUtilities.createKing(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(7, 7), TestUtilities.createBishop(PlayerColor.BLACK, idCount));
        countInc();
        board.setEntity(new Point2D(0, 5), TestUtilities.createRook(PlayerColor.WHITE, idCount));

        final List<Piece> blockers = CheckCalculator.getInterposingPieces(board, PlayerColor.WHITE).keySet().stream().toList();
        assertEquals(1, blockers.size());
        assertEquals(new Point2D(0, 5), board.getPosByEntity(blockers.get(0)));
    }

    @Test
    void testKnightCheckCannotBlock() throws StreamReadException, DatabindException, IOException {
        board.setEntity(new Point2D(4, 4), TestUtilities.createKing(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(6, 5), TestUtilities.createKnight(PlayerColor.BLACK, idCount));
        countInc();
        board.setEntity(new Point2D(4, 5), TestUtilities.createRook(PlayerColor.WHITE, idCount));

        final List<Piece> blockers = CheckCalculator.getInterposingPieces(board, PlayerColor.WHITE).keySet().stream().toList();
        assertTrue(blockers.isEmpty());
    }

    @Test
    void testAdjacentCheckCannotBlock() throws StreamReadException, DatabindException, IOException {
        board.setEntity(new Point2D(0, 0), TestUtilities.createKing(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(0, 1), TestUtilities.createRook(PlayerColor.BLACK, idCount));
        countInc();
        board.setEntity(new Point2D(5, 5), TestUtilities.createRook(PlayerColor.WHITE, idCount));

        final List<Piece> blockers = CheckCalculator.getInterposingPieces(board, PlayerColor.WHITE).keySet().stream().toList();
        assertTrue(blockers.isEmpty());
    }

    @Test
    void testPinnedPieceCannotBlock() throws StreamReadException, DatabindException, IOException {
        board.setEntity(new Point2D(0, 0), TestUtilities.createKing(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(0, 7), TestUtilities.createRook(PlayerColor.BLACK, idCount));
        countInc();
        board.setEntity(new Point2D(7, 0), TestUtilities.createRook(PlayerColor.BLACK, idCount));
        countInc();
        board.setEntity(new Point2D(0, 3), TestUtilities.createRook(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(0, 7), TestUtilities.createRook(PlayerColor.BLACK, idCount)); // Pins the white rook
        countInc();
        
        board = new ChessBoardImpl(); // Reset
        board.setEntity(new Point2D(0, 0), TestUtilities.createKing(PlayerColor.WHITE, idCount));
        countInc();
        board.setEntity(new Point2D(7, 0), TestUtilities.createRook(PlayerColor.BLACK, idCount));
        countInc();
        board.setEntity(new Point2D(7, 7), TestUtilities.createBishop(PlayerColor.BLACK, idCount));
        countInc();
        board.setEntity(new Point2D(3, 3), TestUtilities.createQueen(PlayerColor.WHITE, idCount));
        
        final List<Piece> blockers = CheckCalculator.getInterposingPieces(board, PlayerColor.WHITE).keySet().stream().toList();
        // The queen at 3,3 cannot move to 3,0 because it would expose the king to the bishop.
        assertTrue(blockers.isEmpty());
    }

    /*  
    void testPrioritization() throws StreamReadException, DatabindException, IOException {
        board.setEntity(new Point2D(0, 0), TestUtilities.createKing(PlayerColor.WHITE));
        board.setEntity(new Point2D(0, 7), TestUtilities.createRook(PlayerColor.BLACK));
        board.setEntity(new Point2D(2, 3), TestUtilities.createKnight(PlayerColor.WHITE));
        board.setEntity(new Point2D(5, 4), TestUtilities.createRook(PlayerColor.WHITE));
        board.setEntity(new Point2D(2, 5), TestUtilities.createQueen(PlayerColor.WHITE));

        List<Piece> blockers = CheckCalculator.getInterposingPieces(board, PlayerColor.WHITE).keySet().stream().toList();
        
        assertEquals(3, blockers.size());
        
        assertEquals(PieceType.INFERIOR, blockers.stream()
                                                .filter(piece -> piece.getType() == PieceType.INFERIOR).findFirst().get().getType());
        assertEquals(PieceType.TOWER, blockers.stream()
                                                .filter(piece -> piece.getType() == PieceType.TOWER).findFirst().get().getType());
        assertEquals(PieceType.SUPERIOR, blockers.stream()
                                                .filter(piece -> piece.getType() == PieceType.SUPERIOR).findFirst().get().getType());
    }
    */

    private void countInc() {
        this.idCount += 1;
    }
}
