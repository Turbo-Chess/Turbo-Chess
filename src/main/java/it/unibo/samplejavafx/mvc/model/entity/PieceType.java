package it.unibo.samplejavafx.mvc.model.entity;

/**
 * Enumerates the standard and special classification types for game pieces and entities.
 * These types are central to game logic, affecting movement rules, victory conditions,
 * and interaction mechanics.
 */
public enum PieceType {
    /**
     * Represents the standard Pawn piece.
     * Characterized by forward movement and specific capture rules.
     */
    PAWN,

    /**
     * Represents a generic inferior piece class.
     * Used for minor pieces like Bishops and Knights in standard chess,
     * or other low-value units in variants.
     */
    INFERIOR,

    /**
     * Represents a generic superior piece class.
     * used for major pieces like Queens in standard chess.
     */
    SUPERIOR,

    /**
     * Represents a Rook.
     * Often associated with castling mechanics and straight-line movement.
     */
    TOWER,

    /**
     * Represents the King piece.
     * The primary objective of the game usually revolves around protecting or capturing this piece.
     */
    KING,

    /**
     * Represents a Power-up entity.
     * These are special items on the board that grant effects rather than behaving like traditional pieces.
     */
    POWERUP
}
