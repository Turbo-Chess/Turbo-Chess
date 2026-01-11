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

}
