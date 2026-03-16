package it.unibo.samplejavafx.mvc.model.point2d;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a 2D point with integer coordinates.
 * This class is a record, so it's immutable, because it only carries data.
 *
 * @param x the x coordinate of the point.
 * @param y the y coordinate of the point.
 */
public record Point2D(@JsonProperty("x") int x, @JsonProperty("y") int y) {

    /**
     * Sums this point with another point.
     *
     * @param p the point to add to this one.
     * @return a new Point2D representing the sum of the two points.
     */
    public Point2D sum(final Point2D p) {
        return new Point2D(this.x() + p.x(), this.y() + p.y());
    }

    /**
     * Multiplies the coordinates of this point by a scalar.
     *
     * @param n the scalar value to multiply with.
     * @return a new Point2D with coordinates multiplied by n.
     */
    public Point2D multiply(final int n) {
        return new Point2D(this.x() * n, this.y() * n);
    }

    /**
     * Inverts the x coordinate of this point.
     *
     * @return a new Point2D with the x coordinate negated.
     */
    public Point2D invertX() {
        return new Point2D(-this.x(), this.y());
    }

    /**
     * Inverts the y coordinate of this point.
     *
     * @return a new Point2D with the y coordinate negated.
     */
    public Point2D invertY() {
        return new Point2D(this.x(), -this.y());
    }

    /**
     * Flips the y coordinate based on the board height.
     * Useful for converting between coordinate systems where the origin might be different (e.g., top-left vs bottom-left).
     *
     * @param boardHeight the height of the board.
     * @return a new Point2D with the y coordinate flipped.
     */
    public Point2D flipY(final int boardHeight) {
        return new Point2D(this.x(), boardHeight - 1 - this.y());
    }

    /**
     * Returns a string representation of the point.
     *
     * @return a string in the format "(x, y)".
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
