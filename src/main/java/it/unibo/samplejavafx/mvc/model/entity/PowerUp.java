package it.unibo.samplejavafx.mvc.model.entity;

import java.nio.file.Path;

/**
 * PowerUps are special entities that applies effect on the board that can affect both players.
 * Each power up has a duration.
 */
public class PowerUp extends AbstractEntity {
    private final int duration;

    /**
     * Constructs a new power up with the following properties.
     *
     * @param id unique identifier for the piece.
     * @param name the name of the piece displayed.
     * @param gameId the id that the class will get at runtime.
     * @param path file path with the image of the piece.
     * @param playerColor color of the player owning this power up.
     * @param duration positive int value that represents the duration of the applied effect.
     */
    PowerUp(final String id, final String name, final int gameId, final String path,
            final PlayerColor playerColor, final int duration
    ) {
        super(id, name, gameId, path, playerColor);
        this.duration = duration;
    }

    /**
     * Applies the effect on the board.
     */
    public void applyEffect() {
        // TO-DO: implement method
    }

    /**
     * Return the duration of the effect.
     *
     * @return a non-negative int value representing the duration of the applied effect.
     */
    public int getDuration() {
        return this.duration;
    }
}
