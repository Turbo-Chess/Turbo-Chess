package it.unibo.samplejavafx.mvc.model.handler;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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
import it.unibo.samplejavafx.mvc.model.utils.RulesUtils;

/**
 * Placeholder.
 */
public final class TurnHandlerImpl implements TurnHandler {
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
    private Optional<Piece> promotionHolder = Optional.empty();

    /**
     * placeholder.
     *
     * @param match placeholder.
     */
    // The TurnHandler needs to manage the state of the match, so passing a mutable reference to it
    // is appropriate in this case.
    @SuppressFBWarnings("EI_EXPOSE_REP2")
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
     * Handles all the actions of the players during his turn.
     * 
     * @param pos the {@link Point2D} of the clicked cell.
     * @return a list of {@link Point2D} of all possible moves for the View side.
     */
    @Override
    public List<Point2D> thinking(final Point2D pos) {
        return switch (state) {
            case NORMAL -> {
                yield doIfNormal(pos);
            }
            case CHECK -> {
                yield doIfCheck(pos);
            }
            case DOUBLE_CHECK -> {
                yield doIfDoubleCheck(pos);
            }
            case PROMOTION -> {
                if (AdvancedRules.check(board, currentColor) == GameState.CHECK) {
                    yield doIfCheck(pos);
                }
                yield doIfNormal(pos);
            }
            default -> {
                yield new LinkedList<>();
            }
        };
    }

    /**
     * Executes the turn, finalizing the chosen move and rechecking all rules.
     *
     * @param moveAction the {@link MoveType} of the chosen move.
     * @param target the {@link Point2D} position of the chosen move.
     * @return   {@code true} if the turn has ended successfully, 
     *           {@code false} if the game ends with {@code CHECKMATE} or {@code DRAW}.
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
        this.state = AdvancedRules.check(board, RulesUtils.swapColor(currentColor));

        if (this.state == GameState.CHECK) {
            this.interposingPieces.putAll(CheckCalculator.getInterposingPieces(board, RulesUtils.swapColor(currentColor)));
        }

        if ((state == GameState.CHECK || state == GameState.DOUBLE_CHECK)) {
            this.state = AdvancedRules.checkmate(board, RulesUtils.swapColor(currentColor), state, interposingPieces);
            updateStats();
        }

        switch (currentColor) {
            case WHITE:
                promotion(BOUNDARIES.x());
                break;
            case BLACK:
                promotion(BOUNDARIES.y());
                break;
        }

        this.state = AdvancedRules.draw(board, RulesUtils.swapColor(currentColor), state);
        this.turn += 1;
        this.currentColor = RulesUtils.swapColor(currentColor);
        updateStats();

        this.castlingOptions = AdvancedRules.castle(board, currentColor);
        unsetCurrentPiece();
        return true;
    }

    /**
     * Strategy for handling thinking during a {@code NORMAL}.
     * 
     * @param pos the {@link Point2D} of the chosen cell.
     * @return  a list of {@link Point2D} containing all the possible moves for a piece,
     *          returns a single {@link Point2D} of the chosen movement if the piece moves,
     *          returns an empty list if there are no avaiable moves or no owned pieces are selected. 
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
            this.pieceMoves = RulesUtils.kingPossibleMoves(king.getValidMoves(pos, board), board, currentColor);
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
            return ensureMoveSafety(this.pieceMoves);
        }
        if (!board.isFree(pos) && board.getEntity(pos).get().getPlayerColor() == currentColor) {
            final var newPiece = (Piece) board.getEntity(pos).get();
            this.currentPiece = Optional.of(newPiece);
            this.promotionHolder = Optional.of(newPiece);
            this.pieceMoves = newPiece.getValidMoves(pos, board);
            return ensureMoveSafety(this.pieceMoves);
        }
        if (!board.isFree(pos)
            && board.getEntity(pos).get().getPlayerColor() == RulesUtils.swapColor(currentColor)
            && currentPiece.isPresent() && pieceMoves.contains(pos)) {
            return executeTurn(MoveType.MOVE_AND_EAT, pos) ? List.of(pos) : Collections.emptyList();
        }
        unsetCurrentPiece();
        return this.pieceMoves;
    }

    /**
     * Strategy for handling thinking during a {@code CHECK}.
     * 
     * @param pos the {@link Point2D} of the chosen cell.
     * @return  a list of {@link Point2D} containing all the possible moves for a piece,
     *          returns a single {@link Point2D} of the chosen movement if the piece moves,
     *          returns an empty list if there are no avaiable moves or no owned pieces are selected. 
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
            this.pieceMoves = RulesUtils.kingPossibleMoves(king.getValidMoves(pos, board), board, currentColor);
            return ensureMoveSafety(this.pieceMoves);
        }
        if (!board.isFree(pos) && board.getEntity(pos).get().getPlayerColor() == currentColor
            && interposingPieces.keySet().contains(board.getEntity(pos).get())) {
            final var piece = (Piece) board.getEntity(pos).get();
            this.currentPiece = Optional.of(piece);
            this.pieceMoves = interposingPieces.get(piece);
            return ensureMoveSafety(this.pieceMoves);
        }
        if (!board.isFree(pos)
            && board.getEntity(pos).get().getPlayerColor() == RulesUtils.swapColor(currentColor)
            && currentPiece.isPresent() && pieceMoves.contains(pos)) {
            return executeTurn(MoveType.MOVE_AND_EAT, pos) ? List.of(pos) : Collections.emptyList();
        }
        unsetCurrentPiece();
        return this.pieceMoves;
    }

    /**
     * Strategy for handling thinking during a {@code DOUBLE_CHECK}.
     * 
     * @param pos the {@link Point2D} of the chosen cell.
     * @return  a list of {@link Point2D} containing all the possible moves for a piece,
     *          returns a single {@link Point2D} of the chosen movement if the piece moves,
     *          returns an empty list if there are no avaiable moves or no owned pieces are selected. 
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
            this.pieceMoves = RulesUtils.kingPossibleMoves(king.getValidMoves(pos, board), board, currentColor);
            return ensureMoveSafety(this.pieceMoves);
        }
        if (!board.isFree(pos)
            && board.getEntity(pos).get().getPlayerColor() == RulesUtils.swapColor(currentColor)
            && currentPiece.isPresent() && pieceMoves.contains(pos)) {
            return executeTurn(MoveType.MOVE_AND_EAT, pos) ? List.of(pos) : Collections.emptyList();
        }
        unsetCurrentPiece();
        return this.pieceMoves;
    }

    private List<Point2D> ensureMoveSafety(final List<Point2D> list) {
        return list.stream()
                   .filter(pos -> CheckCalculator.isMoveSafe(board, currentPiece.get(),
                           board.getPosByEntity(currentPiece.get()), pos, currentColor))
                   .collect(Collectors.toList());
    }

    @Override
    public void setTurn(final int turn) {
        this.turn = turn;
    }

    @Override
    public void setPlayerColor(final PlayerColor color) {
        this.currentColor = color;
    }

    /**
     * Handles the {@code PROMOTION} GameState.
     * 
     * @param height the y coordinate of the promotion (determines if the {@link PlayerColor} is either white or black).
     * @return {@code true} if a promotion is taking place, {@code false} otherwise.
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
     * Unsets the current piece and all related fields.
     */
    private void unsetCurrentPiece() {
        this.currentPiece = Optional.empty();
        this.pieceMoves = Collections.emptyList();
    }

    /**
     * Getter for the current piece position used for promotion.
     * 
     * @return the {@link Point2D} position.
     */
    @Override
    public Point2D getCurrentPiecePos() {
        return board.getPosByEntity(promotionHolder.get());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void surrender() {
        this.state = GameState.CHECKMATE;
        match.updateGameState(this.state, RulesUtils.swapColor(this.currentColor));
        match.updatePlayerColor(this.currentColor);
        match.updateTurn(this.turn);
    }

    /**
     * Updates the match stats with the real current statistics.
     */
    public void updateStats() {
        match.updateGameState(state, currentColor);
        match.updatePlayerColor(currentColor);
        match.updateTurn(turn);
    }
}
