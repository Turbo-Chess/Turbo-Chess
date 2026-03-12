package it.unibo.samplejavafx.mvc.model.handler.turnstates;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.handler.TurnHandlerContext;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl.MoveType;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import it.unibo.samplejavafx.mvc.model.utils.RulesUtils;

public final class DoubleCheckTurnState extends AbstractTurnState {
    private final ChessBoard board;
    private PlayerColor currentColor;
    private Optional<Piece> promotionHolder = Optional.empty();

    public DoubleCheckTurnState(final TurnHandlerContext context) {
        super(context);
        this.board = context.getBoard();
        this.currentColor = context.getCurrentColor();
    }

    @Override
    public List<Point2D> thinking(Point2D pos) {
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
        if (!board.isFree(pos)
            && board.getEntity(pos).get().getPlayerColor() == RulesUtils.swapColor(currentColor)
            && context.getCurrentPiece().isPresent() && context.getCurrentMoves().contains(pos)) {
            return context.executeTurn(MoveType.MOVE_AND_EAT, pos) ? List.of(pos) : Collections.emptyList();
        }
        context.unsetCurrentPiece();
        return context.getCurrentMoves();
    }

    @Override
    public void passOnStats(Optional<Piece> promotion) {
        promotion = this.promotionHolder.isPresent() ? Optional.of(this.promotionHolder.get()) : Optional.empty();
    }
}
