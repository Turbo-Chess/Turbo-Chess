package it.unibo.samplejavafx.mvc.model.entity;

import lombok.Getter;

import java.nio.file.Path;

/**
 * Abstract Entity is the abstract class that implements {@link Entity} and defines the shared behavior of the
 * two main entities of the game.
 */
@Getter
public abstract class AbstractEntity implements Entity {
    private final String id;
    private final String name;
    private final Path imagePath;
    private final PlayerColor playerColor;

    /**
     * Base constructor shared with every entity.
     *
     * @param id            unique identifier of the entity.
     * @param name          display name of the entity.
     * @param path          the path containing the image resource to display.
     * @param playerColor    color of the player owning this entity.
     */
    AbstractEntity(final String id, final String name, final Path path, final PlayerColor playerColor) {
        this.id = id;
        this.name = name;
        this.imagePath = path;
        this.playerColor = playerColor;
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
