// CHECKSTYLE: MagicNumber OFF

package it.unibo.turbochess.model.score;

import static org.junit.jupiter.api.Assertions.assertEquals;

import it.unibo.turbochess.model.score.api.ScoreManager;
import it.unibo.turbochess.model.score.impl.ScoreManagerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.turbochess.model.entity.impl.PlayerColor;

import it.unibo.turbochess.model.entity.api.Entity;
import it.unibo.turbochess.model.entity.impl.PieceType;
import it.unibo.turbochess.model.point2d.Point2D;
import java.util.Optional;
import it.unibo.turbochess.model.entity.api.Moveable;

class ScoreManagerTest {

    private static final String TEST_STRING = "test";
    private ScoreManager scoreManager;

    private Entity createMockEntity(final PlayerColor color, final int weight) {
        return new Entity() {
            @Override
            public String getId() {
                return TEST_STRING;
            }

            @Override
            public int getGameId() {
                return 0;
            }

            @Override
            public String getName() {
                return TEST_STRING;
            }

            @Override
            public String getImagePath() {
                return TEST_STRING;
            }

            @Override
            public PieceType getType() {
                return PieceType.PAWN;
            }

            @Override
            public PlayerColor getPlayerColor() {
                return color;
            }

            @Override
            public int getWeight() {
                return weight;
            }

            @Override
            public Optional<Moveable> asMoveable() {
                return Optional.empty();
            }
        };
    }

    @BeforeEach
    void setUp() {
        scoreManager = new ScoreManagerImpl();
    }

    @Test
    void testInitialScore() {
        assertEquals(0, scoreManager.getScore(PlayerColor.WHITE));
        assertEquals(0, scoreManager.getScore(PlayerColor.BLACK));
    }

    @Test
    void testAddPoints() {
        scoreManager.onEntityAdded(new Point2D(0, 0), createMockEntity(PlayerColor.WHITE, 100));
        assertEquals(100, scoreManager.getScore(PlayerColor.WHITE));

        scoreManager.onEntityAdded(new Point2D(0, 1), createMockEntity(PlayerColor.WHITE, 900));
        assertEquals(1000, scoreManager.getScore(PlayerColor.WHITE));
    }

    @Test
    void testRemovePoints() {
        scoreManager.onEntityAdded(new Point2D(0, 0), createMockEntity(PlayerColor.WHITE, 1000));
        scoreManager.onEntityRemoved(new Point2D(0, 0), createMockEntity(PlayerColor.WHITE, 300));
        assertEquals(700, scoreManager.getScore(PlayerColor.WHITE));
    }

    @Test
    void testReset() {
        scoreManager.onEntityAdded(new Point2D(0, 0), createMockEntity(PlayerColor.WHITE, 100));
        scoreManager.reset();
        assertEquals(0, scoreManager.getScore(PlayerColor.WHITE));
    }

    @Test
    void testObserver() {
        final int[] lastScore = {0};
        scoreManager.addObserver((player, newScore) -> {
            if (player == PlayerColor.WHITE) {
                lastScore[0] = newScore;
            }
        });

        scoreManager.onEntityAdded(new Point2D(0, 0), createMockEntity(PlayerColor.WHITE, 150));
        assertEquals(150, lastScore[0]);
    }
}

// CHECKSTYLE: MagicNumber ON
