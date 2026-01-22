package it.unibo.samplejavafx.mvc.model;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoardImpl;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.movement.JumpingMovement;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl;
import it.unibo.samplejavafx.mvc.model.movement.SlidingMovement;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MovementTest {
    ChessBoard board = new ChessBoardImpl();
    Comparator<Point2D> xComparator = (pos1, pos2) -> pos1.x() - pos2.x();

    @BeforeEach
    public void initBoard() {
        this.board = new ChessBoardImpl();
    }

    @Test
    public void testWhiteBlackMovement() {
        // This test wants to prove that the white piece moves in the reversed y direction compared to the black
        final Piece blackPiece = new Piece("test", "test-piece", Path.of("/home/giacomo/Documents/pawn.jpg"), false,
                3, List.of(
                new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, new JumpingMovement())
        ));

        final Piece whitePiece = new Piece("test", "test-piece", Path.of("/home/giacomo/Documents/pawn.jpg"), true,
                3, List.of(
                new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, new JumpingMovement())
        ));

        board.setEntity(new Point2D(4, 4), whitePiece);
        board.setEntity(new Point2D(2, 2), blackPiece);

        whitePiece.getValidMoves(new Point2D(4, 4), board);
        assertEquals(List.of(new Point2D(4, 3)), whitePiece.getAvailableCells());

        blackPiece.getValidMoves(new Point2D(2, 2), board);
        assertEquals(List.of(new Point2D(2, 3)), blackPiece.getAvailableCells());
    }

    @Test
    public void testJumping() {
        final Piece blackJumpingPiece = new Piece("test", "test-piece", Path.of("/home/giacomo/Documents/pawn.jpg"), false,
                3, List.of(
                new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, new JumpingMovement()),
                new MoveRulesImpl(new Point2D(0, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, new JumpingMovement()),
                new MoveRulesImpl(new Point2D(1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, new JumpingMovement()),
                new MoveRulesImpl(new Point2D(-1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, new JumpingMovement())
        ));

        final Piece whiteJumpingPiece = new Piece("test", "test-piece", Path.of("/home/giacomo/Documents/pawn.jpg"), true,
                3, List.of(
                new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, new JumpingMovement()),
                new MoveRulesImpl(new Point2D(0, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, new JumpingMovement()),
                new MoveRulesImpl(new Point2D(1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, new JumpingMovement()),
                new MoveRulesImpl(new Point2D(-1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, new JumpingMovement())
        ));

        // In the following test, we asssume that there are no entities on the piece way

        //Verify the jumping movement of the black piece, assuming it remains on board limits
        board.setEntity(new Point2D(1, 2), blackJumpingPiece);
        blackJumpingPiece.getValidMoves(new Point2D(1, 2), board);
        assertEquals(List.of(new Point2D(1,3), new Point2D(1, 1), new Point2D(2, 2), new Point2D(0, 2)), blackJumpingPiece.getAvailableCells());

        //Verify the jumping movement of the white piece, assuming it remains on board limits
        board.setEntity(new Point2D(5, 6), whiteJumpingPiece);
        whiteJumpingPiece.getValidMoves(new Point2D(5, 6), board);
        assertEquals(List.of(new Point2D(5,5), new Point2D(5, 7), new Point2D(6, 6), new Point2D(4, 6)), whiteJumpingPiece.getAvailableCells());


        //Verify the jumping movement of the black piece, assuming it goes out of board limits
        board.setEntity(new Point2D(0, 0), blackJumpingPiece);
        blackJumpingPiece.getValidMoves(new Point2D(0, 0), board);
        assertEquals(List.of(new Point2D(0, 1), new Point2D(1, 0)), blackJumpingPiece.getAvailableCells());

        //Verify the jumping movement of the white piece, assuming it remains on board limits
        board.setEntity(new Point2D(7, 7), whiteJumpingPiece);
        whiteJumpingPiece.getValidMoves(new Point2D(7, 7), board);
        assertEquals(List.of(new Point2D(7,6), new Point2D(6, 7)), whiteJumpingPiece.getAvailableCells());
    }

    @Test
    public void testSliding() {
        final Piece blackSlidingPiece = new Piece("test", "test-piece", Path.of("/home/giacomo/Documents/pawn.jpg"), false,
                3, List.of(
                new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, new SlidingMovement()),
                new MoveRulesImpl(new Point2D(0, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, new SlidingMovement()),
                new MoveRulesImpl(new Point2D(1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, new SlidingMovement()),
                new MoveRulesImpl(new Point2D(-1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, new SlidingMovement())
        ));

        final Piece whiteSlidingPiece = new Piece("test", "test-piece", Path.of("/home/giacomo/Documents/pawn.jpg"), true,
                3, List.of(
                new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, new SlidingMovement()),
                new MoveRulesImpl(new Point2D(0, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, new SlidingMovement()),
                new MoveRulesImpl(new Point2D(1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, new SlidingMovement()),
                new MoveRulesImpl(new Point2D(-1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, new SlidingMovement())
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
    public void testMoveOnlyRule() {
        final Piece blackSlidingPiece = new Piece("test", "test-piece", Path.of("/home/giacomo/Documents/pawn.jpg"), false,
                3, List.of(
                new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_ONLY, new SlidingMovement()),
                new MoveRulesImpl(new Point2D(0, -1), MoveRulesImpl.MoveType.MOVE_ONLY, new SlidingMovement()),
                new MoveRulesImpl(new Point2D(1, 0), MoveRulesImpl.MoveType.MOVE_ONLY, new SlidingMovement()),
                new MoveRulesImpl(new Point2D(-1, 0), MoveRulesImpl.MoveType.MOVE_ONLY, new SlidingMovement())
        ));

        final Piece whiteJumpingPiece = new Piece("test", "test-piece", Path.of("/home/giacomo/Documents/pawn.jpg"), true,
                3, List.of(
                new MoveRulesImpl(new Point2D(2, 1), MoveRulesImpl.MoveType.MOVE_ONLY, new JumpingMovement()),
                new MoveRulesImpl(new Point2D(-2, -1), MoveRulesImpl.MoveType.MOVE_ONLY, new JumpingMovement()),
                new MoveRulesImpl(new Point2D(-2, 1), MoveRulesImpl.MoveType.MOVE_ONLY, new JumpingMovement()),
                new MoveRulesImpl(new Point2D(2, -1), MoveRulesImpl.MoveType.MOVE_ONLY, new JumpingMovement())
        ));

        // Dummy Pieces
        final Piece blackDummyPiece = new Piece("dummy1", "blackDummy", Path.of("/home/giacomo/Documents/pawn.jpg"), false, 3, List.of());
        board.setEntity(new Point2D(3, 2), blackDummyPiece);
        final Piece whiteDummyPiece = new Piece("dummy2", "whiteDummy", Path.of("/home/giacomo/Documents/pawn.jpg"), true, 3, List.of());
        board.setEntity(new Point2D(1, 4), whiteDummyPiece);


        board.setEntity(new Point2D(1, 2), blackSlidingPiece);
        blackSlidingPiece.getValidMoves(new Point2D(1, 2), board);
        assertEquals(Set.of(new Point2D(1, 1), new Point2D(1, 0), new Point2D(0, 2), new Point2D(2, 2), new Point2D(1, 3)), new HashSet<>(blackSlidingPiece.getAvailableCells()));

        // Change dummy positions
        board.setEntity(new Point2D(7, 6), blackDummyPiece);
        board.setEntity(new Point2D(3, 4), whiteDummyPiece);

        board.setEntity(new Point2D(5, 5), whiteJumpingPiece);
        whiteJumpingPiece.getValidMoves(new Point2D(5, 5), board);
        assertEquals(Set.of(new Point2D(7,4), new Point2D(3, 6)), new HashSet<>(whiteJumpingPiece.getAvailableCells()));
    }

    @Test
    public void testEatOnlyRule() {
        final Piece blackSlidingPiece = new Piece("test", "test-piece", Path.of("/home/giacomo/Documents/pawn.jpg"), false,
                3, List.of(
                new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.EAT_ONLY, new SlidingMovement()),
                new MoveRulesImpl(new Point2D(0, -1), MoveRulesImpl.MoveType.EAT_ONLY, new SlidingMovement()),
                new MoveRulesImpl(new Point2D(1, 0), MoveRulesImpl.MoveType.EAT_ONLY, new SlidingMovement()),
                new MoveRulesImpl(new Point2D(-1, 0), MoveRulesImpl.MoveType.EAT_ONLY, new SlidingMovement())
        ));

        final Piece whiteJumpingPiece = new Piece("test", "test-piece", Path.of("/home/giacomo/Documents/pawn.jpg"), true,
                3, List.of(
                new MoveRulesImpl(new Point2D(2, 1), MoveRulesImpl.MoveType.EAT_ONLY, new JumpingMovement()),
                new MoveRulesImpl(new Point2D(-2, -1), MoveRulesImpl.MoveType.EAT_ONLY, new JumpingMovement()),
                new MoveRulesImpl(new Point2D(2, -1), MoveRulesImpl.MoveType.EAT_ONLY, new JumpingMovement()),
                new MoveRulesImpl(new Point2D(-2, 1), MoveRulesImpl.MoveType.EAT_ONLY, new JumpingMovement())
        ));

        // Dummy Pieces
        final Piece blackDummyPiece = new Piece("dummy1", "blackDummy", Path.of("/home/giacomo/Documents/pawn.jpg"), false, 3, List.of());
        board.setEntity(new Point2D(3, 2), blackDummyPiece);
        final Piece whiteDummyPiece = new Piece("dummy2", "whiteDummy", Path.of("/home/giacomo/Documents/pawn.jpg"), true, 3, List.of());
        board.setEntity(new Point2D(1, 4), whiteDummyPiece);


        board.setEntity(new Point2D(1, 2), blackSlidingPiece);
        blackSlidingPiece.getValidMoves(new Point2D(1, 2), board);
        assertEquals(Set.of(new Point2D(1, 4)), new HashSet<>(blackSlidingPiece.getAvailableCells()));

        // Change dummy positions
        board.setEntity(new Point2D(7, 6), blackDummyPiece);
        board.setEntity(new Point2D(3, 4), whiteDummyPiece);

        board.setEntity(new Point2D(5, 5), whiteJumpingPiece);
        whiteJumpingPiece.getValidMoves(new Point2D(5, 5), board);
        assertEquals(Set.of(new Point2D(7,6)), new HashSet<>(whiteJumpingPiece.getAvailableCells()));
    }

    @Test
    public void testBothRules() {
        final Piece blackSlidingPiece = new Piece("test", "test-piece", Path.of("/home/giacomo/Documents/pawn.jpg"), false,
                3, List.of(
                new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.EAT_ONLY, new SlidingMovement()),
                new MoveRulesImpl(new Point2D(0, -1), MoveRulesImpl.MoveType.EAT_ONLY, new SlidingMovement()),
                new MoveRulesImpl(new Point2D(1, 0), MoveRulesImpl.MoveType.EAT_ONLY, new SlidingMovement()),
                new MoveRulesImpl(new Point2D(-1, 0), MoveRulesImpl.MoveType.EAT_ONLY, new SlidingMovement())
        ));

        final Piece whiteJumpingPiece = new Piece("test", "test-piece", Path.of("/home/giacomo/Documents/pawn.jpg"), true,
                3, List.of(
                new MoveRulesImpl(new Point2D(2, 1), MoveRulesImpl.MoveType.EAT_ONLY, new JumpingMovement()),
                new MoveRulesImpl(new Point2D(-2, -1), MoveRulesImpl.MoveType.EAT_ONLY, new JumpingMovement()),
                new MoveRulesImpl(new Point2D(2, -1), MoveRulesImpl.MoveType.EAT_ONLY, new JumpingMovement()),
                new MoveRulesImpl(new Point2D(-2, 1), MoveRulesImpl.MoveType.EAT_ONLY, new JumpingMovement())
        ));

        // Dummy Pieces
        final Piece blackDummyPiece = new Piece("dummy1", "blackDummy", Path.of("/home/giacomo/Documents/pawn.jpg"), false, 3, List.of());
        board.setEntity(new Point2D(3, 2), blackDummyPiece);
        final Piece whiteDummyPiece = new Piece("dummy2", "whiteDummy", Path.of("/home/giacomo/Documents/pawn.jpg"), true, 3, List.of());
        board.setEntity(new Point2D(1, 4), whiteDummyPiece);


        board.setEntity(new Point2D(1, 2), blackSlidingPiece);
        blackSlidingPiece.getValidMoves(new Point2D(1, 2), board);
        assertEquals(Set.of(new Point2D(1, 4)), new HashSet<>(blackSlidingPiece.getAvailableCells()));

        // Change dummy positions
        board.setEntity(new Point2D(7, 6), blackDummyPiece);
        board.setEntity(new Point2D(3, 4), whiteDummyPiece);

        board.setEntity(new Point2D(5, 5), whiteJumpingPiece);
        whiteJumpingPiece.getValidMoves(new Point2D(5, 5), board);
        assertEquals(Set.of(new Point2D(7,6)), new HashSet<>(whiteJumpingPiece.getAvailableCells()));
    }
}

/*

Things to test
* Nero:
MOVE_ONLY -> muove in cella libera OK
          -> muove in cella occupata NO
EAT_ONLY  -> muove in cella libera NO
          -> muove in cella occupata da nemico SI
          -> muove in cella occupata da amico NO
BOTH      -> muove in cella libera SI
          -> muove in cella occupata da nemico SI
          -> muove in cella occupata da amico NO

Bianco:
MOVE_ONLY -> muove in cella libera OK
          -> muove in cella occupata NO
EAT_ONLY  -> muove in cella libera NO
          -> muove in cella occupata da nemico SI
          -> muove in cella occupata da amico NO
BOTH      -> muove in cella libera SI
          -> muove in cella occupata da nemico SI
          -> muove in cella occupata da amico NO

          */
