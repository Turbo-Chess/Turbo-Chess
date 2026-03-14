package it.unibo.samplejavafx.mvc.model.chessboard.boardfactory;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

@FunctionalInterface
public interface PieceCreator {
    /**
     * Creates and places a new piece on an existing board at the specified position.
     *
     * <p>
     * This method is typically used for game mechanics that introduce new pieces during play,
     * such as pawn promotion.
     * </p>
     *
     * @param pos             The target {@link Point2D} position for the new piece.
     * @param board           The {@link ChessBoard} on which to place the piece.
     * @param color           The {@link PlayerColor} of the player who owns the new piece.
     */
    void createNewPiece(Point2D pos,
                        ChessBoard board,
                        String packId,
                        String pieceId,
                        PlayerColor color);
}
