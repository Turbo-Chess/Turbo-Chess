package it.unibo.samplejavafx.mvc.model.replay;

import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PieceDefinition;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import it.unibo.samplejavafx.mvc.model.properties.GameProperties;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameHistoryRecorderTest {

    private static final String IMAGE_PATH = GameProperties.EXTERNAL_ASSETS_FOLDER.getPath().replace("file:", "");
    private static final Piece TEST_PIECE = new Piece.Builder()
        .setHasMoved(false)
        .entityDefinition(new PieceDefinition.Builder()
            .name("Pawn")
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
    void testMoveRecording() {
        final GameHistoryRecorder recorder = new GameHistoryRecorder(() -> 1);
        final Point2D from = new Point2D(0, 0);
        final Point2D to = new Point2D(0, 1);

        // Simulate move sequence from ChessBoardImpl
        // 1. Remove from 'from'
        recorder.onEntityRemoved(from, TEST_PIECE);
        // 2. Add to 'to'
        recorder.onEntityAdded(to, TEST_PIECE);
        // 3. Move notification
        recorder.onEntityMoved(from, to);

        final List<GameEvent> events = recorder.getHistory().getEvents();
        assertEquals(1, events.size());
        assertTrue(events.get(0) instanceof MoveEvent);
        assertEquals(from, ((MoveEvent) events.get(0)).from());
        assertEquals(to, ((MoveEvent) events.get(0)).to());
    }

    @Test
    void testCaptureRecording() {
        final GameHistoryRecorder recorder = new GameHistoryRecorder(() -> 1);
        final Point2D from = new Point2D(0, 0);
        final Point2D to = new Point2D(1, 1);
        final Entity captured = TEST_PIECE;

        recorder.onEntityRemoved(to, captured);
        recorder.onEntityRemoved(from, TEST_PIECE);
        recorder.onEntityAdded(to, TEST_PIECE);
        recorder.onEntityEaten(from, to);

        final List<GameEvent> events = recorder.getHistory().getEvents();
        assertEquals(1, events.size());
        assertTrue(events.get(0) instanceof MoveEvent); 

        assertEquals(from, ((MoveEvent) events.get(0)).from());
        assertEquals(to, ((MoveEvent) events.get(0)).to());
        assertEquals(captured, ((MoveEvent) events.get(0)).capturedEntity());
    }
}
