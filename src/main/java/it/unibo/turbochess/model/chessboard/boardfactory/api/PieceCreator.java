package it.unibo.turbochess.model.chessboard.boardfactory.api;

import it.unibo.turbochess.model.chessboard.board.api.ChessBoard;
import it.unibo.turbochess.model.entity.impl.PlayerColor;
import it.unibo.turbochess.model.point2d.Point2D;

/**
 * Functional interface for creating pieces.
 */
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
     * @param packId          The ID of the resource pack containing the piece definition.
     * @param pieceId         The ID of the piece definition within the pack.
     * @param color           The {@link PlayerColor} of the player who owns the new piece.
     */
    void createNewPiece(Point2D pos,
                        ChessBoard board,
                        String packId,
                        String pieceId,
                        PlayerColor color);
}
