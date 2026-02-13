package it.unibo.samplejavafx.mvc.model.handler;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl.MoveStrategy;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl.MoveType;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import it.unibo.samplejavafx.mvc.model.rules.AdvancedRules;

/**
 * Placeholder.
 */
public class TurnHandlerImpl {
    private int turn;
    private ChessBoard board;
    private GameState state;
    private PlayerColor currentColor;
    private Optional<Piece> currentPiece;
    private List<Point2D> pieceMoves;

    public TurnHandlerImpl(final int turn, final ChessBoard board) {
        this.turn = turn;
        this.board = board;
        this.currentColor = PlayerColor.WHITE;
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
        if (board.isFree(pos) && pieceMoves.contains(pos)) {
            executeTurn(MoveType.MOVE_ONLY, pos);
            return List.of(pos);
        }
        if (!board.isFree(pos) && board.getEntity(pos).get().getPlayerColor().equals(currentColor)) {
            var newPiece = (Piece) board.getEntity(pos).get();
            this.currentPiece = Optional.of(newPiece);
            this.pieceMoves = newPiece.getValidMoves(pos, board);
            return this.pieceMoves;
        }
        if (!board.isFree(pos)
            && board.getEntity(pos).get().getPlayerColor().equals(AdvancedRules.swapColor(currentColor))
            && currentPiece.isPresent()) {
            if (pieceMoves.contains(pos)) {
                executeTurn(MoveType.MOVE_AND_EAT, pos);
                return List.of(pos);
            }
        }
        this.currentPiece = Optional.empty();
        this.pieceMoves = Collections.emptyList();
        return this.pieceMoves;
    }

    /**
     *  placeholder.
     *
     * @param moveAction placeholder.
     * @param target placeholder.
     */
    public void executeTurn(final MoveType moveAction, final Point2D target) {
        switch (moveAction) {
            case MOVE_ONLY:

                break;
            case MOVE_AND_EAT:

                break;
            default:

                break;
        }
    }
}
