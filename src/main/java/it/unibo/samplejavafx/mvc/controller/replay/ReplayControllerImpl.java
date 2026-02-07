package it.unibo.samplejavafx.mvc.controller.replay;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.replay.DespawnEvent;
import it.unibo.samplejavafx.mvc.model.replay.GameEvent;
import it.unibo.samplejavafx.mvc.model.replay.GameHistory;
import it.unibo.samplejavafx.mvc.model.replay.MoveEvent;
import it.unibo.samplejavafx.mvc.model.replay.SpawnEvent;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Implementation of ReplayController.
 */
public final class ReplayControllerImpl implements ReplayController {

    private final ChessBoard board;
    private GameHistory history;
    private int currentIndex;

    /**
     * @param board the board to control during playback.
     */
    public ReplayControllerImpl(final ChessBoard board) {
        this.board = board;
        this.history = new GameHistory();
        this.currentIndex = 0;
    }

    /**
     * Loads a new history into the controller.
     *
     * @param newHistory the new history to load.
     */
    @Override
    public void loadHistory(final GameHistory newHistory) {
        this.history = new GameHistory();
        this.history.setEvents(newHistory.getEvents());
        this.currentIndex = 0;
    }

    @Override
    public boolean next() {
        if (currentIndex >= history.getEvents().size()) {
            return false;
        }

        final GameEvent event = history.getEvents().get(currentIndex);
        applyEvent(event);
        currentIndex++;
        return true;
    }

    @Override
    public boolean prev() {
        if (currentIndex <= 0) {
            return false;
        }

        currentIndex--;
        final GameEvent event = history.getEvents().get(currentIndex);
        revertEvent(event);
        return true;
    }

    @Override
    public void jumpToStart() {
        boolean success = prev();
        while (success) {
            success = prev();
        }
    }

    @Override
    public void jumpToEnd() {
        boolean success = next();
        while (success) {
            success = next();
        }
    }

    @Override
    public int getCurrentIndex() {
        return currentIndex;
    }

    @Override
    public int getTotalEvents() {
        return history.getEvents().size();
    }

    private void applyEvent(final GameEvent event) {
        if (event instanceof MoveEvent move) {
            board.removeEntity(move.from());
            board.setEntity(move.to(), move.entity());
        } else if (event instanceof SpawnEvent spawn) {
            board.setEntity(spawn.position(), spawn.entity());
        } else if (event instanceof DespawnEvent despawn) {
            board.removeEntity(despawn.position());
        }
    }

    private void revertEvent(final GameEvent event) {
        if (event instanceof MoveEvent move) {
            board.removeEntity(move.to());
            board.setEntity(move.from(), move.entity());
        } else if (event instanceof SpawnEvent spawn) {
            board.removeEntity(spawn.position());
        } else if (event instanceof DespawnEvent despawn) {
            board.setEntity(despawn.position(), despawn.entity());
        }
    }
}
