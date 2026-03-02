package it.unibo.samplejavafx.mvc.model.replay;

import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PieceDefinition;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import it.unibo.samplejavafx.mvc.model.properties.GameProperties;
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
    private static final String PIECE_ID = "test";
    private static final String PIECE_NAME = "Pawn";
    private static final String IMAGE_PATH = GameProperties.EXTERNAL_ASSETS_FOLDER.getPath().replace("file:", "");
    private static final Piece TEST_PIECE = new Piece.Builder()
        .setHasMoved(false)
        .entityDefinition(new PieceDefinition.Builder()
            .name(PIECE_NAME)
            .id(PIECE_ID)
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
    void testSaveAndLoad() throws IOException {
        final ReplayManager manager = new ReplayManager();
        final GameHistory history = new GameHistory();

        // Add move event
        history.addEvent(
            new MoveEvent(
                1, 
                PIECE_NAME,
                new Point2D(0, 0),
                new Point2D(0, 1)
            )
        );

        // Add spawn event
        history.addEvent(
            new SpawnEvent(
                2, 
                TEST_PIECE, 
                new Point2D(4, 4)
            )
        );

        // Add despawn event
        history.addEvent(
            new DespawnEvent(
                3, 
                TEST_PIECE,
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
        assertEquals(PIECE_NAME, me.entityName());

        final SpawnEvent se = (SpawnEvent) loaded.getEvents().get(1);
        assertEquals(2, se.getTurn());
        assertTrue(se.entity() instanceof Piece);
        assertEquals(PIECE_NAME, se.entity().getName());
        
        final DespawnEvent de = (DespawnEvent) loaded.getEvents().get(2);
        assertEquals(3, de.getTurn());
        assertEquals(PIECE_NAME, de.entity().getName());
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
    
        history.addEvent(new SpawnEvent(0, TEST_PIECE, new Point2D(0, 0)));
        history.addEvent(new MoveEvent(1, PIECE_NAME, new Point2D(0, 0), new Point2D(0, 1)));
        
        controller.loadHistory(history);
        assertTrue(events.isEmpty());
        
        assertTrue(controller.next());
        assertEquals(1, events.size());
        assertEquals("ADDED: (0, 0) Pawn", events.get(0));
        events.clear();

        // Step 1: Execute MoveEvent
        assertTrue(controller.next());
        
        // ReplayController logic: 
        // removeEntity(from) -> REMOVED
        // setEntity(to, entity) -> ADDED
        
        assertEquals(2, events.size());
        assertEquals("REMOVED: (0, 0) Pawn", events.get(0));
        assertEquals("ADDED: (0, 1) Pawn", events.get(1));

        events.clear();

        // Step 2: Revert MoveEvent
        assertTrue(controller.prev());
        
        assertEquals(2, events.size());
        assertEquals("REMOVED: (0, 1) Pawn", events.get(0));
        assertEquals("ADDED: (0, 0) Pawn", events.get(1));
    }
}
