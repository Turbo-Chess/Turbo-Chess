package it.unibo.samplejavafx.mvc.model.movement;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoardImpl;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PieceDefinition;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class made to test the general behavior of the move rules, pieces created on the spot just to verify
 * that the rules are working.
 */
class MovementTest {
    private static final String PIECE_ID = "test";
    private static final String PIECE_NAME = "test-piece";
    private static final String IMAGE_PATH = "/home/giacomo/Documents/pawn.jpg";

    ChessBoard board = new ChessBoardImpl();
    int counter;

    @BeforeEach
    void initBoard() {
        this.board = new ChessBoardImpl();
        this.counter = 0;
    }

    private Piece createPiece(final String id, final String name, final PlayerColor color, final int weight, final PieceType type, final List<MoveRules> moveRules) {
        final PieceDefinition def = new PieceDefinition.Builder()
                .name(name)
                .id(id)
                .imagePath(IMAGE_PATH)
                .weight(weight)
                .pieceType(type)
                .moveRules(moveRules)
                .build();
        final int currentGameId = counter;
        counter++;
        return new Piece.Builder()
            .setHasMoved(false)
            .entityDefinition(def)
            .gameId(currentGameId)
            .playerColor(color)
            .build();
    }

    @Test
    void testWhiteBlackMovement() {
        // This test wants to prove that the white piece moves in the reversed y direction compared to the black
        final Piece blackPiece = createPiece(PIECE_ID, PIECE_NAME, PlayerColor.BLACK, 3, PieceType.INFERIOR,
                List.of(new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING)));

        final Piece whitePiece = createPiece(PIECE_ID, PIECE_NAME, PlayerColor.WHITE, 3, PieceType.INFERIOR,
                List.of(new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING)));

        board.setEntity(new Point2D(4, 4), whitePiece);
        board.setEntity(new Point2D(2, 2), blackPiece);

        whitePiece.getValidMoves(new Point2D(4, 4), board);
        assertEquals(List.of(new Point2D(4, 3)), whitePiece.getAvailableCells());

        blackPiece.getValidMoves(new Point2D(2, 2), board);
        assertEquals(List.of(new Point2D(2, 3)), blackPiece.getAvailableCells());
    }

    @Test
    void testJumping() {
        final Piece blackJumpingPiece = createPiece(PIECE_ID, PIECE_NAME, PlayerColor.BLACK, 3, PieceType.INFERIOR,
                List.of(
                        new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(0, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(-1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING)
                ));

        final Piece whiteJumpingPiece = createPiece(PIECE_ID, PIECE_NAME, PlayerColor.WHITE, 3, PieceType.INFERIOR,
                List.of(
                        new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(0, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(-1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING)
                ));

        // In the following test, we asssume that there are no entities on the piece way

        //Verify the jumping movement of the black piece, assuming it remains on board limits
        board.setEntity(new Point2D(1, 2), blackJumpingPiece);
        blackJumpingPiece.getValidMoves(new Point2D(1, 2), board);
        assertEquals(Set.of(new Point2D(1,3), new Point2D(1, 1), new Point2D(2, 2), new Point2D(0, 2)), new HashSet<>(blackJumpingPiece.getAvailableCells()));

        //Verify the jumping movement of the white piece, assuming it remains on board limits
        board.setEntity(new Point2D(5, 6), whiteJumpingPiece);
        whiteJumpingPiece.getValidMoves(new Point2D(5, 6), board);
        assertEquals(Set.of(new Point2D(5,5), new Point2D(5, 7), new Point2D(6, 6), new Point2D(4, 6)), new HashSet<>(whiteJumpingPiece.getAvailableCells()));

        board.removeEntity(new Point2D(1, 2));
        board.removeEntity(new Point2D(5, 6));

        //Verify the jumping movement of the black piece, assuming it goes out of board limits
        board.setEntity(new Point2D(0, 0), blackJumpingPiece);
        blackJumpingPiece.getValidMoves(new Point2D(0, 0), board);
        assertEquals(Set.of(new Point2D(0, 1), new Point2D(1, 0)), new HashSet<>(blackJumpingPiece.getAvailableCells()));

        //Verify the jumping movement of the white piece, assuming it remains on board limits
        board.setEntity(new Point2D(7, 7), whiteJumpingPiece);
        whiteJumpingPiece.getValidMoves(new Point2D(7, 7), board);
        assertEquals(Set.of(new Point2D(7,6), new Point2D(6, 7)), new HashSet<>(whiteJumpingPiece.getAvailableCells()));
    }

    @Test
    void testSliding() {
        final Piece blackSlidingPiece = createPiece(PIECE_ID, PIECE_NAME, PlayerColor.BLACK, 3, PieceType.INFERIOR,
                List.of(
                        new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING),
                        new MoveRulesImpl(new Point2D(0, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING),
                        new MoveRulesImpl(new Point2D(1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING),
                        new MoveRulesImpl(new Point2D(-1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING)
                ));

        final Piece whiteSlidingPiece = createPiece(PIECE_ID, PIECE_NAME, PlayerColor.WHITE, 3, PieceType.INFERIOR,
                List.of(
                        new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING),
                        new MoveRulesImpl(new Point2D(0, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING),
                        new MoveRulesImpl(new Point2D(1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING),
                        new MoveRulesImpl(new Point2D(-1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING)
                ));

        // Test the sliding movement with no other entities on the way
        board.setEntity(new Point2D(1, 2), blackSlidingPiece);
        blackSlidingPiece.getValidMoves(new Point2D(1, 2), board);
        assertEquals(Set.of(new Point2D(1, 1), new Point2D(1, 0), new Point2D(1, 3), new Point2D(1, 4), new Point2D(1, 5), new Point2D(1, 6), new Point2D(1, 7), new Point2D(0, 2), new Point2D(2, 2), new Point2D(3, 2), new Point2D(4, 2), new Point2D(5, 2), new Point2D(6, 2), new Point2D(7, 2)), new HashSet<>(blackSlidingPiece.getAvailableCells()));

        board.setEntity(new Point2D(5, 6), whiteSlidingPiece);
        whiteSlidingPiece.getValidMoves(new Point2D(5, 6), board);
        assertEquals(Set.of(new Point2D(5, 5), new Point2D(5, 4), new Point2D(5, 3), new Point2D(5, 2), new Point2D(5, 1), new Point2D(5, 0), new Point2D(5, 7), new Point2D(6, 6), new Point2D(7, 6), new Point2D(4, 6), new Point2D(3, 6), new Point2D(2, 6), new Point2D(1, 6), new Point2D(0, 6)), new HashSet<>(whiteSlidingPiece.getAvailableCells()));
    }

    @Test
    void testMoveOnlyRule() {
        final Piece blackSlidingPiece = createPiece(PIECE_ID, PIECE_NAME, PlayerColor.BLACK, 3, PieceType.INFERIOR,
                List.of(
                        new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_ONLY, MoveRulesImpl.MoveStrategy.SLIDING),
                        new MoveRulesImpl(new Point2D(0, -1), MoveRulesImpl.MoveType.MOVE_ONLY, MoveRulesImpl.MoveStrategy.SLIDING),
                        new MoveRulesImpl(new Point2D(1, 0), MoveRulesImpl.MoveType.MOVE_ONLY, MoveRulesImpl.MoveStrategy.SLIDING),
                        new MoveRulesImpl(new Point2D(-1, 0), MoveRulesImpl.MoveType.MOVE_ONLY, MoveRulesImpl.MoveStrategy.SLIDING)
                ));

        final Piece whiteJumpingPiece = createPiece(PIECE_ID, PIECE_NAME, PlayerColor.WHITE, 3, PieceType.INFERIOR,
                List.of(
                        new MoveRulesImpl(new Point2D(2, 1), MoveRulesImpl.MoveType.MOVE_ONLY, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(-2, -1), MoveRulesImpl.MoveType.MOVE_ONLY, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(-2, 1), MoveRulesImpl.MoveType.MOVE_ONLY, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(2, -1), MoveRulesImpl.MoveType.MOVE_ONLY, MoveRulesImpl.MoveStrategy.JUMPING)
                ));

        // Dummy Pieces
        final Piece blackDummyPiece = createPiece("dummy1", "blackDummy", PlayerColor.BLACK, 3, PieceType.INFERIOR, List.of());
        board.setEntity(new Point2D(3, 2), blackDummyPiece);
        final Piece whiteDummyPiece = createPiece("dummy2", "whiteDummy", PlayerColor.WHITE, 3, PieceType.INFERIOR, List.of());
        board.setEntity(new Point2D(1, 4), whiteDummyPiece);

        board.setEntity(new Point2D(1, 2), blackSlidingPiece);
        blackSlidingPiece.getValidMoves(new Point2D(1, 2), board);
        assertEquals(Set.of(new Point2D(1, 1), new Point2D(1, 0), new Point2D(0, 2), new Point2D(2, 2), new Point2D(1, 3)), new HashSet<>(blackSlidingPiece.getAvailableCells()));

        // Change dummy positions
        board.removeEntity(new Point2D(3, 2));
        board.removeEntity(new Point2D(1, 4));
        board.setEntity(new Point2D(7, 6), blackDummyPiece);
        board.setEntity(new Point2D(3, 4), whiteDummyPiece);

        board.setEntity(new Point2D(5, 5), whiteJumpingPiece);
        whiteJumpingPiece.getValidMoves(new Point2D(5, 5), board);
        assertEquals(Set.of(new Point2D(7,4), new Point2D(3, 6)), new HashSet<>(whiteJumpingPiece.getAvailableCells()));
    }

    @Test
    void testEatOnlyRule() {
        final Piece blackSlidingPiece = createPiece(PIECE_ID, PIECE_NAME, PlayerColor.BLACK, 3, PieceType.INFERIOR,
                List.of(
                        new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.EAT_ONLY, MoveRulesImpl.MoveStrategy.SLIDING),
                        new MoveRulesImpl(new Point2D(0, -1), MoveRulesImpl.MoveType.EAT_ONLY, MoveRulesImpl.MoveStrategy.SLIDING),
                        new MoveRulesImpl(new Point2D(1, 0), MoveRulesImpl.MoveType.EAT_ONLY, MoveRulesImpl.MoveStrategy.SLIDING),
                        new MoveRulesImpl(new Point2D(-1, 0), MoveRulesImpl.MoveType.EAT_ONLY, MoveRulesImpl.MoveStrategy.SLIDING)
                ));

        final Piece whiteJumpingPiece = createPiece(PIECE_ID, PIECE_NAME, PlayerColor.WHITE, 3, PieceType.INFERIOR,
                List.of(
                        new MoveRulesImpl(new Point2D(2, 1), MoveRulesImpl.MoveType.EAT_ONLY, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(-2, -1), MoveRulesImpl.MoveType.EAT_ONLY, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(2, -1), MoveRulesImpl.MoveType.EAT_ONLY, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(-2, 1), MoveRulesImpl.MoveType.EAT_ONLY, MoveRulesImpl.MoveStrategy.JUMPING)
                ));

        // Dummy Pieces
        final Piece blackDummyPiece = createPiece("dummy1", "blackDummy", PlayerColor.BLACK, 3, PieceType.INFERIOR, List.of());
        board.setEntity(new Point2D(3, 2), blackDummyPiece);
        final Piece whiteDummyPiece = createPiece("dummy2", "whiteDummy", PlayerColor.WHITE, 3, PieceType.INFERIOR, List.of());
        board.setEntity(new Point2D(1, 4), whiteDummyPiece);


        board.setEntity(new Point2D(1, 2), blackSlidingPiece);
        blackSlidingPiece.getValidMoves(new Point2D(1, 2), board);
        assertEquals(Set.of(new Point2D(1, 4)), new HashSet<>(blackSlidingPiece.getAvailableCells()));

        // Change dummy positions
        board.removeEntity(new Point2D(3, 2));
        board.removeEntity(new Point2D(1, 4));
        board.setEntity(new Point2D(7, 6), blackDummyPiece);
        board.setEntity(new Point2D(3, 4), whiteDummyPiece);

        board.setEntity(new Point2D(5, 5), whiteJumpingPiece);
        whiteJumpingPiece.getValidMoves(new Point2D(5, 5), board);
        assertEquals(Set.of(new Point2D(7,6)), new HashSet<>(whiteJumpingPiece.getAvailableCells()));
    }

    @Test
     void testBothRules() {
        final Piece blackSlidingPiece = createPiece(PIECE_ID, PIECE_NAME, PlayerColor.BLACK, 3, PieceType.INFERIOR,
                List.of(
                        new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING),
                        new MoveRulesImpl(new Point2D(0, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING),
                        new MoveRulesImpl(new Point2D(1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING),
                        new MoveRulesImpl(new Point2D(-1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING)
                ));

        final Piece whiteJumpingPiece = createPiece(PIECE_ID, PIECE_NAME, PlayerColor.WHITE, 3, PieceType.INFERIOR,
                List.of(
                        new MoveRulesImpl(new Point2D(2, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(-2, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(2, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(-2, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING)
                ));

        // Dummy Pieces
        final Piece blackDummyPiece = createPiece("dummy1", "blackDummy", PlayerColor.BLACK, 3, PieceType.INFERIOR, List.of());
        board.setEntity(new Point2D(3, 2), blackDummyPiece);
        final Piece whiteDummyPiece = createPiece("dummy2", "whiteDummy", PlayerColor.WHITE, 3, PieceType.INFERIOR, List.of());
        board.setEntity(new Point2D(1, 4), whiteDummyPiece);


        board.setEntity(new Point2D(1, 2), blackSlidingPiece);
        blackSlidingPiece.getValidMoves(new Point2D(1, 2), board);
        assertEquals(Set.of(new Point2D(1, 3), new Point2D(1, 4), new Point2D(1, 1), new Point2D(1, 0), new Point2D(0, 2), new Point2D(2, 2)), new HashSet<>(blackSlidingPiece.getAvailableCells()));

        // Change dummy positions
        board.removeEntity(new Point2D(3, 2));
        board.removeEntity(new Point2D(1, 4));
        board.setEntity(new Point2D(7, 6), blackDummyPiece);
        board.setEntity(new Point2D(3, 4), whiteDummyPiece);

        board.setEntity(new Point2D(5, 5), whiteJumpingPiece);
        whiteJumpingPiece.getValidMoves(new Point2D(5, 5), board);
        assertEquals(Set.of(new Point2D(7,6), new Point2D(3, 6), new Point2D(7, 4)), new HashSet<>(whiteJumpingPiece.getAvailableCells()));
    }
}
