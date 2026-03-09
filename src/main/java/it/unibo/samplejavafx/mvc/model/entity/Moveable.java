package it.unibo.samplejavafx.mvc.model.entity;

import java.util.List;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

/**
 * The {@code Moveable} interface defines the behavior for game entities that are capable of changing their position
 * on the game board. It provides methods to calculate valid destinations and track movement history.
 * <p>
 * Implementing classes are expected to adhere to the game's movement rules, ensuring that all returned moves are valid
 * within the context of the current board state.
 * </p>
 */
public interface Moveable {

    /**
     * Calculates and returns the set of all possible coordinates to which the piece can legally move
     * from a given starting position.
     * <p>
     * This calculation takes into account the geometry of the board, the presence of other pieces,
     * and the specific movement patterns defined for the entity.
     * </p>
     *
     * @param board  the current state of the {@link ChessBoard}, providing context for the next move validation.
     * @param start  the {@link Point2D} representing the current position of the entity.
     * @return a non-null, unmodifiable {@link List} of {@link Point2D} coordinates representing valid moves.
     *         The list is empty if no legal moves are available.
     */
    List<Point2D> getValidMoves(Point2D start, ChessBoard board);

    /**
     * Updates the internal state of the entity to indicate that it has performed a move.
     * This state change is often relevant for rules concerning initial moves (e.g., pawn double step, castling).
     */
    void setHasMoved();

    /**
     * Checks whether the entity has already moved at least once in the current game.
     *
     * @return {@code true} if the entity has moved previously, {@code false} otherwise.
     */
    boolean hasMoved();
}
