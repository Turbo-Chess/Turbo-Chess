package it.unibo.samplejavafx.mvc.model.point2d;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Immutable 2D point expressed with integer coordinates.
 *
 * <p>
 * Used throughout the model to represent board coordinates. Coordinates are 0-based, where {@code x}
 * typically represents the file (column) and {@code y} the rank (row).
 * </p>
 *
 * @param x the horizontal coordinate.
 * @param y the vertical coordinate.
 */
public record Point2D(@JsonProperty("x") int x, @JsonProperty("y") int y) {

    /**
     * Returns the component-wise sum of this point and the given one.
     *
     * @param p the point to add.
     * @return a new {@link Point2D} equal to {@code this + p}.
     */
    public Point2D sum(final Point2D p) {
        return new Point2D(this.x() + p.x(), this.y() + p.y());
    }

    /**
     * Returns this point scaled by the given scalar.
     *
     * @param n the scalar multiplier.
     * @return a new {@link Point2D} equal to {@code this * n}.
     */
    public Point2D multiply(final int n) {
        return new Point2D(this.x() * n, this.y() * n);
    }

    /**
     * Returns a new point with the {@code x} component negated.
     *
     * @return a new {@link Point2D} with {@code x} inverted.
     */
    public Point2D invertX() {
        return new Point2D(-this.x(), this.y());
    }

    /**
     * Returns a new point with the {@code y} component negated.
     *
     * @return a new {@link Point2D} with {@code y} inverted.
     */
    public Point2D invertY() {
        return new Point2D(this.x(), -this.y());
    }

    /**
     * Flips the {@code y} coordinate within a board of the given height.
     *
     * <p>
     * This maps {@code y} to {@code (boardHeight - 1 - y)}.
     * </p>
     *
     * @param boardHeight the total height of the board.
     * @return a new {@link Point2D} with {@code y} flipped.
     */
    public Point2D flipY(final int boardHeight) {
        return new Point2D(this.x(), boardHeight - 1 - this.y());
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
