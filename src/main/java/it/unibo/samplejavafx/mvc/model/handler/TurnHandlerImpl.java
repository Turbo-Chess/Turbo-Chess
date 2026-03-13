package it.unibo.samplejavafx.mvc.model.handler;

import java.util.Collections;
import java.util.HashMap;
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
import it.unibo.samplejavafx.mvc.model.handler.turnstates.CheckTurnState;
import it.unibo.samplejavafx.mvc.model.handler.turnstates.DoubleCheckTurnState;
import it.unibo.samplejavafx.mvc.model.handler.turnstates.NormalTurnState;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl.MoveType;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import it.unibo.samplejavafx.mvc.model.rules.AdvancedRules;
import it.unibo.samplejavafx.mvc.model.rules.CastleCondition;
import it.unibo.samplejavafx.mvc.model.rules.CheckCalculator;
import it.unibo.samplejavafx.mvc.model.utils.RulesUtils;

/**
 * Placeholder.
 */
public final class TurnHandlerImpl implements TurnHandler, TurnHandlerContext {
    private static final Point2D CASTLE_POS = new Point2D(2, 6);
    private static final Point2D ROOK_CASTLE_POS = new Point2D(3, 5);
    private static final Point2D BOUNDARIES = new Point2D(0, 7);
    private final ChessMatch match;
    private final ChessBoard board;
    private final Map<Piece, List<Point2D>> interposingPieces;
    private GameState state;
    private TurnState turnState;
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
        this.turnState = new NormalTurnState(this);
        this.interposingPieces = new HashMap<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Point2D> thinking(final Point2D pos) {
        final List<Point2D> results = this.turnState.thinking(pos);
        return results;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void transitionTo(final TurnState newState) {
        this.turnState = newState;
    }

    private void choosingTurnState() {
        switch (state) {
            case NORMAL -> transitionTo(new NormalTurnState(this));
            case CHECK -> transitionTo(new CheckTurnState(this));
            case DOUBLE_CHECK -> transitionTo(new DoubleCheckTurnState(this));
            default -> transitionTo(turnState);
        }
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
                break;
        }

        this.interposingPieces.clear();
        this.state = AdvancedRules.check(board, RulesUtils.swapColor(currentColor));

        if (this.state == GameState.CHECK) {
            this.interposingPieces.putAll(CheckCalculator.getInterposingPieces(board, RulesUtils.swapColor(currentColor)));
        }

        if (state == GameState.CHECK || state == GameState.DOUBLE_CHECK) {
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
        choosingTurnState();
        updateStats();

        this.castlingOptions = AdvancedRules.castle(board, currentColor);
        unsetCurrentPiece();
        return true;
    }

    private List<Point2D> ensureMoveSafety(final List<Point2D> list) {
        return list.stream()
                   .filter(pos -> CheckCalculator.isMoveSafe(board, currentPiece.get(),
                           board.getPosByEntity(currentPiece.get()), pos, currentColor))
                   .collect(Collectors.toList());
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
            updateStats();
            this.state = AdvancedRules.check(board, RulesUtils.swapColor(currentColor));
            choosingTurnState();
            updateStats();
            return true;
        }
        return false;
    }

    /**
     * Getter for the current piece position used for promotion.
     * 
     * @return the {@link Point2D} position.
     */
    @Override
    public Point2D getPromotingPawnPos() {
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

    /**
     * Unsets the current piece and all related fields.
     */
    @Override
    public void unsetCurrentPiece() {
        this.currentPiece = Optional.empty();
        this.pieceMoves = Collections.emptyList();
    }

// - - - Helper methods for TurnState implementations - - -

    /**
     * {@inheritDoc}
     */
    @Override
    public PlayerColor getCurrentColor() {
        return this.currentColor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Piece> getCurrentPiece() {
        return this.currentPiece;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Point2D> getCurrentMoves() {
        return this.pieceMoves;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCurrentPiece(final Piece piece) {
        this.currentPiece = Optional.of(piece);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPieceMoves(final List<Point2D> moves) {
        this.pieceMoves = ensureMoveSafety(moves);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChessBoard getBoard() {
        return this.board;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Piece, List<Point2D>> getInterposing() {
        return this.interposingPieces;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CastleCondition getCastleCon() {
        return this.castlingOptions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTurn() {
        return this.turn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTurn(final int newTurn) {
        this.turn = newTurn;
    }

    /**
     * Setter for the promotionHolder of the TurnHandler.
     * 
     * @return the {@link Optional} where we want to save a promoting piece.
     */
    public void passOnPromotion(final Optional<Piece> pawn) {
        this.promotionHolder = pawn;
    }

// - - - Helper methods for Replay and LoadGame related features - - -

    @Override
    public void setStartTurn(final int turn) {
        this.turn = turn;
    }

    @Override
    public void setStartPlayerColor(final PlayerColor color) {
        this.currentColor = color;
    }
}
