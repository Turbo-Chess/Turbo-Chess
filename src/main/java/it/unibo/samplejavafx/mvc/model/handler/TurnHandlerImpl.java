package it.unibo.samplejavafx.mvc.model.handler;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

/**
 * Placeholder.
 */
public class TurnHandlerImpl {
    private int turn;
    private ChessBoard board;
    private GameState state;
    private Optional<Piece> currentPiece;
    private List<Point2D> pieceMoves;

    public TurnHandlerImpl(final int turn, final ChessBoard board) {
        this.turn = turn;
        this.board = board;
    }

    /**
     * placeholder.
     * 
     * @param pos placeholder.
     * @return placeholder.
     */
    public List<Point2D> thinking(final Point2D pos) {
        if (board.isFree(pos) && currentPiece.isEmpty()) {
            return Collections.emptyList();
        }
        if (board.isFree(pos)) {
            
        }
    }

    /**
     *  placeholder.
     *
     * 
     */
    public void executeTurn() {

    }
}
