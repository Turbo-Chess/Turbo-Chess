package it.unibo.samplejavafx.mvc.model;

import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PieceDefinition;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import it.unibo.samplejavafx.mvc.model.replay.DespawnEvent;
import it.unibo.samplejavafx.mvc.model.replay.GameHistory;
import it.unibo.samplejavafx.mvc.model.replay.MoveEvent;
import it.unibo.samplejavafx.mvc.model.replay.ReplayManager;
import it.unibo.samplejavafx.mvc.model.replay.SpawnEvent;
import it.unibo.samplejavafx.mvc.controller.replay.ReplayController;
import it.unibo.samplejavafx.mvc.controller.replay.ReplayControllerImpl;
import it.unibo.samplejavafx.mvc.model.chessboard.BoardObserver;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoardImpl;
import it.unibo.samplejavafx.mvc.model.entity.Entity;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReplayTest {

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


    @Test
    void testSaveAndLoad() throws IOException {
        final ReplayManager manager = new ReplayManager();
        final GameHistory history = new GameHistory();

        // Add move event
        history.addEvent(
            new MoveEvent(
                1, 
                createTestPiece(1, PlayerColor.WHITE),
                new Point2D(0, 0),
                new Point2D(0, 1)
            )
        );

        // Add spawn event
        history.addEvent(
            new SpawnEvent(
                2, 
                createTestPiece(2, PlayerColor.WHITE),
                new Point2D(4, 4)
            )
        );

        // Add despawn event
        history.addEvent(
            new DespawnEvent(
                3, 
                createTestPiece(3, PlayerColor.WHITE),  
                new Point2D(5, 5)
            )
        );

        final Path debugDir = Path.of("test-saves");
        if (!Files.exists(debugDir)) {
            Files.createDirectories(debugDir);
        }
        
        final File file = debugDir.resolve("replay.json").toFile();
        if (file.exists()) {
            file.delete();
        }
        
        final boolean saved = manager.saveGame(history, file.toPath());

        assertTrue(saved);
        assertTrue(file.exists());

        final GameHistory loaded = manager.loadGame(file.toPath());

        assertEquals(3, loaded.getEvents().size());
        
        // Check types
        assertTrue(loaded.getEvents().get(0) instanceof MoveEvent);
        assertTrue(loaded.getEvents().get(1) instanceof SpawnEvent);
        assertTrue(loaded.getEvents().get(2) instanceof DespawnEvent);

        // Check values
        final MoveEvent me = (MoveEvent) loaded.getEvents().get(0);
        assertEquals(1, me.getTurn());
        assertEquals(new Point2D(0, 0), me.from());

        final SpawnEvent se = (SpawnEvent) loaded.getEvents().get(1);
        assertEquals(2, se.getTurn());
        assertTrue(se.entity() instanceof Piece);
        assertEquals("test-piece", se.entity().getName());
    }

    @Test
    void testReplayWithObserver() {
        final ChessBoardImpl board = new ChessBoardImpl();
        final ReplayController controller = new ReplayControllerImpl(board);
        final List<String> events = new ArrayList<>();

        board.addObserver(new BoardObserver() {
            @Override
            public void onEntityAdded(final Point2D pos, final Entity entity) {
                events.add("ADDED: " + pos + " " + entity.getName());
            }

            @Override
            public void onEntityRemoved(final Point2D pos, final Entity entity) {
                events.add("REMOVED: " + pos + " " + entity.getName());
            }
        });

        final GameHistory history = new GameHistory();
        // Move: (0,0) -> (0,1)
        history.addEvent(new MoveEvent(1, createTestPiece(1, PlayerColor.WHITE), new Point2D(0, 0), new Point2D(0, 1)));
        
        controller.loadHistory(history);

        assertTrue(events.isEmpty());
        
        assertTrue(controller.next());
        
        assertEquals(1, events.size());
        assertEquals("ADDED: (0, 1) test-piece", events.get(0));
        
        events.clear();

        // Step 2: Revert MoveEvent
        // Logic: remove from (0,1), add to (0,0)
        assertTrue(controller.prev());
        
        // Since (0,1) has the piece now, removeEntity((0,1)) should succeed -> REMOVED
        // setEntity((0,0)) -> ADDED
        
        assertEquals(2, events.size());
        assertEquals("REMOVED: (0, 1) test-piece", events.get(0));
        assertEquals("ADDED: (0, 0) test-piece", events.get(1));
    }
}
