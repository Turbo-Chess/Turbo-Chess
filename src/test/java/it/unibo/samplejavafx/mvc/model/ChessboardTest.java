package it.unibo.samplejavafx.mvc.model;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoardImpl;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.movement.JumpingMovement;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 */
public class ChessboardTest {
    /**
     *
     */
    @Test
    public void initTest() {
        final Piece p = new Piece("test", "test-piece", Path.of("/home/giacomo/Documents/pawn.jpg"), false,
                3, List.of(
                new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, new JumpingMovement())
        ));

        // Test if a cell is free at initialization and then correctly occupied
        ChessBoard board = new ChessBoardImpl();
        assertTrue(board.isFree(new Point2D(2, 2)));
        board.setEntity(new Point2D(2, 2), p);
        assertFalse(board.isFree(new Point2D(2, 2)));

        assertTrue(board.isFree(new Point2D(5, 3)));
        board.setEntity(new Point2D(5, 3), p);
        assertFalse(board.isFree(new Point2D(5, 3)));
    }

    /**
     *
     */
    @Test
    public void checkBoardLimits() {
        ChessBoard board = new ChessBoardImpl();
        // Test that the limit values are correctly generated (the isFree method access the map, so it would throw
        // an error if a point does not exist)
        assertTrue(board.isFree(new Point2D(0, 0)));
        assertTrue(board.isFree(new Point2D(7, 7)));
        assertThrows(NullPointerException.class, () -> board.isFree(new Point2D(8, 8)));
    }
}
