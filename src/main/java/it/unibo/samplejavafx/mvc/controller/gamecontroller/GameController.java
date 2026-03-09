package it.unibo.samplejavafx.mvc.controller.gamecontroller;

import it.unibo.samplejavafx.mvc.controller.loadercontroller.LoaderController;
import it.unibo.samplejavafx.mvc.controller.uicontroller.ChessboardViewController;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.chessboard.boardfactory.BoardFactory;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatch;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.loadout.Loadout;
import it.unibo.samplejavafx.mvc.model.loadout.LoadoutEntry;
import it.unibo.samplejavafx.mvc.model.loadout.LoadoutManager;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import it.unibo.samplejavafx.mvc.model.replay.GameHistory;

import java.util.List;

/**
 * The {@code GameController} interface orchestrates the flow of the chess game, mediating between
 * the data model (match, board, loadouts) and the user interface.
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
     * @param chessboardViewController The {@link ChessboardViewController} to associate.
     */
    void setChessboardViewController(ChessboardViewController chessboardViewController);

    /**
     * Generates the correct file path for a piece's image based on its base path, player color, and ID.
     *
     * @param imagePath   The base directory or path for the image.
     * @param playerColor The color of the player owning the piece (affects the image variant).
     * @param id          The specific ID of the piece type.
     * @return a {@link String} representing the full path to the image resource.
     */
    String calculateImageColorPath(String imagePath, PlayerColor playerColor, String id);

    /**
     * Triggers the surrender action for the current player, typically ending the match.
     */
    void surrender();

    /**
     * Executes the promotion of a pawn to a new piece type.
     *
     * @param pieceEntry The {@link LoadoutEntry} describing the new piece to be placed.
     */
    void promote(LoadoutEntry pieceEntry);

    // TODO: remove that and use the one from advanced rules
    /**
     * Retrieves the current position of the King for the active player.
     * useful for validating checks and checkmates.
     *
     * @return the {@link Point2D} coordinate of the King.
     */
    Point2D getKingPos();

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
     * Retrieves the loadout configuration for the White player.
     *
     * @return the White player's {@link Loadout}.
     */
    Loadout getWhiteLoadout();

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

    void setupCoordinator();
}
