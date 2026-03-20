package it.unibo.turbochess.model.replay;

import it.unibo.turbochess.controller.replay.api.ReplayController;
import it.unibo.turbochess.controller.replay.impl.ReplayControllerImpl;
import it.unibo.turbochess.model.chessboard.board.impl.ChessBoardImpl;
import it.unibo.turbochess.model.entity.impl.Piece;
import it.unibo.turbochess.model.entity.impl.PieceType;
import it.unibo.turbochess.model.entity.impl.PlayerColor;
import it.unibo.turbochess.model.entity.definition.PieceDefinition;
import it.unibo.turbochess.model.movement.impl.MoveRulesImpl;
import it.unibo.turbochess.model.point2d.Point2D;
import it.unibo.turbochess.model.replay.api.GameHistory;
import it.unibo.turbochess.model.replay.impl.GameHistoryImpl;
import it.unibo.turbochess.model.replay.impl.MoveEvent;
import it.unibo.turbochess.model.replay.impl.SpawnEvent;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ReplayBugTest {

    private static final String IMAGE_PATH = "classpath:/assets/images/";
    private static final String PAWN_NAME = "Pawn";
    private static final Piece TEST_PIECE = new Piece.Builder()
        .moved(false)
        .entityDefinition(new PieceDefinition.Builder()
            .name(PAWN_NAME)
            .id("test")
            .imagePath(IMAGE_PATH)
            .weight(1)
            .pieceType(PieceType.PAWN)
            .moveRules(List.of(
                new MoveRulesImpl(
                    new Point2D(0, 1),
                    MoveRulesImpl.MoveType.MOVE_AND_EAT,
                    MoveRulesImpl.MoveStrategy.JUMPING,
                    false
                )
            ))
            .build())
        .gameId(0)
        .playerColor(PlayerColor.WHITE)
        .build();

    @Test
    void testReplayReloadConsistency() {
        final ChessBoardImpl board = new ChessBoardImpl();
        final ReplayController controller = new ReplayControllerImpl(board);
        final GameHistory history = new GameHistoryImpl();

        history.addEvent(new SpawnEvent(0, TEST_PIECE, new Point2D(0, 0), 0, 0));
        history.addEvent(new MoveEvent(1, PAWN_NAME, PlayerColor.WHITE, new Point2D(0, 0), new Point2D(0, 1), null, 0, 0));

        // --- SESSION 1 ---
        controller.loadHistory(history);

        assertTrue(controller.next().isPresent());
        assertTrue(board.getEntity(new Point2D(0, 0)).isPresent());

        assertTrue(controller.next().isPresent());
        assertTrue(board.getEntity(new Point2D(0, 1)).isPresent());
        assertTrue(board.getEntity(new Point2D(0, 0)).isEmpty());

        assertTrue(controller.prev().isPresent());
        assertTrue(board.getEntity(new Point2D(0, 0)).isPresent(), "After undo, piece should be at (0,0)");
        assertTrue(board.getEntity(new Point2D(0, 1)).isEmpty(), "After undo, piece should NOT be at (0,1)");

        // --- SESSION 2 ---.
        controller.loadHistory(history);

        controller.jumpToEnd();

        assertTrue(board.getEntity(new Point2D(0, 1)).isPresent(), "After reload and jumpToEnd, piece should be at (0,1)");
        assertTrue(board.getEntity(new Point2D(0, 0)).isEmpty(), "After reload and jumpToEnd, piece should NOT be at (0,0)");
    }

    @Test
    void testGhostPieceBug() {
        final ChessBoardImpl board = new ChessBoardImpl();
        final ReplayController controller = new ReplayControllerImpl(board);
        final GameHistory history = new GameHistoryImpl();

        history.addEvent(new SpawnEvent(0, TEST_PIECE, new Point2D(0, 0), 0, 0));
        history.addEvent(new MoveEvent(1, PAWN_NAME, PlayerColor.WHITE, new Point2D(0, 0), new Point2D(0, 1), null, 0, 0));
        history.addEvent(new MoveEvent(2, PAWN_NAME, PlayerColor.BLACK, new Point2D(0, 1), new Point2D(0, 2), null, 0, 0));

        controller.loadHistory(history);
        controller.next();
        controller.next();

        assertTrue(board.getEntity(new Point2D(0, 1)).isPresent());

        controller.loadHistory(history);
        controller.next();

        assertTrue(board.getEntity(new Point2D(0, 0)).isPresent(), "Spawn should place piece at (0,0)");
        assertTrue(board.getEntity(new Point2D(0, 1)).isEmpty(), "Board should have been cleared, so (0,1) should be empty");
    }
}
