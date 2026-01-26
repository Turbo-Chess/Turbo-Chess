package it.unibo.samplejavafx.mvc.model.entity;

import ch.qos.logback.core.pattern.parser.OptionTokenizer;
import lombok.Getter;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

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
    AbstractEntity(final String id, final String name, final int gameId, final Path path, final PlayerColor playerColor) {
        this.id = id;
        this.name = name;
        this.gameId = gameId;
        this.imagePath = path;
        this.playerColor = playerColor;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEntity that = (AbstractEntity) o;
        return gameId == that.gameId && Objects.equals(id, that.id) && playerColor == that.playerColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, gameId, playerColor);
    }

//    /**
//     * Returns the id of the entity.
//     *
//     * @return a non-null {@link String} representing the identifier of the entity.
//     */
//    public String getId() {
//        return this.id;
//    }
//
//    /**
//     * Returns the name of the entity.
//     *
//     * @return a non-null {@link String} representing the name of the entity.
//     */
//    public String name() {
//        return this.name;
//    }
//
//    /**
//     * Returns a boolean tracking the color of the entity.
//     * Note: its value can't be changed after instantiation.
//     *
//     * @return {@code true} if it's white, {@code false otherwise}.
//     */
//    public boolean isWhite() {
//        return this.isWhite;
//    }
//
//    /**
//     * Returns the path of the image.
//     *
//     * @return a non-null path.
//     */
//    public Path getImagePath() {
//        return this.imagePath;
//    }
}
