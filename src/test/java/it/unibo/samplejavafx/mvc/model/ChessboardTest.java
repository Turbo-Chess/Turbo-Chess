package it.unibo.samplejavafx.mvc.model;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoardImpl;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PieceDefinition;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 */
class ChessboardTest {

    private Piece createTestPiece(final int gameId, final PlayerColor color) {
        final PieceDefinition def = new PieceDefinition.Builder()
                .setName("test-piece")
                .setId("test")
                .setImagePath("/home/giacomo/Documents/pawn.jpg")
                .setWeight(3)
                .setPieceType(PieceType.INFERIOR)
                .setMoveRules(List.of(new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING)))
                .build();
        return new Piece.Builder()
                .setHasMoved(false)
                .setEntityDefinition(def)
                .setGameId(gameId)
                .setPlayerColor(color)
                .build();
    }

    /**
     *
     */
    @Test
   void initTest() {
        final Piece p = createTestPiece(1, PlayerColor.BLACK);

        // Test if a cell is free at initialization and then correctly occupied
        final ChessBoard board = new ChessBoardImpl();
        assertTrue(board.isFree(new Point2D(2, 2)));
        board.setEntity(new Point2D(2, 2), p);
        assertFalse(board.isFree(new Point2D(2, 2)));

        board.removeEntity(new Point2D(2, 2));
        assertTrue(board.isFree(new Point2D(2, 2)));

        assertTrue(board.isFree(new Point2D(5, 3)));
        board.setEntity(new Point2D(5, 3), p);
        assertFalse(board.isFree(new Point2D(5, 3)));
    }

    /**
     *
     */
    @Test
    void checkBoardLimits() {
        final ChessBoard board = new ChessBoardImpl();
        // Test that the limit values are correctly generated (the isFree method access the map, so it would throw
        // an error if a point does not exist)
        assertTrue(board.checkBounds(new Point2D(0, 0)));
        assertTrue(board.checkBounds(new Point2D(7, 7)));
        assertFalse(board.checkBounds(new Point2D(8, 8)));
    }

    @Test
    void checkGetPos() {
        final ChessBoard board = new ChessBoardImpl();
        final Piece p1 = createTestPiece(1, PlayerColor.BLACK);
        final Piece p2 = createTestPiece(2, PlayerColor.BLACK);

       board.setEntity(new Point2D(2, 2), p1);
       board.setEntity(new Point2D(3, 3), p2);

       assertEquals(new Point2D(2, 2), board.getPosByEntity(p1));
       assertEquals(new Point2D(3, 3), board.getPosByEntity(p2));
    }

    @Test
    void removeNull() {
        final ChessBoard board = new ChessBoardImpl();

        board.removeEntity(new Point2D(2, 2));
    }
}
