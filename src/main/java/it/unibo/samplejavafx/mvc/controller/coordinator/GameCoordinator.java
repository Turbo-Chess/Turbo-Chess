package it.unibo.samplejavafx.mvc.controller.coordinator;

import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameController;
import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameControllerImpl;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatch;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatchImpl;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PieceDefinition;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import it.unibo.samplejavafx.mvc.controller.uicontroller.ChessboardViewControllerImpl;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * placeholder.
 */
public final class GameCoordinator {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 800;
    private static final int KING_POS_X = 6;
    private static final int KING_POS_Y = 7;
    private static final int BISHOP_POS_X = 4;
    private static final int BISHOP_POS_Y = 4;
    private static final int BISHOP2_POS_X = 5;
    private static final int BISHOP2_POS_Y = 3;
    private static final String STANDARD_PACK = "StandardChessPieces";
    private static final Logger LOGGER = LoggerFactory.getLogger(GameCoordinator.class);

    private final Stage stage;
    private final ChessMatch match = new ChessMatchImpl();
    private final GameController gameController = new GameControllerImpl(match);

    /**
     * placeholder.
     *
     * @param stage placeholder.
     */
    public GameCoordinator(final Stage stage) {
        this.stage = stage;
    }

    /**
     * placeholder.
     */
    public void loadPieces() {
        gameController.getLoaderController().load();
        LOGGER.debug(gameController.getLoaderController().getEntityCache().toString());
    }

    /**
     * Initializes the main menu.
     */
    public void initMainMenu() {
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/MainMenu.fxml"));
            loader.setControllerFactory(c -> new it.unibo.samplejavafx.mvc.controller.uicontroller.MainMenuController(this));
            final Parent root = loader.load();
            final Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            final var cssLocation = getClass().getResource("/css/MainMenu.css");
            if (cssLocation != null) {
                scene.getStylesheets().add(cssLocation.toExternalForm());
            }
            stage.setTitle("TurboChess - Main Menu");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            LOGGER.error("Failed to load Main Menu", e);
        }
    }

    /**
     * Initializes the settings scene.
     */
    public void initSettings() {
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/Settings.fxml"));
            loader.setControllerFactory(c -> new it.unibo.samplejavafx.mvc.controller.uicontroller.SettingsController(this));
            final Parent root = loader.load();
            final Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            final var cssLocation = getClass().getResource("/css/MainMenu.css");
            if (cssLocation != null) {
                scene.getStylesheets().add(cssLocation.toExternalForm());
            }
            stage.setTitle("TurboChess - Settings");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            LOGGER.error("Failed to load Settings", e);
        }
    }

    /**
     * Initializes the loadout scene.
     */
    public void initLoadout() {
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/Loadout.fxml"));
            loader.setControllerFactory(c -> new it.unibo.samplejavafx.mvc.controller.uicontroller.LoadoutController(this));
            final Parent root = loader.load();
            final Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            final var cssLocation = getClass().getResource("/css/MainMenu.css");
            if (cssLocation != null) {
                scene.getStylesheets().add(cssLocation.toExternalForm());
            }
            stage.setTitle("TurboChess - Loadout");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            LOGGER.error("Failed to load Loadout", e);
        }
    }

    /**
     * Quits the application.
     */
    public void quit() {
        stage.close();
    }

    /**
     * placeholder.
     *
     * @throws IOException placeholder.
     */
    public void initGame() throws IOException {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/GameLayout.fxml"));
        loader.setControllerFactory(c -> new ChessboardViewControllerImpl(this.gameController, this));
        final Parent root = loader.load();
        final ChessboardViewControllerImpl viewController = loader.getController();
        // TODO: remove reference of the match in the view controller
        match.getBoard().addObserver(viewController);
        match.addObserver(viewController);
        gameController.setChessboardViewController(viewController);

        gameController.getLoaderController().load();

        final var cssLocation = getClass().getResource("/css/GameLayout.css");
        final Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        stage.setTitle("TurboChess - Game");
        stage.setScene(scene);
        scene.getStylesheets().add(cssLocation.toExternalForm());
        stage.show();
        //Only for testing
        final var entityCache = gameController.getLoaderController().getEntityCache();
        final Piece rook = new Piece.Builder()
                .entityDefinition(
                        (PieceDefinition) entityCache.get(STANDARD_PACK).get("rook"))
                .gameId(1)
                .playerColor(PlayerColor.BLACK)
                .build();

        /*final Piece newPiece = new Piece.Builder()
                .entityDefinition(
                        (PieceDefinition) entityCache.get("TestPack").get("new"))
                .gameId(5)
                .playerColor(PlayerColor.BLACK)
                .build();*/

        final Piece king = new Piece.Builder()
                .entityDefinition(
                        (PieceDefinition) entityCache.get(STANDARD_PACK).get("king"))
                .gameId(2)
                .playerColor(PlayerColor.WHITE)
                .build();

        final Piece bishop = new Piece.Builder()
                .entityDefinition(
                        (PieceDefinition) entityCache.get(STANDARD_PACK).get("bishop"))
                .gameId(3)
                .playerColor(PlayerColor.WHITE)
                .build();

        final Piece bishop2 = new Piece.Builder()
                .entityDefinition(
                        (PieceDefinition) entityCache.get(STANDARD_PACK).get("bishop"))
                .gameId(4)
                .playerColor(PlayerColor.WHITE)
                .build();

        this.match.getBoard().setEntity(new Point2D(1, 1), rook);
        this.match.getBoard().setEntity(new Point2D(KING_POS_X, KING_POS_Y), king);
        this.match.getBoard().setEntity(new Point2D(BISHOP_POS_X, BISHOP_POS_Y), bishop);
        this.match.getBoard().setEntity(new Point2D(BISHOP2_POS_X, BISHOP2_POS_Y), bishop2);
        //this.match.getBoard().setEntity(new Point2D(5, 5), newPiece);
    }
}
