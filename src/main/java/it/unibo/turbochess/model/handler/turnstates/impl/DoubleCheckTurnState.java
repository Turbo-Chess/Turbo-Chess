package it.unibo.turbochess.model.handler.turnstates.impl;

import java.util.Collections;
import java.util.List;

import it.unibo.turbochess.model.chessboard.board.api.ChessBoard;
import it.unibo.turbochess.model.entity.impl.Piece;
import it.unibo.turbochess.model.entity.impl.PieceType;
import it.unibo.turbochess.model.entity.impl.PlayerColor;
import it.unibo.turbochess.model.handler.api.TurnHandlerContext;
import it.unibo.turbochess.model.handler.turnstates.api.AbstractTurnState;
import it.unibo.turbochess.model.movement.impl.MoveRulesImpl.MoveType;
import it.unibo.turbochess.model.point2d.Point2D;
import it.unibo.turbochess.model.utils.RulesUtils;

/**
 * {@inheritDoc}
 * Implementation of {@link TurnState} for the {@code DOUBLE_CHECK} {@link GameState}.
 */
public final class DoubleCheckTurnState extends AbstractTurnState {
    private final ChessBoard board;

    /**
     * Contructor for the DoubleCheckTurnState.
     * 
     * @param context the current {@link TurnHandlerContext}.
     */
    public DoubleCheckTurnState(final TurnHandlerContext context) {
        super(context);
        this.board = context.getBoard();
    }

    /**
     * Strategy for handling thinking during a {@code DOUBLE_CHECK}.
     * 
     * @param pos the {@link Point2D} of the chosen cell.
     * @return  a list of {@link Point2D} containing all the possible moves for a piece,
     *          returns a single {@link Point2D} of the chosen movement if the piece moves,
     *          returns an empty list if there are no avaiable moves or no owned pieces are selected. 
     */
    @Override
    public List<Point2D> thinking(final Point2D pos) {
        if (board.isFree(pos) && getContext().getCurrentPiece().isEmpty()) {
            return Collections.emptyList();
        }
        if (board.isFree(pos) && getContext().getCurrentMoves().contains(pos)) {
            return getContext().executeTurn(MoveType.MOVE_ONLY, pos) ? List.of(pos) : Collections.emptyList();
        }
        final PlayerColor currentColor = getContext().getCurrentColor();
        if (!board.isFree(pos)
            && board.getEntity(pos).get().getPlayerColor() == currentColor
            && board.getEntity(pos).get().getType() == PieceType.KING) {
            final var king = (Piece) board.getEntity(pos).get();
            getContext().setCurrentPiece(king);
            getContext().setPieceMoves(RulesUtils.kingPossibleMoves(king.getValidMoves(pos, board), board, currentColor, king));
            return getContext().getCurrentMoves();
        }
        if (!board.isFree(pos)
            && board.getEntity(pos).get().getPlayerColor() == RulesUtils.swapColor(currentColor)
            && getContext().getCurrentPiece().isPresent() && getContext().getCurrentMoves().contains(pos)) {
            return getContext().executeTurn(MoveType.MOVE_AND_EAT, pos) ? List.of(pos) : Collections.emptyList();
        }
        getContext().unsetCurrentPiece();
        return getContext().getCurrentMoves();
    }
}
