package it.unibo.samplejavafx.mvc.model.entity;

import java.util.Optional;

/**
 * Entity is the class that defines what can be on the board.
 */
public interface Entity {
    /**
     * placeholder.
     *
     * @return placeholder.
     */
    String getId();

    /**
     * placeholder.
     *
     * @return placeholder.
     */
    String getName();

    /**
     * placeholder.
     *
     * @return placeholder.
     */
    String getImagePath();

    /**
     * placeholder.
     *
     * @return placeholder.
     */
    PieceType getType();

    /**
     *  placeholder.
     *
     * @return placeholder.
     */
    PlayerColor getPlayerColor();

    /**
     * placeholder.
     *
     * @return placeholder.
     */
    default Optional<Moveable> asMoveable() {
        return Optional.empty();
    }
}
