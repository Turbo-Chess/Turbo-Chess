package it.unibo.samplejavafx.mvc.model.entity;

import java.util.Optional;

/**
 * Entity is the class that defines what can be on the board.
 */
@FunctionalInterface
public interface Entity {
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
