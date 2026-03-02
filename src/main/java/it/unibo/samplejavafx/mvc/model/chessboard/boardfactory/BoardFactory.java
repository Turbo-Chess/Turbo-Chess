package it.unibo.samplejavafx.mvc.model.chessboard.boardfactory;

import it.unibo.samplejavafx.mvc.model.chessboard.BoardObserver;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PieceDefinition;
import it.unibo.samplejavafx.mvc.model.loadout.Loadout;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

/**
 * placeholder.
 */
public interface BoardFactory {
    /**
     * placeholder.
     *
     * @param whiteLoadout  placeholder.
     * @param blackLoadout  plcaholder.
     * @param boardObserver plcaholder.
     * @return placeholder.
     */
    ChessBoard createPopulatedChessboard(Loadout whiteLoadout,
                                         Loadout blackLoadout,
                                         BoardObserver boardObserver);


    /**
     * placeholder.
     *
     * @param pos placeholder.
     * @param board placeholder.
     * @param pieceDefinition placeholder.
     */
    void createNewPiece(Point2D pos, ChessBoard board, PieceDefinition pieceDefinition);
}

