package it.unibo.turbochess.controller.gamecontroller.api;

import it.unibo.turbochess.controller.uicontroller.api.BoardView;
import it.unibo.turbochess.model.chessboard.board.api.ChessBoard;
import it.unibo.turbochess.model.chessmatch.api.ChessMatch;
import it.unibo.turbochess.model.entity.impl.PlayerColor;
import it.unibo.turbochess.model.loadout.api.Loadout;
import it.unibo.turbochess.model.loadout.impl.LoadoutEntry;
import it.unibo.turbochess.model.point2d.Point2D;
import it.unibo.turbochess.model.replay.api.GameHistory;

/**
 * The {@code GameController} interface orchestrates the flow of the chess game, mediating between
 * the data model (match, board, loadouts) and the user interface.
 *
 * <p>
 * It provides methods for things like handling user interactions such as clicking on the board to move pieces and
 * controlling the game state transitions like surrender or promotion.
 * </p>
 */
public interface GameController {

    /**
     * Initializes the controller with a specific chess match instance.
     * This links the controller to the model that it will manipulate.
     *
     * @param match The {@link ChessMatch} to be managed.
     */
    void setMatch(ChessMatch match);

    /**
     * Handles the user's interaction with a specific point on the board.
     * This method interprets clicks as either selecting a piece or attempting to move to a destination.
     *
     * @param pointClicked The {@link Point2D} coordinate on the board that was clicked.
     */
    void handleClick(Point2D pointClicked);

    /**
     * Link the controller to a specific subset of decoupled view functions
     * to trigger specific UI updates (e.g., highlighting cells).
     *
     * @param boardView The {@link BoardView} to manage showing of specific cells.
     */
    void setBoardView(BoardView boardView);

    /**
     * Triggers the surrender action for the current player, typically ending the match.
     */
    void surrender();

    /**
     * Executes the promotion of a pawn to a new piece type.
     *
     * @param pieceEntry the {@link LoadoutEntry} of the piece to promote.
     */
    void promote(LoadoutEntry pieceEntry);

    /**
     * Retrieves the current position of the King for the active player.
     * useful for validating checks and checkmates.
     *
     * @param color the {@link PlayerColor} of the wanted King.
     * @return the {@link Point2D} coordinate of the King.
     */
    Point2D getKingPos(PlayerColor color);

    /**
     * Retrieves the loadout configuration for the White player.
     *
     * @return the White player's {@link Loadout}.
     */
    Loadout getWhiteLoadout();

    /**
     * Sets the white player's loadout.
     *
     * @param loadout the {@link Loadout} to set.
     */
    void setWhiteLoadout(Loadout loadout);

    /**
     * Sets the black player's loadout.
     *
     * @param loadout the loadout to set.
     */
    void setBlackLoadout(Loadout loadout);

    /**
     * Retrieves the loadout configuration for the Black player.
     *
     * @return the Black player's {@link Loadout}.
     */
    Loadout getBlackLoadout();

    /**
     * @return the game history.
     */
    GameHistory getGameHistory();

    /**
     * @return the live chessboard.
     */
    ChessBoard getLiveBoard();

    /**
     * Retrieves the current active match reference.
     *
     * @return the {@link ChessMatch} object.
     */
    ChessMatch getMatch();
}
