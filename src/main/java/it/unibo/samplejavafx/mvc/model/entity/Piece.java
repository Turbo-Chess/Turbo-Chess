package it.unibo.samplejavafx.mvc.model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import it.unibo.samplejavafx.mvc.model.loader.JsonViews;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.movement.MoveRules;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Represents a playable game piece with its move rules and specific values.
 * This entity tracks the piece's state (if it has moved, and its weight)
 * and calculates the cells in which he could move to, based on its specific {@link MoveRules}
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class Piece extends AbstractEntity implements Moveable {
    @JsonView(JsonViews.FirstLoading.class)
    private final int weight;
    @JsonView(JsonViews.FirstLoading.class)
    private final PieceType type;
    @JsonView(JsonViews.FullLoading.class)
    private boolean hasMoved;
    // Available cells will be moved to its own cache class
    @Deprecated
    @JsonIgnore
    private final transient List<Point2D> availableCells = new ArrayList<>();
    @JsonDeserialize(contentAs = MoveRulesImpl.class)
    @JsonView(JsonViews.FirstLoading.class)
    private final List<MoveRules> moveRules;

    /**
     * Constructs a new piece with the following properties.
     *
     * @param id unique identifier for the piece.
     * @param name the name of the piece displayed.
     * @param gameId the id that the class will get at runtime.
     * @param path file path with the image of the piece.
     * @param playerColor color of the player owning this piece.
     * @param weight positive int value that represents the importance (and score) value of the piece.
     * @param type type of the piece
     * @param moveRules non-null list of rules defining how the piece can move.
     */
    @JsonCreator
    public Piece(
            @JsonProperty("id") final String id,
            @JsonProperty("name") final String name,
            @JsonProperty("gameId") final int gameId,
            @JsonProperty("path") final String path,
            @JsonProperty("playerColor") final PlayerColor playerColor,
            @JsonProperty("weight") final int weight,
            @JsonProperty("type") final PieceType type,
            @JsonProperty("moveRules") final List<MoveRules> moveRules
    ) {
        super(id, name, gameId, path, playerColor);
        this.moveRules = List.copyOf(moveRules);
        this.weight = weight;
        this.type = type;
        this.hasMoved = false;

    }

    /**
     * Calculates and returns the possible coordinates for the piece to move.
     *
     * @param board  placeholder
     * @param start  placeholder
     * @return a non-null unmodifiable {@link List} of {@link Point2D} coordinates. It could be empty if no moves are possible.
     */
    @Override
    public List<Point2D> getValidMoves(final Point2D start, final ChessBoard board) {
        availableCells.clear();
        for (final var rule : moveRules) {
            this.availableCells.addAll(rule.getValidMoves(start, board, this.getPlayerColor()));
        }
        return Collections.unmodifiableList(availableCells);
    }

    /**
     * placeholder.
     *
     * @return placeholder.
     */
    @Override
    public Optional<Moveable> asMoveable() {
        return Optional.of(this);
    }

    /**
     * Returns the weight of the piece.
     * The weight represents the importance of a piece.
     * It's used to determine which custom piece can substitute it, and it's fundamental to calculate
     * points during a match.
     *
     * @return a positive int representing the weight of the piece.
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Returns if the piece has moved at least one time from the beginning of the match.
     * Useful for implementing advanced rules like castling and for some movement pattern
     * (like pawn's ability to move 2 cells the first time).
     *
     * @return {@code true} if it has moved at least one time, {@code false} otherwise.
     */
    public boolean hasMoved() {
        return hasMoved;
    }

    /**
     * Sets the value {@code hasMoved} to true.
     * Note: this operation is irreversible for the lifetime of the piece instance.
     */
    public void setHasMoved() {
        this.hasMoved = true;
    }

    /**
     * placeholder.
     *
     * @return placeholder.
     */
    @Override
    public List<Point2D> getAvailableCells() {
        return List.copyOf(this.availableCells);
    }
}
