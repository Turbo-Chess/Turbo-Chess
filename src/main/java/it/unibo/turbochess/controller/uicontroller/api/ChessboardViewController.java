package it.unibo.turbochess.controller.uicontroller.api;

import it.unibo.turbochess.model.chessboard.board.api.ChessBoard;
import it.unibo.turbochess.model.point2d.Point2D;

/**
 * The {@code ChessboardViewController} interface defines the contact for the UI controller responsible for
 * managing the visual representation of the chess board.
 *
 * <p>
 * It provides methods to update the board's appearance based on game state changes, such as moving pieces,
 * highlighting valid moves, and visualizing captures.
 * </p>
 */
public interface ChessboardViewController {
    /**
     * Rebuilds or refreshes the chessboard UI to reflect the current state of the model.
     *
     * @param board the {@link ChessBoard} model to render
     */
    void refreshBoardView(ChessBoard board);

    /**
     * Visually indicates a piece movement from a starting cell to an ending cell.
     *
     * @param start The starting {@link Point2D} coordinate.
     * @param end   The destination {@link Point2D} coordinate.
     */
    void highlightMovement(Point2D start, Point2D end);

    /**
     * Visually indicates a capture (eat) event on the board.
     *
     * @param start The starting {@link Point2D} coordinate of the attacking piece.
     * @param end   The {@link Point2D} coordinate where the capture occurred.
     */
    void highlightEat(Point2D start, Point2D end);
}
