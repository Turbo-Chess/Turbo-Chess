package it.unibo.samplejavafx.mvc.controller.coordinator;

import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameController;
import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameControllerImpl;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatch;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatchImpl;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PieceDefinition;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import it.unibo.samplejavafx.mvc.controller.uicontroller.ChessboardViewControllerImpl;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * placeholder.
 */
public final class GameCoordinator {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 800;
    private static final int ROOK_WEIGHT = 5;
    private static final int KING_WEIGHT = 100;
    private static final int BKING_POS_Y = 0;
    private static final int WKING_POS_Y = 7;
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

        final var cssLocation = getClass().getResource("/css/GameLayout.css");
        final Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        stage.setTitle("TurboChess - Game");
        stage.setScene(scene);
        scene.getStylesheets().add(cssLocation.toExternalForm());
        stage.show();
        //Only for testing
        //Only for testing
        final Piece brook = new Piece.Builder()
                .entityDefinition((PieceDefinition) gameController.getLoaderController().getEntityCache().get("StandardChessPieces").get("rook"))
                .gameId(1)
                .playerColor(PlayerColor.BLACK)
                .build();

        final Piece brook2 = new Piece.Builder()
                .entityDefinition((PieceDefinition) gameController.getLoaderController().getEntityCache().get("StandardChessPieces").get("rook"))
                .gameId(2)
                .playerColor(PlayerColor.BLACK)
                .build();

        final Piece bknight = new Piece.Builder()
                .entityDefinition((PieceDefinition) gameController.getLoaderController().getEntityCache().get("StandardChessPieces").get("knight"))
                .gameId(3)
                .playerColor(PlayerColor.BLACK)
                .build();

        final Piece bknight2 = new Piece.Builder()
                .entityDefinition((PieceDefinition) gameController.getLoaderController().getEntityCache().get("StandardChessPieces").get("knight"))
                .gameId(4)
                .playerColor(PlayerColor.BLACK)
                .build();

        final Piece bbishop = new Piece.Builder()
                .entityDefinition((PieceDefinition) gameController.getLoaderController().getEntityCache().get("StandardChessPieces").get("bishop"))
                .gameId(5)
                .playerColor(PlayerColor.BLACK)
                .build();

        final Piece bbishop2 = new Piece.Builder()
                .entityDefinition((PieceDefinition) gameController.getLoaderController().getEntityCache().get("StandardChessPieces").get("bishop"))
                .gameId(6)
                .playerColor(PlayerColor.BLACK)
                .build();

        final Piece bqueen = new Piece.Builder()
                .entityDefinition((PieceDefinition) gameController.getLoaderController().getEntityCache().get("StandardChessPieces").get("queen"))
                .gameId(7)
                .playerColor(PlayerColor.BLACK)
                .build();

        final Piece bking = new Piece.Builder()
                .entityDefinition((PieceDefinition) gameController.getLoaderController().getEntityCache().get("StandardChessPieces").get("king"))
                .gameId(8)
                .playerColor(PlayerColor.BLACK)
                .build();


        final Piece wking = new Piece.Builder()
                .entityDefinition((PieceDefinition) gameController.getLoaderController().getEntityCache().get("StandardChessPieces").get("king"))
                .gameId(9)
                .playerColor(PlayerColor.WHITE)
                .build();

        final Piece wbishop = new Piece.Builder()
                .entityDefinition((PieceDefinition) gameController.getLoaderController().getEntityCache().get("StandardChessPieces").get("bishop"))
                .gameId(10)
                .playerColor(PlayerColor.WHITE)
                .build();

        final Piece wbishop2 = new Piece.Builder()
                .entityDefinition((PieceDefinition) gameController.getLoaderController().getEntityCache().get("StandardChessPieces").get("bishop"))
                .gameId(11)
                .playerColor(PlayerColor.WHITE)
                .build();

        final Piece wknight = new Piece.Builder()
                .entityDefinition((PieceDefinition) gameController.getLoaderController().getEntityCache().get("StandardChessPieces").get("knight"))
                .gameId(12)
                .playerColor(PlayerColor.WHITE)
                .build();

        final Piece wknight2 = new Piece.Builder()
                .entityDefinition((PieceDefinition) gameController.getLoaderController().getEntityCache().get("StandardChessPieces").get("knight"))
                .gameId(13)
                .playerColor(PlayerColor.WHITE)
                .build();

        final Piece wrook = new Piece.Builder()
                .entityDefinition((PieceDefinition) gameController.getLoaderController().getEntityCache().get("StandardChessPieces").get("rook"))
                .gameId(14)
                .playerColor(PlayerColor.WHITE)
                .build();

        final Piece wrook2 = new Piece.Builder()
                .entityDefinition((PieceDefinition) gameController.getLoaderController().getEntityCache().get("StandardChessPieces").get("rook"))
                .gameId(15)
                .playerColor(PlayerColor.WHITE)
                .build();

        final Piece wqueen = new Piece.Builder()
                .entityDefinition((PieceDefinition) gameController.getLoaderController().getEntityCache().get("StandardChessPieces").get("queen"))
                .gameId(16)
                .playerColor(PlayerColor.WHITE)
                .build();

        this.match.getBoard().setEntity(new Point2D(0, 0), brook);
        this.match.getBoard().setEntity(new Point2D(0, 0), brook2);
        this.match.getBoard().setEntity(new Point2D(4, BKING_POS_Y), bking);
        this.match.getBoard().setEntity(new Point2D(2, 0), bbishop);
        this.match.getBoard().setEntity(new Point2D(5, 0), bbishop2);
        this.match.getBoard().setEntity(new Point2D(1, 0), bknight);
        this.match.getBoard().setEntity(new Point2D(6, 0), bknight2);
        this.match.getBoard().setEntity(new Point2D(3, 0), bqueen);
        this.match.getBoard().setEntity(new Point2D(4, WKING_POS_Y), wking);
        this.match.getBoard().setEntity(new Point2D(2, 7), wbishop);
        this.match.getBoard().setEntity(new Point2D(5, 7), wbishop2);
        this.match.getBoard().setEntity(new Point2D(0, 7), wrook);
        this.match.getBoard().setEntity(new Point2D(7, 7), wrook2);
        this.match.getBoard().setEntity(new Point2D(1, 7), wknight);
        this.match.getBoard().setEntity(new Point2D(6, 7), wknight2);
        this.match.getBoard().setEntity(new Point2D(3, 7), wqueen);
    }
}
