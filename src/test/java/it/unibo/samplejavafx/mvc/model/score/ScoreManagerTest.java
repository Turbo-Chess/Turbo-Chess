package it.unibo.samplejavafx.mvc.model.score;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;

class ScoreManagerTest {

    private ScoreManager scoreManager;

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
        scoreManager.addPoints(PlayerColor.WHITE, 100);
        assertEquals(100, scoreManager.getScore(PlayerColor.WHITE));
        
        scoreManager.addPoints(PlayerColor.WHITE, 900);
        assertEquals(1000, scoreManager.getScore(PlayerColor.WHITE));
    }

    @Test
    void testRemovePoints() {
        scoreManager.addPoints(PlayerColor.WHITE, 1000);
        scoreManager.removePoints(PlayerColor.WHITE, 300);
        assertEquals(700, scoreManager.getScore(PlayerColor.WHITE));
    }

    @Test
    void testReset() {
        scoreManager.addPoints(PlayerColor.WHITE, 100);
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
        
        scoreManager.addPoints(PlayerColor.WHITE, 150);
        assertEquals(150, lastScore[0]);
    }
}
