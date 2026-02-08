package it.unibo.samplejavafx.mvc.model.point2d;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Point2D(@JsonProperty("x") int x, @JsonProperty("y") int y) {

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

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}