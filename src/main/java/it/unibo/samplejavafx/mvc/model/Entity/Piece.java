package it.unibo.samplejavafx.mvc.model.Entity;

import it.unibo.samplejavafx.mvc.model.Movement.MoveRules;
import it.unibo.samplejavafx.mvc.model.Point2D.Point2D;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a playable game piece with its move rules and specific values.
 * This entity tracks the piece's state (if it has moved, and its weight)
 * and calculates the cells in which he could move to, based on its specific {@link MoveRules}
 */
public class Piece extends AbstractEntity {
    private final int weight;
    private boolean hasMoved;
    private final List<Point2D> avaliableCells = new ArrayList<>();
    private final List<MoveRules> moveRules;

    /**
     * Constructs a new piece with the following properties.
     *
     * @param id unique identifier for the piece.
     * @param name the name of the piece displayed.
     * @param path file path with the image of the piece.
     * @param whiteColor {@code true} if the piece belongs to the white player.
     * @param weight positive int value that represents the importance (and score) value of the piece.
     * @param moveRules non-null list of rules defining how the piece can move.
     */
    Piece(final String id, final String name, final Path path, final boolean whiteColor,
          final int weight, final List<MoveRules> moveRules) {
        super(id, name, path, whiteColor);
        this.moveRules = moveRules;
        this.weight = weight;
        this.hasMoved = false;
    }

    /**
     * Calculates and returns the possible coordinates for the piece to move.
     *
     * @return a non-null unmodifiable {@link List} of {@link Point2D} coordinates. It could be empty if no moves are possible.
     */
    public List<Point2D> getValidMoves() {
        // TO-DO: Piece logic needs implementation
        return Collections.unmodifiableList(avaliableCells);
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
}
