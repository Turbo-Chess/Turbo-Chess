package it.unibo.samplejavafx.mvc.model;

import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.entity.PowerUp;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import it.unibo.samplejavafx.mvc.model.replay.DespawnEvent;
import it.unibo.samplejavafx.mvc.model.replay.GameHistory;
import it.unibo.samplejavafx.mvc.model.replay.MoveEvent;
import it.unibo.samplejavafx.mvc.model.replay.ReplayManager;
import it.unibo.samplejavafx.mvc.model.replay.SpawnEvent;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReplayTest {
    private static final String PIECE_ID = "test";
    private static final String PIECE_NAME = "test-piece";
    private static final String IMAGE_PATH = "/home/giacomo/Documents/pawn.jpg";
    private static final Piece testPiece = new Piece(
        PIECE_ID, 
        PIECE_NAME, 
        0, 
        IMAGE_PATH, 
        PlayerColor.WHITE, 
        1,
        List.of(
            new MoveRulesImpl(
                new Point2D(0, 1), 
                MoveRulesImpl.MoveType.MOVE_AND_EAT, 
                MoveRulesImpl.MoveStrategy.JUMPING
            )
        )
    );

    @Test
    void testSaveAndLoad() throws IOException {
        final ReplayManager manager = new ReplayManager();
        final GameHistory history = new GameHistory();

        // Add move event
        history.addEvent(
            new MoveEvent(
                1, 
                testPiece,
                new Point2D(0, 0),
                new Point2D(0, 1)
            )
        );

        // Add spawn event
        history.addEvent(
            new SpawnEvent(
                2, 
                testPiece, 
                new Point2D(4, 4)
            )
        );

        // Add despawn event
        history.addEvent(
            new DespawnEvent(
                3, 
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
        System.out.println("Replay saved to: " + file.getAbsolutePath());

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
        assertEquals(PIECE_NAME, se.entity().getName());
    }
}
