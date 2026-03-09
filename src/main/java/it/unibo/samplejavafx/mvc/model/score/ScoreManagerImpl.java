package it.unibo.samplejavafx.mvc.model.score;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;

/**
 * Implementation of ScoreManager.
 */
public final class ScoreManagerImpl implements ScoreManager {

    private final Map<PlayerColor, Integer> scores = new EnumMap<>(PlayerColor.class);
    private final List<ScoreObserver> observers = new ArrayList<>();

    /**
     * Constructs a new ScoreManagerImpl with scores set to zero.
     */
    public ScoreManagerImpl() {
        reset();
    }

    @Override
    public int getScore(final PlayerColor player) {
        return scores.getOrDefault(player, 0);
    }

    @Override
    public void reset() {
        for (final PlayerColor player : PlayerColor.values()) {
            scores.put(player, 0);
            notifyScoreChanged(player, 0);
        }
    }

    @Override
    public void addObserver(final ScoreObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(final ScoreObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void addPoints(final PlayerColor player, final int points) {
        updateScore(player, points);
    }

    @Override
    public void removePoints(final PlayerColor player, final int points) {
        updateScore(player, -points);
    }

    private void updateScore(final PlayerColor player, final int delta) {
        if (player == null) {
            return;
        }
        final int currentScore = scores.getOrDefault(player, 0);
        final int newScore = currentScore + delta;
        scores.put(player, newScore);
        notifyScoreChanged(player, newScore);
    }

    private void notifyScoreChanged(final PlayerColor player, final int newScore) {
        for (final ScoreObserver observer : observers) {
            observer.onScoreChanged(player, newScore);
        }
    }
}
