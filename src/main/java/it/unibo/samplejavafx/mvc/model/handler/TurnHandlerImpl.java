package it.unibo.samplejavafx.mvc.model.handler;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatch;
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
    private static final Point2D CASTLE_POS = new Point2D(2, 6);
    private static final Point2D ROOK_CASTLE_POS = new Point2D(3, 5);
    private static final Point2D BOUNDARIES = new Point2D(0, 7);
    private final ChessMatch match;
    private final ChessBoard board;
    private final Map<Piece, List<Point2D>> interposingPieces;
    private GameState state;
    private CastleCondition castlingOptions;
    private PlayerColor currentColor;
    private int turn;
    private Optional<Piece> currentPiece = Optional.empty();
    private List<Point2D> pieceMoves;

    /**
     * placeholder.
     *
     * @param match placeholder.
     */
    public TurnHandlerImpl(final ChessMatch match) {
        this.match = match;
        this.turn = match.getTurnNumber();
        this.board = match.getBoard();
        this.currentColor = match.getCurrentPlayer();
        this.state = match.getGameState();
        this.castlingOptions = CastleCondition.NO_CASTLE;
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
                if (currentPiece.get().getType() == PieceType.KING) {
                    if (target.equals(new Point2D(CASTLE_POS.x(), board.getPosByEntity(currentPiece.get()).y()))
                            && pieceMoves.contains(target)) {
                        board.move(board.getPosByEntity(currentPiece.get()), target);
                        board.move(new Point2D(BOUNDARIES.x(), target.y()), new Point2D(ROOK_CASTLE_POS.x(), target.y()));
                        break;
                    }
                    if (target.equals(new Point2D(CASTLE_POS.y(), board.getPosByEntity(currentPiece.get()).y()))
                            && pieceMoves.contains(target)) {
                        board.move(board.getPosByEntity(currentPiece.get()), target);
                        board.move(new Point2D(BOUNDARIES.y(), target.y()), new Point2D(ROOK_CASTLE_POS.y(), target.y()));
                        break;
                    }
                }
                board.move(board.getPosByEntity(currentPiece.get()), target);
                break;
            case MOVE_AND_EAT:
                board.eat(board.getPosByEntity(currentPiece.get()), target);
                break;
            default:
                // the move wasn't safe, so we cancel the move and go back
        }

        this.interposingPieces.clear();
        this.state = AdvancedRules.check(board, AdvancedRules.swapColor(currentColor));

        if (this.state == GameState.CHECK) {
            this.interposingPieces.putAll(CheckCalculator.getInterposingPieces(board, AdvancedRules.swapColor(currentColor)));
        }

        if ((state == GameState.CHECK || state == GameState.DOUBLE_CHECK)
                && AdvancedRules.checkmate(board, AdvancedRules.swapColor(currentColor), state, interposingPieces)) {
            return false;
        }

        switch (currentColor) {
            case WHITE:
                promotion(BOUNDARIES.x());
            case BLACK:
                promotion(BOUNDARIES.y());
        }

        this.turn += 1;
        this.currentColor = AdvancedRules.swapColor(currentColor);
        updateStats();

        if (AdvancedRules.draw(board, AdvancedRules.swapColor(currentColor), state)) {
            return false;
        }
        this.castlingOptions = AdvancedRules.castle(board, currentColor);
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
            switch (castlingOptions) {
                case CASTLE_BOTH:
                    this.pieceMoves.addAll(List.of(new Point2D(CASTLE_POS.x(), board.getPosByEntity(king).y()), 
                                                   new Point2D(CASTLE_POS.y(), board.getPosByEntity(king).y())));
                    break;
                case CASTLE_LEFT:
                    this.pieceMoves.add(new Point2D(CASTLE_POS.x(), board.getPosByEntity(king).y()));
                    break;
                case CASTLE_RIGHT:
                    this.pieceMoves.add(new Point2D(CASTLE_POS.y(), board.getPosByEntity(king).y()));
                    break;
                case NO_CASTLE:
                    break;
            }
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
     * 
     * @param height placeholder.
     * @return placeholder.
     */
    private boolean promotion(final int height) {
        final List<Point2D> pawn = board.getBoard().keySet().stream()
                .filter(pos -> pos.y() == height)
                .filter(pos -> board.getEntity(pos).get().getType() == PieceType.PAWN)
                .toList();
        if (!pawn.isEmpty()) {
            currentPiece = Optional.of((Piece) board.getEntity(pawn.getFirst()).get());
            state = GameState.PROMOTION;
            return true;
        }
        return false;
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
     */
    public void updateStats() {
        match.updateGameState(state);
        match.updatePlayerColor(currentColor);
        match.updateTurn(turn);
    }
}
