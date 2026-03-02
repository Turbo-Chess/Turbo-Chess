package it.unibo.samplejavafx.mvc.model.movement;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

import java.util.List;

/**
 * placeholder.
 */
public final class SteppingMovement implements MovementStrategy {
    @Override
    public List<Point2D> calculateMoves(final Point2D start, final Point2D direction, final ChessBoard board) {
        // Extract the "atomic" movement to perform at each step
        final Point2D xDir = new Point2D(Integer.signum(direction.x()), 0);
        final Point2D yDir = new Point2D(0, Integer.signum(direction.y()));
        Point2D newPoint = new Point2D(start.x(), start.y());
        final int totalSteps = Math.abs(direction.x()) + Math.abs(direction.y());
        int currentStep = 0;

        for (int i = 0; i < Math.abs(direction.x()); i++) {
            newPoint = newPoint.sum(xDir);
            currentStep++;
            if (currentStep < totalSteps && board.getEntity(newPoint).isPresent()) {
                return List.of();
            }
        }

        for (int i = 0; i < Math.abs(direction.y()); i++) {
            newPoint = newPoint.sum(yDir);
            currentStep++;
            if (currentStep < totalSteps && board.getEntity(newPoint).isPresent()) {
                return List.of();
            }
        }

        return List.of(newPoint);
    }
}
