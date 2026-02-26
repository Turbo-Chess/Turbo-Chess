package it.unibo.samplejavafx.mvc.model.point2d;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * placeholder.
 *
 * @param x placeholder.
 * @param y placeholder.
 */
public record Point2D(@JsonProperty("x") int x, @JsonProperty("y") int y) {

    /**
     * placeholder.
     *
     * @param p placeholder.
     * @return placeholder.
     */
    public Point2D sum(final Point2D p) {
        return new Point2D(this.x() + p.x(), this.y() + p.y());
    }

    /**
     *  placeholder.
     *
     * @param n placeholder.
     * @return placeholder.
     */
    public Point2D multiply(final int n) {
        return new Point2D(this.x() * n, this.y() * n);
    }

    /**
     * p placeholder.
     *
     * @return placeholder.
     */
    public Point2D invertX() {
        return new Point2D(-this.x(), this.y());
    }

    /**
     *  placeholder.
     *
     * @return placeholder.
     */
    public Point2D invertY() {
        return new Point2D(this.x(), -this.y());
    }

    /**
     * placeholder.
     *
     * @param boardHeight placeholder.
     * @return placeholder.
     */
    public Point2D flipY(final int boardHeight) {
        return new Point2D(this.x(), boardHeight - 1 - this.y());
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
