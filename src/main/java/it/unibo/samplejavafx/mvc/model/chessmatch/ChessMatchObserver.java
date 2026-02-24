package it.unibo.samplejavafx.mvc.model.chessmatch;

import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.handler.GameState;


public interface ChessMatchObserver {
    void onTurnUpdated(final int turnNumber);

    void onPlayerUpdated(final PlayerColor playerColor);

    void onGameStateUpdated(final GameState gameState);

    // TODO: add for the timer
    //void onTimerUpdated(final PlayerColor playerColor);
}
