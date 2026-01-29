package it.unibo.samplejavafx.mvc.model.entity;

import lombok.Getter;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Abstract Entity is the abstract class that implements {@link Entity} and defines the shared behavior of the
 * two main entities of the game.
 */
@Getter
public abstract class AbstractEntity implements Entity {
    private final String id;
    private final String name;
    private final int gameId;
    private final Path imagePath;
    private final PlayerColor playerColor;

    /**
     * Base constructor shared with every entity.
     *
     * @param id            unique identifier of the entity.
     * @param name          display name of the entity.
     * @param gameId        id assigned to the piece if is instantiated on the chess board. -1 if it's not.
     * @param path          the path containing the image resource to display.
     * @param playerColor    color of the player owning this entity.
     */
    public AbstractEntity(
        final String id, 
        final String name, 
        final int gameId, 
        final Path path, 
        final PlayerColor playerColor
    ) {
        this.id = id;
        this.name = name;
        this.gameId = gameId;
        this.imagePath = path;
        this.playerColor = playerColor;
    }

    /**
     * placeholder.
     *
     * @param o placeholder.
     * @return placeholder.
     */
    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AbstractEntity that = (AbstractEntity) o;
        return gameId == that.gameId && Objects.equals(id, that.id) && playerColor == that.playerColor;
    }

    /**
     * placeholder.
     *
     * @return placeholder.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, gameId, playerColor);
    }
}
