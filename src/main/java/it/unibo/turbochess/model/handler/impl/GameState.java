package it.unibo.turbochess.model.handler.impl;

/**
 * Values given to the current game-state in order to apply rules.
 */
public enum GameState {
    NORMAL,
    CHECK,
    DOUBLE_CHECK,
    CHECKMATE,
    DRAW,
    PROMOTION,
    TIMEOUT
}
