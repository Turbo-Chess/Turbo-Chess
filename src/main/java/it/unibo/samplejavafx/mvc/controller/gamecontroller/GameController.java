package it.unibo.samplejavafx.mvc.controller.gamecontroller;

import it.unibo.samplejavafx.mvc.controller.loadercontroller.LoaderController;
import it.unibo.samplejavafx.mvc.controller.uicontroller.BoardView;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatch;
import it.unibo.samplejavafx.mvc.model.loadout.Loadout;
import it.unibo.samplejavafx.mvc.model.loadout.LoadoutEntry;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import it.unibo.samplejavafx.mvc.model.replay.GameHistory;

/**
 * The {@code GameController} interface orchestrates the flow of the chess game, mediating between
 * the data model (match, board, loadouts) and the user interface.
 *
 * <p>
 * It provides methods for:
 * - Managing game entities and resources via the {@link LoaderController}.
 * - Handling user interactions such as clicking on the board to move pieces.
 * - Controlling game state transitions like surrender or promotion.
 * - Caching valid moves to optimize performance.
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
     * Links the UI view controller to this game controller.
     * This allows the game controller to trigger UI updates (e.g., highlighting cells).
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
     * @return the {@link Point2D} coordinate of the King.
     */
    Point2D getKingPos();

    /**
     * Retrieves the loadout configuration for the White player.
     *
     * @return the White player's {@link Loadout}.
     */
    Loadout getWhiteLoadout();

    /**
     * Sets the white player's loadout.
     *
     * @param loadout the loadout to set.
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
     * Requests the UI to display the main game view.
     */
    void showGame();

    /**
     * Retrieves the current active match reference.
     *
     * @return the {@link ChessMatch} object.
     */
    ChessMatch getMatch();
}
