package it.unibo.samplejavafx.mvc.model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import it.unibo.samplejavafx.mvc.model.Loader.JsonViews;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import java.util.Objects;

/**
 * Abstract Entity is the abstract class that implements {@link Entity} and defines the shared behavior of the
 * two main entities of the game.
 */
@Getter
@EqualsAndHashCode
public abstract class AbstractEntity implements Entity {
    @JsonView(JsonViews.FirstLoading.class)
    private final String id;
    @JsonView(JsonViews.FirstLoading.class)
    private final String name;
    @JsonView(JsonViews.FirstLoading.class)
    private final String imagePath;
    @JsonView(JsonViews.FullLoading.class)
    private final int gameId;
    @JsonView(JsonViews.FullLoading.class)
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
    @JsonCreator
    AbstractEntity(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("gameId") int gameId,
            @JsonProperty("path") String path,
            @JsonProperty("playerColor") PlayerColor playerColor
    ) {
        this.id = id;
        this.name = name;
        this.gameId = gameId;
        this.imagePath = path;
        this.playerColor = (playerColor == null) ? PlayerColor.NONE : playerColor;
    }
}
