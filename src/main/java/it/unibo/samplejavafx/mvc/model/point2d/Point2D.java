package it.unibo.samplejavafx.mvc.model.point2d;

/**
 * A record that represents a point in a 2D Cartesian system.
 * Used to give a position to an existing piece on the board.
 * By convention, the values are 0-indexed, where the top-left corner is the (0, 0) point.
 * Points can also be used as "movement vectors" to represent the direction of the movement of a piece.
 *
 * @param x the horizontal coordinate.
 * @param y the vertical coordinate.
 */
public record Point2D(int x, int y) {
    /**
     *  placeholder.
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
     * @retur placeholdern.
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
