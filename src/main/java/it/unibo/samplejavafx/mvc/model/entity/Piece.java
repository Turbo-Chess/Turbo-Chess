package it.unibo.samplejavafx.mvc.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * Placeholder.
 */
@Getter
@ToString
@JsonDeserialize(builder = Piece.Builder.class)
public class Piece extends AbstractEntity<PieceDefinition> implements Moveable {
    private boolean hasMoved;
    @Deprecated
    private final List<Point2D> availableCells = new ArrayList<>();

     /**
      * Placeholder.
      *
      * @param builder Placeholder.
      */
     protected Piece(final Builder builder) {
        super(builder);
        this.hasMoved = builder.hasMoved;
    }


    /**
     * {@inheritDoc}
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
     */
    @Override
    @JsonIgnore
    public final List<Point2D> getAvailableCells() {
        return Collections.unmodifiableList(this.availableCells);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public final Optional<Moveable> asMoveable() {
        return Optional.of(this);
    }

    /**
     * Placeholder.
     *
     * @return Placeholder.
     */
    public boolean hasMoved() {
        return this.hasMoved;
    }

    /**
     * Placeholder.
     *
     * @return Placeholder.
     */
    @JsonIgnore
    public int getWeight() {
        return super.getEntityDefinition().getWeight();
    }

    /**
     * placeholder.
     */
    @Override
    public void setHasMoved() {
        this.hasMoved = true;
    }

    /**
     * Placeholder.
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder extends AbstractEntity.AbstractBuilder<PieceDefinition, Builder> {
        private boolean hasMoved;

        /**
         * Placeholder.
         *
         * @param newHasMoved Placeholder.
         * @return Placeholder.
         */
        public Builder setHasMoved(final boolean newHasMoved) {
            this.hasMoved = newHasMoved;
            return this;
        }


        /**
         * {@inheritDoc}
         */
        @Override
        protected Builder self() {
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Piece build() {
            return new Piece(this);
        }
    }
}
