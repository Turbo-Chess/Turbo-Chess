package it.unibo.samplejavafx.mvc.model.handler;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl.MoveType;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import it.unibo.samplejavafx.mvc.model.rules.AdvancedRules;
import it.unibo.samplejavafx.mvc.model.rules.CastleCondition;
import it.unibo.samplejavafx.mvc.model.rules.CheckCalculator;

/**
 * Placeholder.
 */
public class TurnHandlerImpl implements TurnHandler {
    private int turn;
    private final ChessBoard board;
    private GameState state;
    private CastleCondition castlingOptions;
    private PlayerColor currentColor;
    private Optional<Piece> currentPiece = Optional.empty();
    private List<Point2D> pieceMoves;
    private final Map<Piece, List<Point2D>> interposingPieces;

    /**
     * placeholder.
     *
     * @param turn placeholder.
     * @param board placeholder.
     */
    public TurnHandlerImpl(final int turn, final ChessBoard board) {
        this.turn = turn;
        this.board = board;
        this.currentColor = PlayerColor.WHITE;
        this.castlingOptions = CastleCondition.NO_CASTLE;
        this.state = GameState.NORMAL;
        this.interposingPieces = new HashMap<>();
    }

    /**
     * placeholder.
     * 
     * @param pos placeholder.
     * @return placeholder.
     */
    @Override
    public List<Point2D> thinking(final Point2D pos) {
        if (state == GameState.NORMAL) {
            return doIfNormal(pos);
        }
        if (state == GameState.CHECK) {
            return doIfCheck(pos);
        }
        if (state == GameState.DOUBLE_CHECK) {
            return doIfDoubleCheck(pos);
        }
        return Collections.emptyList(); // unreachable for design
    }

    /**
     *  placeholder.
     *
     * @param moveAction placeholder.
     * @param target placeholder.
     */
    @Override
    public boolean executeTurn(final MoveType moveAction, final Point2D target) {
        if (!CheckCalculator.isMoveSafe(board, currentPiece.get(),
           board.getPosByEntity(currentPiece.get()), target, currentColor)) {
                return false; // the move wasn't safe, so we cancel it and go back
        }
        switch (moveAction) {
            case MOVE_ONLY: 
                board.move(board.getPosByEntity(currentPiece.get()), target);
                break;
            case MOVE_AND_EAT:
                board.eat(board.getPosByEntity(currentPiece.get()), target);
                break;
            default:
                // the move wasn't safe, so we cancel the move and go back
        }
        this.state = AdvancedRules.check(board, AdvancedRules.swapColor(currentColor));
        if ((state == GameState.CHECK || state == GameState.DOUBLE_CHECK)
                && AdvancedRules.checkmate(board, AdvancedRules.swapColor(currentColor), state, interposingPieces)) {
            // call to another function that ends the match
            System.out.println("to be implemented"); //NOPMD
        }
        if (AdvancedRules.draw(board, AdvancedRules.swapColor(currentColor))) {
            // call to another function that ends the match
            System.out.println("to be implemented"); //NOPMD
        }

        // Changing variables for the next turn iteration
        this.castlingOptions = AdvancedRules.castle(board, AdvancedRules.swapColor(currentColor));
        this.turn += 1;
        this.currentColor = AdvancedRules.swapColor(currentColor);
        unsetCurrentPiece();
        return true;
    }

    /**
     * placeholder.
     * 
     * @param pos placeholder.
     * @return placeholder.
     */
    private List<Point2D> doIfNormal(final Point2D pos) {
        if (board.isFree(pos) && currentPiece.isEmpty()) {
            return Collections.emptyList();
        }
        if (board.isFree(pos) && pieceMoves.contains(pos)) {
            return executeTurn(MoveType.MOVE_ONLY, pos) ? List.of(pos) : Collections.emptyList();
        }
        if (!board.isFree(pos) && board.getEntity(pos).get().getPlayerColor() == currentColor
                && board.getEntity(pos).get().getType() == PieceType.KING) {
            final var king = (Piece) board.getEntity(pos).get();
            this.currentPiece = Optional.of(king);
            this.pieceMoves = AdvancedRules.kingPossibleMoves(king.getValidMoves(pos, board), board, currentColor);
            return this.pieceMoves;
        }
        if (!board.isFree(pos) && board.getEntity(pos).get().getPlayerColor() == currentColor) {
            final var newPiece = (Piece) board.getEntity(pos).get();
            this.currentPiece = Optional.of(newPiece);
            this.pieceMoves = newPiece.getValidMoves(pos, board);
            return this.pieceMoves;
        }
        if (!board.isFree(pos)
            && board.getEntity(pos).get().getPlayerColor() == AdvancedRules.swapColor(currentColor)
            && currentPiece.isPresent() && pieceMoves.contains(pos)) {
            return executeTurn(MoveType.MOVE_AND_EAT, pos) ? List.of(pos) : Collections.emptyList();
        }
        unsetCurrentPiece();
        return this.pieceMoves;
    }

    /**
     * placeholder.
     * 
     * @param pos placeholder.
     * @return placeholder.
     */
    private List<Point2D> doIfCheck(final Point2D pos) {
        if (board.isFree(pos) && currentPiece.isEmpty()) {
            return Collections.emptyList();
        }
        if (board.isFree(pos) && pieceMoves.contains(pos)) {
            return executeTurn(MoveType.MOVE_ONLY, pos) ? List.of(pos) : Collections.emptyList();
        }
        if (!board.isFree(pos) && board.getEntity(pos).get().getPlayerColor() == currentColor
            && board.getEntity(pos).get().getType() == PieceType.KING) {
            final var king = (Piece) board.getEntity(pos).get();
            this.currentPiece = Optional.of(king);
            this.pieceMoves = AdvancedRules.kingPossibleMoves(king.getValidMoves(pos, board), board, currentColor);
            return this.pieceMoves;
        }
        if (!board.isFree(pos) && board.getEntity(pos).get().getPlayerColor() == currentColor
            && interposingPieces.keySet().contains(board.getEntity(pos).get())) {
            final var piece = (Piece) board.getEntity(pos).get();
            this.currentPiece = Optional.of(piece);
            this.pieceMoves = interposingPieces.get(piece);
            return this.pieceMoves;
        }
        if (!board.isFree(pos)
            && board.getEntity(pos).get().getPlayerColor() == AdvancedRules.swapColor(currentColor)
            && currentPiece.isPresent() && pieceMoves.contains(pos)) {
            return executeTurn(MoveType.MOVE_AND_EAT, pos) ? List.of(pos) : Collections.emptyList();
        }
        unsetCurrentPiece();
        return this.pieceMoves;
    }

    /**
     * placeholder.
     * 
     * @param pos placeholder.
     * @return placeholder.
     */
    private List<Point2D> doIfDoubleCheck(final Point2D pos) {
        if (board.isFree(pos) && currentPiece.isEmpty()) {
            return Collections.emptyList();
        }
        if (board.isFree(pos) && pieceMoves.contains(pos)) {
            return executeTurn(MoveType.MOVE_ONLY, pos) ? List.of(pos) : Collections.emptyList();
        }
        if (!board.isFree(pos) && board.getEntity(pos).get().getPlayerColor() == currentColor
            && board.getEntity(pos).get().getType() == PieceType.KING) {
            final var king = (Piece) board.getEntity(pos).get();
            this.currentPiece = Optional.of(king);
            this.pieceMoves = AdvancedRules.kingPossibleMoves(king.getValidMoves(pos, board), board, currentColor);
            return this.pieceMoves;
        }
        if (!board.isFree(pos)
            && board.getEntity(pos).get().getPlayerColor() == AdvancedRules.swapColor(currentColor)
            && currentPiece.isPresent() && pieceMoves.contains(pos)) {
            return executeTurn(MoveType.MOVE_AND_EAT, pos) ? List.of(pos) : Collections.emptyList();
        }
        unsetCurrentPiece();
        return this.pieceMoves;
    }

    /**
     * placeholder.
     */
    private void unsetCurrentPiece() {
        this.currentPiece = Optional.empty();
        this.pieceMoves = Collections.emptyList();
    }

    /**
     * placeholder.
     * 
     * @return placeholder.
     */
    public int getCurrentTurn() {
        return this.turn;
    }
}
