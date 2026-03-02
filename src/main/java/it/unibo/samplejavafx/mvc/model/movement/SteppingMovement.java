package it.unibo.samplejavafx.mvc.model.movement;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

import java.util.List;

public class SteppingMovement implements MovementStrategy {
    @Override
    public List<Point2D> calculateMoves(Point2D start, Point2D direction, ChessBoard board) {
        // Extract the "atomic" movement to perform at each step
        final Point2D xDir = new Point2D(Integer.signum(direction.x()), 0);
        final Point2D yDir = new Point2D(0, Integer.signum(direction.y()));
        Point2D newPoint = new Point2D(start.x(), start.y());
        for (int i = 0; i < Math.abs(direction.x()); i++) {
            newPoint = newPoint.sum(xDir);
            if (board.getEntity(newPoint).isPresent()) {
                return List.of();
            }
        }

        for (int i = 0; i < Math.abs(direction.y()); i++) {
            newPoint = newPoint.sum(yDir);
            if (!board.getEntity(newPoint).isEmpty()) {
                return List.of();
            }
        }

        return List.of(newPoint);
    }
}
