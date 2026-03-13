package it.unibo.samplejavafx.mvc.model.replay;

import it.unibo.samplejavafx.mvc.controller.replay.ReplayController;
import it.unibo.samplejavafx.mvc.controller.replay.ReplayControllerImpl;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoardImpl;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PieceDefinition;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import it.unibo.samplejavafx.mvc.model.properties.GameProperties;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ReplayBugTest {

    private static final String IMAGE_PATH = GameProperties.EXTERNAL_ASSETS_FOLDER.getPath();
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
        final GameHistory history = new GameHistory();

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
        final GameHistory history = new GameHistory();
        
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
