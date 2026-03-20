// CHECKSTYLE: MagicNumber OFF

package it.unibo.turbochess.model.replay;

import it.unibo.turbochess.model.entity.impl.Piece;
import it.unibo.turbochess.model.entity.impl.PieceType;
import it.unibo.turbochess.model.entity.impl.PlayerColor;
import it.unibo.turbochess.model.entity.definition.PieceDefinition;
import it.unibo.turbochess.model.movement.impl.MoveRulesImpl;
import it.unibo.turbochess.model.point2d.Point2D;
import it.unibo.turbochess.controller.replay.api.ReplayController;
import it.unibo.turbochess.controller.replay.impl.ReplayControllerImpl;
import it.unibo.turbochess.model.chessboard.board.api.BoardObserver;
import it.unibo.turbochess.model.chessboard.board.impl.ChessBoardImpl;
import it.unibo.turbochess.model.entity.api.Entity;
import it.unibo.turbochess.model.replay.api.ReplayManager;
import it.unibo.turbochess.model.replay.impl.DespawnEvent;
import it.unibo.turbochess.model.replay.api.GameHistory;
import it.unibo.turbochess.model.replay.impl.GameHistoryImpl;
import it.unibo.turbochess.model.replay.impl.MoveEvent;
import it.unibo.turbochess.model.replay.impl.ReplayManagerImpl;
import it.unibo.turbochess.model.replay.impl.SpawnEvent;

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
    private static final String IMAGE_PATH = "classpath:/assets/images/white_pawn.png";
    private static final Piece TEST_PIECE = new Piece.Builder()
        .moved(false)
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
        final ReplayManager manager = new ReplayManagerImpl();
        final GameHistory history = new GameHistoryImpl();

        // Add move event
        history.addEvent(
            new MoveEvent(
                1,
                PIECE_NAME,
                PlayerColor.WHITE,
                new Point2D(0, 0),
                new Point2D(0, 1),
                null,
                0,
                0
            )
        );

        // Add spawn event
        history.addEvent(
            new SpawnEvent(
                2,
                TEST_PIECE,
                new Point2D(4, 4),
                0,
                0
            )
        );

        // Add despawn event
        history.addEvent(
            new DespawnEvent(
                3,
                TEST_PIECE,
                new Point2D(5, 5),
                0,
                0
            )
        );

        final Path debugDir = Path.of("test-saves");
        if (!Files.exists(debugDir)) {
            Files.createDirectories(debugDir);
        }

        final File file = debugDir.resolve("replay.json").toFile();
        if (file.exists()) {
            assertTrue(file.delete(), "Failed to delete existing replay.json file");
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

            @Override
            public void onEntityMoved(final Point2D from, final Point2D to, final Entity entity) {
                events.add("MOVED: " + from + " -> " + to + " " + entity.getName());
            }
        });

        final GameHistory history = new GameHistoryImpl();

        history.addEvent(new SpawnEvent(0, TEST_PIECE, new Point2D(0, 0), 0, 0));
        history.addEvent(new MoveEvent(1, PIECE_NAME, PlayerColor.WHITE, new Point2D(0, 0), new Point2D(0, 1), null, 0, 0));

        controller.loadHistory(history);
        assertTrue(events.isEmpty());

        assertTrue(controller.next().isPresent());
        assertEquals(1, events.size());
        assertEquals("ADDED: (0, 0) Pawn", events.get(0));
        events.clear();

        assertTrue(controller.next().isPresent());

        assertEquals(2, events.size());
        assertEquals("REMOVED: (0, 0) Pawn", events.get(0));
        assertEquals("ADDED: (0, 1) Pawn", events.get(1));

        events.clear();

        // Step 2: Revert MoveEvent
        assertTrue(controller.prev().isPresent());

        assertEquals(2, events.size());
        assertEquals("REMOVED: (0, 1) Pawn", events.get(0));
        assertEquals("ADDED: (0, 0) Pawn", events.get(1));
    }
}

// CHECKSTYLE: MagicNumber ON
