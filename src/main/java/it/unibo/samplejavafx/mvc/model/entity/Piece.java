package it.unibo.samplejavafx.mvc.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PieceDefinition;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Represents a specific game piece on the board, extending the generic {@link AbstractEntity}.
 * This class encapsulates the state and behavior of pieces such as Pawns, Kings, or custom pieces.
 * It implements the {@link Moveable} interface to provide movement logic based on the piece's definition
 * and the current state of the board.
 *
 * <p>
 * This class is designed to be immutable with respect to its definition, but maintains state for
 * gameplay mechanics like movement history.
 * </p>
 */
@Getter
@ToString
@JsonDeserialize(builder = Piece.Builder.class)
public class Piece extends AbstractEntity<PieceDefinition> implements Moveable {
    private boolean hasMoved;
    @Deprecated
    private final List<Point2D> availableCells = new ArrayList<>();

     /**
      * Constructs a new {@code Piece} instance using the provided builder configuration.
      *
      * @param builder The {@link Builder} containing initialization parameters for the piece.
      */
     protected Piece(final Builder builder) {
        super(builder);
        this.hasMoved = builder.hasMoved;
    }


    /**
     * {@inheritDoc}
     *
     * <p>
     * Calculates valid moves by delegating to the movement rules defined in the {@link PieceDefinition}.
     * It considers the piece's current position, board state, and player color.
     * </p>
     */
    @Override
    public final List<Point2D> getValidMoves(final Point2D start, final ChessBoard board) {
        this.availableCells.clear();
        final List<Point2D> res = new ArrayList<>();
        for (final var movement : super.getEntityDefinition().getMoveRules()) {
            res.addAll(movement.getValidMoves(start, board, super.getPlayerColor()));
            // This is maintained for now for compatibility, well be removed as well as "availableCells"
            availableCells.addAll(res);
        }
        return Collections.unmodifiableList(res);
    }


    /**
     * {@inheritDoc}
     * 
     * <p>
     * Returns a cached list of available cells from the last validation.
     * Note: This method accesses a deprecated field and may be subject to removal in future versions.
     * </p>
     * 
     */
    @Override
    @JsonIgnore
    public final List<Point2D> getAvailableCells() {
        return Collections.unmodifiableList(this.availableCells);
    }


    /**
     * {@inheritDoc}
     *
     * @return an {@link Optional} containing this piece instance, confirming it is a moveable entity.
     */
    @Override
    public final Optional<Moveable> asMoveable() {
        return Optional.of(this);
    }

    /**
     * Checks if the piece has moved at least once during the game.
     * This state is crucial for special moves like Castling or Pawn initial double-step.
     *
     * @return {@code true} if the piece has moved, {@code false} otherwise.
     */
    @Override
    public boolean hasMoved() {
        return this.hasMoved;
    }

    /**
     * Retrieves the weight value of the piece.
     * This value is used for {@code Loadout} validation
     *
     * @return an integer representing the relative positive value or weight of this piece.
     */
    @JsonIgnore
    public int getWeight() {
        return super.getEntityDefinition().getWeight();
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Marks the piece as having moved. This action is irreversible for the duration of the match
     * and affects the availability of certain moves.
     * </p>
     */
    @Override
    public void setHasMoved() {
        this.hasMoved = true;
    }

    /**
     * A builder class for creating instances of {@link Piece}.
     * Follows the standard builder pattern to allow flexible object construction.
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder extends AbstractEntity.AbstractBuilder<PieceDefinition, Builder> {
        @JsonProperty("hasMoved")
        private boolean hasMoved;

        /**
         * Sets the initial movement state of the piece.
         * Useful when loading a game from a save state where a piece might have already moved.
         *
         * @param newHasMoved {@code true} if the piece has already moved, {@code false} otherwise.
         * @return this builder instance.
         */
        public Builder hasMoved(final boolean newHasMoved) {
            this.hasMoved = newHasMoved;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Builder self() {
            return this;
        }

        /**
         * {@inheritDoc}
         *
         * <p>
         * Creates a new immutable {@link Piece} object based on the current builder state.
         * </p>
         */
        @Override
        public Piece build() {
            return new Piece(this);
        }
    }
}
