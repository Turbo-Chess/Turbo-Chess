package it.unibo.samplejavafx.mvc.model.handler.turnstates;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.handler.TurnHandlerContext;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl.MoveType;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import it.unibo.samplejavafx.mvc.model.utils.RulesUtils;

public final class CheckTurnState extends AbstractTurnState {
    private final ChessBoard board;
    private final Map<Piece, List<Point2D>> interposingPieces = new HashMap<>();
    private PlayerColor currentColor;

    public CheckTurnState(final TurnHandlerContext context) {
        super(context);
        this.board = context.getBoard();
        this.currentColor = context.getCurrentColor();
        this.interposingPieces.putAll(context.getInterposing());
    }

    /**
     * Strategy for handling thinking during a {@code CHECK}.
     * 
     * @param pos the {@link Point2D} of the chosen cell.
     * @return  a list of {@link Point2D} containing all the possible moves for a piece,
     *          returns a single {@link Point2D} of the chosen movement if the piece moves,
     *          returns an empty list if there are no avaiable moves or no owned pieces are selected. 
     */
    @Override
    public List<Point2D> thinking(final Point2D pos) {
        if (board.isFree(pos) && context.getCurrentPiece().isEmpty()) {
            return Collections.emptyList();
        }
        if (board.isFree(pos) && context.getCurrentMoves().contains(pos)) {
            return context.executeTurn(MoveType.MOVE_ONLY, pos) ? List.of(pos) : Collections.emptyList();
        }
        if (!board.isFree(pos) && board.getEntity(pos).get().getPlayerColor() == currentColor
            && board.getEntity(pos).get().getType() == PieceType.KING) {
            final var king = (Piece) board.getEntity(pos).get();
            context.setCurrentPiece(king);
            context.setPieceMoves(RulesUtils.kingPossibleMoves(king.getValidMoves(pos, board), board, currentColor, king));
            return context.getCurrentMoves();
        }
        if (!board.isFree(pos) && board.getEntity(pos).get().getPlayerColor() == currentColor
            && interposingPieces.keySet().contains(board.getEntity(pos).get())) {
            final var piece = (Piece) board.getEntity(pos).get();
            context.setCurrentPiece(piece);
            context.setPieceMoves(RulesUtils.kingPossibleMoves(piece.getValidMoves(pos, board), board, currentColor, piece));
            return context.getCurrentMoves();
        }
        if (!board.isFree(pos)
            && board.getEntity(pos).get().getPlayerColor() == RulesUtils.swapColor(currentColor)
            && context.getCurrentPiece().isPresent() && context.getCurrentMoves().contains(pos)) {
            return context.executeTurn(MoveType.MOVE_AND_EAT, pos) ? List.of(pos) : Collections.emptyList();
        }
        context.unsetCurrentPiece();
        return context.getCurrentMoves();
    }
}