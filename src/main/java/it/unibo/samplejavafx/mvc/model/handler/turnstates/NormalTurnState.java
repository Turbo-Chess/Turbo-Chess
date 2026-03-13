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
import it.unibo.samplejavafx.mvc.model.rules.CastleCondition;
import it.unibo.samplejavafx.mvc.model.utils.RulesUtils;

public final class NormalTurnState extends AbstractTurnState {
    private static final Point2D CASTLE_POS = new Point2D(2, 6);
    private final ChessBoard board;
    private CastleCondition castlingOptions;
    private PlayerColor currentColor;

    public NormalTurnState(final TurnHandlerContext context) {
        super(context);
        this.board = context.getBoard();
        this.castlingOptions = context.getCastleCon();
        this.currentColor = context.getCurrentColor();
    }

    /**
     * Strategy for handling thinking during a {@code NORMAL}.
     * 
     * @param pos the {@link Point2D} of the chosen cell.
     * @return  a list of {@link Point2D} containing all the possible moves for a piece,
     *          returns a single {@link Point2D} of the chosen movement if the piece moves,
     *          returns an empty list if there are no avaiable moves or no owned pieces are selected. 
     */
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
            switch (castlingOptions) {
                case CASTLE_BOTH:
                    context.getCurrentMoves().addAll(List.of(new Point2D(CASTLE_POS.x(), board.getPosByEntity(king).y()), 
                                                   new Point2D(CASTLE_POS.y(), board.getPosByEntity(king).y())));
                    break;
                case CASTLE_LEFT:
                    context.getCurrentMoves().add(new Point2D(CASTLE_POS.x(), board.getPosByEntity(king).y()));
                    break;
                case CASTLE_RIGHT:
                    context.getCurrentMoves().add(new Point2D(CASTLE_POS.y(), board.getPosByEntity(king).y()));
                    break;
                case NO_CASTLE:
                    break;
            }
        }
        if (!board.isFree(pos) && board.getEntity(pos).get().getPlayerColor() == currentColor) {
            final var newPiece = (Piece) board.getEntity(pos).get();
            context.setCurrentPiece(newPiece);
            context.passOnPromotion(Optional.of(newPiece));
            context.setPieceMoves(newPiece.getValidMoves(pos, board));
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
