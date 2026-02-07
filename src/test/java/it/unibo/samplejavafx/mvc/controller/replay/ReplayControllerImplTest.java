package it.unibo.samplejavafx.mvc.controller.replay;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import it.unibo.samplejavafx.mvc.model.replay.DespawnEvent;
import it.unibo.samplejavafx.mvc.model.replay.GameHistory;
import it.unibo.samplejavafx.mvc.model.replay.MoveEvent;
import it.unibo.samplejavafx.mvc.model.replay.SpawnEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for ReplayControllerImpl.
 */
class ReplayControllerImplTest {

    private static final Piece TEST_PIECE = new Piece(
        "test-id",
        "TestPiece",
        0,
        "/test/path.png",
        PlayerColor.WHITE,
        1,
        PieceType.PAWN,
        List.of(
            new MoveRulesImpl(
                new Point2D(0, 1),
                MoveRulesImpl.MoveType.MOVE_AND_EAT,
                MoveRulesImpl.MoveStrategy.JUMPING
            )
        )
    );

    private StubChessBoard board;
    private ReplayController controller;

    @BeforeEach
    void setUp() {
        board = new StubChessBoard();
        controller = new ReplayControllerImpl(board);
    }

    @Test
    void testNextWithMoveEvent() {
        final GameHistory history = new GameHistory();
        history.addEvent(new MoveEvent(1, TEST_PIECE, new Point2D(0, 0), new Point2D(0, 1)));
        controller.loadHistory(history);

        assertEquals(0, controller.getCurrentIndex());
        assertTrue(controller.next());
        assertEquals(1, controller.getCurrentIndex());

        // Verify move was applied
        // removeEntity was called on (0,0) even if nothing was there
        assertTrue(board.removeWasCalled(new Point2D(0, 0)));
        assertTrue(board.wasAdded(new Point2D(0, 1), TEST_PIECE));
    }

    @Test
    void testNextWithSpawnEvent() {
        final GameHistory history = new GameHistory();
        history.addEvent(new SpawnEvent(1, TEST_PIECE, new Point2D(2, 3)));
        controller.loadHistory(history);

        assertTrue(controller.next());
        assertEquals(1, controller.getCurrentIndex());

        // Verify spawn was applied
        assertTrue(board.wasAdded(new Point2D(2, 3), TEST_PIECE));
    }

    @Test
    void testNextWithDespawnEvent() {
        final GameHistory history = new GameHistory();
        history.addEvent(new DespawnEvent(1, TEST_PIECE, new Point2D(4, 5)));
        controller.loadHistory(history);

        assertTrue(controller.next());
        assertEquals(1, controller.getCurrentIndex());

        // Verify despawn was applied
        assertTrue(board.removeWasCalled(new Point2D(4, 5)));
    }

    @Test
    void testNextAtEnd() {
        final GameHistory history = new GameHistory();
        history.addEvent(new MoveEvent(1, TEST_PIECE, new Point2D(0, 0), new Point2D(0, 1)));
        controller.loadHistory(history);

        assertTrue(controller.next());
        // Try to go beyond end
        assertFalse(controller.next());
        assertEquals(1, controller.getCurrentIndex());
    }

    @Test
    void testPrevWithMoveEvent() {
        final GameHistory history = new GameHistory();
        history.addEvent(new MoveEvent(1, TEST_PIECE, new Point2D(0, 0), new Point2D(0, 1)));
        controller.loadHistory(history);

        controller.next();
        board.clear();

        assertTrue(controller.prev());
        assertEquals(0, controller.getCurrentIndex());

        // Verify move was reverted (entity moved back)
        assertTrue(board.wasRemoved(new Point2D(0, 1)));
        assertTrue(board.wasAdded(new Point2D(0, 0), TEST_PIECE));
    }

    @Test
    void testPrevWithSpawnEvent() {
        final GameHistory history = new GameHistory();
        history.addEvent(new SpawnEvent(1, TEST_PIECE, new Point2D(2, 3)));
        controller.loadHistory(history);

        controller.next();
        board.clear();

        assertTrue(controller.prev());
        assertEquals(0, controller.getCurrentIndex());

        // Verify spawn was reverted (entity removed)
        assertTrue(board.wasRemoved(new Point2D(2, 3)));
    }

    @Test
    void testPrevWithDespawnEvent() {
        final GameHistory history = new GameHistory();
        history.addEvent(new DespawnEvent(1, TEST_PIECE, new Point2D(4, 5)));
        controller.loadHistory(history);

        controller.next();
        board.clear();

        assertTrue(controller.prev());
        assertEquals(0, controller.getCurrentIndex());

        // Verify despawn was reverted (entity re-added)
        assertTrue(board.wasAdded(new Point2D(4, 5), TEST_PIECE));
    }

    @Test
    void testPrevAtStart() {
        final GameHistory history = new GameHistory();
        history.addEvent(new MoveEvent(1, TEST_PIECE, new Point2D(0, 0), new Point2D(0, 1)));
        controller.loadHistory(history);

        // Try to go before start
        assertFalse(controller.prev());
        assertEquals(0, controller.getCurrentIndex());
    }

    @Test
    void testJumpToStart() {
        final GameHistory history = new GameHistory();
        history.addEvent(new MoveEvent(1, TEST_PIECE, new Point2D(0, 0), new Point2D(0, 1)));
        history.addEvent(new SpawnEvent(2, TEST_PIECE, new Point2D(2, 2)));
        history.addEvent(new DespawnEvent(3, TEST_PIECE, new Point2D(3, 3)));
        controller.loadHistory(history);

        // Move to end
        controller.jumpToEnd();
        assertEquals(3, controller.getCurrentIndex());

        // Jump to start
        controller.jumpToStart();
        assertEquals(0, controller.getCurrentIndex());
    }

    @Test
    void testJumpToEnd() {
        final GameHistory history = new GameHistory();
        history.addEvent(new MoveEvent(1, TEST_PIECE, new Point2D(0, 0), new Point2D(0, 1)));
        history.addEvent(new SpawnEvent(2, TEST_PIECE, new Point2D(2, 2)));
        history.addEvent(new DespawnEvent(3, TEST_PIECE, new Point2D(3, 3)));
        controller.loadHistory(history);

        assertEquals(0, controller.getCurrentIndex());

        controller.jumpToEnd();
        assertEquals(3, controller.getCurrentIndex());
    }

    @Test
    void testMultipleNextAndPrev() {
        final GameHistory history = new GameHistory();
        history.addEvent(new MoveEvent(1, TEST_PIECE, new Point2D(0, 0), new Point2D(0, 1)));
        history.addEvent(new SpawnEvent(2, TEST_PIECE, new Point2D(2, 2)));
        controller.loadHistory(history);

        assertEquals(0, controller.getCurrentIndex());

        assertTrue(controller.next());
        assertEquals(1, controller.getCurrentIndex());

        assertTrue(controller.next());
        assertEquals(2, controller.getCurrentIndex());

        assertTrue(controller.prev());
        assertEquals(1, controller.getCurrentIndex());

        assertTrue(controller.prev());
        assertEquals(0, controller.getCurrentIndex());
    }

    @Test
    void testGetTotalEvents() {
        final GameHistory history = new GameHistory();
        history.addEvent(new MoveEvent(1, TEST_PIECE, new Point2D(0, 0), new Point2D(0, 1)));
        history.addEvent(new SpawnEvent(2, TEST_PIECE, new Point2D(2, 2)));
        history.addEvent(new DespawnEvent(3, TEST_PIECE, new Point2D(3, 3)));
        controller.loadHistory(history);

        assertEquals(3, controller.getTotalEvents());
    }

    @Test
    void testEmptyHistory() {
        final GameHistory history = new GameHistory();
        controller.loadHistory(history);

        assertEquals(0, controller.getCurrentIndex());
        assertEquals(0, controller.getTotalEvents());
        assertFalse(controller.next());
        assertFalse(controller.prev());
    }

    @Test
    void testLoadHistoryResetsIndex() {
        final GameHistory history1 = new GameHistory();
        history1.addEvent(new MoveEvent(1, TEST_PIECE, new Point2D(0, 0), new Point2D(0, 1)));
        controller.loadHistory(history1);
        controller.next();

        assertEquals(1, controller.getCurrentIndex());

        // Load new history should reset index
        final GameHistory history2 = new GameHistory();
        history2.addEvent(new SpawnEvent(1, TEST_PIECE, new Point2D(2, 2)));
        controller.loadHistory(history2);

        assertEquals(0, controller.getCurrentIndex());
    }

    /**
     * Stub implementation of ChessBoard for testing.
     * Tracks all setEntity and removeEntity calls.
     */
    private static final class StubChessBoard implements ChessBoard {
        private final Map<Point2D, Entity> entities = new HashMap<>();
        private final Map<Point2D, Entity> addedEntities = new HashMap<>();
        private final Map<Point2D, Entity> removedEntities = new HashMap<>();
        private final Map<Point2D, Boolean> removeCalled = new HashMap<>();

        @Override
        public boolean isFree(final Point2D pos) {
            return !entities.containsKey(pos);
        }

        @Override
        public void setEntity(final Point2D pos, final Entity newEntity) {
            entities.put(pos, newEntity);
            addedEntities.put(pos, newEntity);
        }

        @Override
        public void removeEntity(final Point2D pos) {
            removeCalled.put(pos, true);
            final Entity removed = entities.remove(pos);
            if (removed != null) {
                removedEntities.put(pos, removed);
            }
        }

        @Override
        public Optional<Entity> getEntity(final Point2D pos) {
            return Optional.ofNullable(entities.get(pos));
        }

        @Override
        public Point2D getPosByEntity(final Entity entity) {
            return entities.entrySet().stream()
                .filter(e -> e.getValue().equals(entity))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
        }

        @Override
        public boolean checkBounds(final Point2D pos) {
            return true;
        }

        @Override
        public com.google.common.collect.BiMap<Point2D, Entity> getBoard() {
            return com.google.common.collect.HashBiMap.create(); // Return empty BiMap instead of null
        }

        @Override
        public void addObserver(final it.unibo.samplejavafx.mvc.model.chessboard.BoardObserver observer) {
            // Not needed for these tests
        }

        @Override
        public void removeObserver(final it.unibo.samplejavafx.mvc.model.chessboard.BoardObserver observer) {
            // Not needed for these tests
        }

        boolean wasAdded(final Point2D pos, final Entity entity) {
            return entity.equals(addedEntities.get(pos));
        }

        boolean wasRemoved(final Point2D pos) {
            return removedEntities.containsKey(pos);
        }

        boolean removeWasCalled(final Point2D pos) {
            return removeCalled.getOrDefault(pos, false);
        }

        void clear() {
            addedEntities.clear();
            removedEntities.clear();
            removeCalled.clear();
        }
    }
}
