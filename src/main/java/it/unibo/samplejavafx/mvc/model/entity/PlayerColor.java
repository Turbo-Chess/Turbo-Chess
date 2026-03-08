package it.unibo.samplejavafx.mvc.model.entity;

/**
 * Enumerates the standard colors assigned to players within the game.
 * These values distinguish the two opposing sides in a chess match and are used for
 * turn management, piece ownership, and board orientation.
 */
public enum PlayerColor {
    /**
     * Represents the White player.
     * Traditionally moves first and starts at the bottom of the board (ranks 1 and 2).
     */
    WHITE,

    /**
     * Represents the Black player.
     * Moves second and starts at the top of the board (ranks 7 and 8).
     */
    BLACK
}
