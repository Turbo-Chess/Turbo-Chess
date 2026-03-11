package it.unibo.samplejavafx.mvc.model.score;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

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

    private void updateScore(final PlayerColor player, final int delta) {
        if (player != null) {
            final int newScore = scores.merge(player, delta, Integer::sum);
            notifyScoreChanged(player, newScore);
        }
    }

    private void notifyScoreChanged(final PlayerColor player, final int newScore) {
        for (final ScoreObserver observer : observers) {
            observer.onScoreChanged(player, newScore);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEntityAdded(final Point2D pos, final Entity entity) {
        updateScore(entity.getPlayerColor(), entity.getWeight());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEntityRemoved(final Point2D pos, final Entity entity) {
        updateScore(entity.getPlayerColor(), -entity.getWeight());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEntityMoved(final Point2D from, final Point2D to, final Entity entity) {
        // No score update needed for movement.
    }
}
