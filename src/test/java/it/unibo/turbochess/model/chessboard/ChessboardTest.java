// CHECKSTYLE: MagicNumber OFF

package it.unibo.turbochess.model.chessboard;

import it.unibo.turbochess.model.chessboard.board.api.ChessBoard;
import it.unibo.turbochess.model.chessboard.board.impl.ChessBoardImpl;
import it.unibo.turbochess.model.entity.impl.Piece;
import it.unibo.turbochess.model.entity.impl.PieceType;
import it.unibo.turbochess.model.entity.impl.PlayerColor;
import it.unibo.turbochess.model.entity.definition.PieceDefinition;
import it.unibo.turbochess.model.movement.impl.MoveRulesImpl;
import it.unibo.turbochess.model.point2d.Point2D;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Test class to verify the functionality of the {@link ChessBoard} implementation.
 *
 * <p>
 * Specifically, it tests basic board operations like entity placement, removal, bounds checking, and piece movement/capture.
 * </p>
 */
class ChessboardTest {

    private Piece createTestPiece(final int gameId, final PlayerColor color) {
        final PieceDefinition def = new PieceDefinition.Builder()
                .name("test-piece")
                .id("test")
                .imagePath("classpath:/assets/images/white_pawn.png")
                .weight(3)
                .pieceType(PieceType.INFERIOR)
                .moveRules(List.of(new MoveRulesImpl(new Point2D(0, 1),
                        MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING, false)))
                .build();
        return new Piece.Builder()
                .moved(false)
                .entityDefinition(def)
                .gameId(gameId)
                .playerColor(color)
                .build();
    }

    /**
     * Tests the initialization of the board and verifies that cells can be correctly occupied and freed.
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
     * Tests the boundary checking logic of the chessboard.
     *
     * <p>
     * Verifies that valid coordinates return true and invalid ones false.
     * </p>
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

    /**
     * Tests the retrieval of a piece's position.
     */
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

    /**
     * Tests that removing a null entity works correctly does not throw exception.
     */
    @Test
    void removeNull() {
        final ChessBoard board = new ChessBoardImpl();

        board.removeEntity(new Point2D(2, 2));
    }

    /**
     * Tests the {@link ChessBoard#move(Point2D, Point2D)} function.
     *
     * <p>
     * Verifies that a piece is correctly moved from one position to another 
     * freeing the start position and occupying the end position.
     * </p>
     */
    @Test
    void testMove() {
        final ChessBoard board = new ChessBoardImpl();
        final Piece piece = createTestPiece(1, PlayerColor.WHITE);
        final Point2D startPos = new Point2D(2, 2);
        final Point2D endPos = new Point2D(4, 4);

        // Set the piece at the start position
        board.setEntity(startPos, piece);
        assertTrue(board.getEntity(startPos).isPresent(), "Start position should contain the piece");
        assertEquals(piece, board.getEntity(startPos).get(), "The piece at start position should be the same");

        // Move the piece from start to end position
        board.move(startPos, endPos);

        // Verify the start position is now free
        assertTrue(board.isFree(startPos), "Start position should be free after move");
        assertFalse(board.getEntity(startPos).isPresent(), "Start position should not contain any entity");

        // Verify the end position contains the piece
        assertFalse(board.isFree(endPos), "End position should be occupied after move");
        assertTrue(board.getEntity(endPos).isPresent(), "End position should contain the piece");
        assertEquals(piece, board.getEntity(endPos).get(),
                "The piece at end position should be the same piece that was moved");

        // Verify the piece position can be retrieved correctly
        assertEquals(endPos, board.getPosByEntity(piece), "The position of the moved piece should be the end position");
    }

    /**
     * Tests the {@link ChessBoard#eat(Point2D, Point2D)} function.
     *
     * <p>
     * Verifies that a piece correctly captures another piece,
     * resulting in the target cell being occupied by the attacker and the captured piece being removed.
     * </p>
     */
    @Test
    void testEat() {
        final ChessBoard board = new ChessBoardImpl();
        final Piece attackingPiece = createTestPiece(1, PlayerColor.WHITE);
        final Piece targetPiece = createTestPiece(2, PlayerColor.BLACK);
        final Point2D attackerPos = new Point2D(2, 2);
        final Point2D targetPos = new Point2D(3, 3);

        // Set both pieces on the board
        board.setEntity(attackerPos, attackingPiece);
        board.setEntity(targetPos, targetPiece);

        // Verify both pieces are on the board
        assertTrue(board.getEntity(attackerPos).isPresent(), "Attacker position should contain the attacking piece");
        assertTrue(board.getEntity(targetPos).isPresent(), "Target position should contain the target piece");
        assertEquals(attackingPiece, board.getEntity(attackerPos).get(), "Attacking piece should be at attacker position");
        assertEquals(targetPiece, board.getEntity(targetPos).get(), "Target piece should be at target position");

        // Execute the eat move
        board.eat(attackerPos, targetPos);

        // Verify the attacker position is now free
        assertTrue(board.isFree(attackerPos), "Attacker position should be free after eating");
        assertFalse(board.getEntity(attackerPos).isPresent(), "Attacker position should not contain any entity");

        // Verify the target position now contains the attacking piece
        assertFalse(board.isFree(targetPos), "Target position should be occupied after eating");
        assertTrue(board.getEntity(targetPos).isPresent(), "Target position should contain the attacking piece");
        assertEquals(attackingPiece, board.getEntity(targetPos).get(), "Target position should contain the attacking piece");

        // Verify the attacking piece position is updated correctly
        assertEquals(targetPos, board.getPosByEntity(attackingPiece), "Attacking piece should be at target position");

        // Verify the captured piece is no longer on the board
        assertNull(board.getPosByEntity(targetPiece), "Captured piece should not have a position on the board");
    }
}
// CHECKSTYLE: MagicNumber ON
