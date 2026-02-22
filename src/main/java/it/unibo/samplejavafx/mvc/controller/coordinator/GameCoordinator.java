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
    private static final int KING_POS_X = 6;
    private static final int KING_POS_Y = 7;
    private static final Logger LOGGER = LoggerFactory.getLogger(GameCoordinator.class);

    private final FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/GameLayout.fxml"));
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
     * placeholder.
     *
     * @throws IOException placeholder.
     */
    public void initGame() throws IOException {
        loader.setControllerFactory(c -> new ChessboardViewControllerImpl(this.gameController));
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
        final Piece rook = new Piece.Builder()
                .entityDefinition((PieceDefinition) gameController.getLoaderController().getEntityCache().get("StandardChessPieces").get("rook"))
                .gameId(1)
                .playerColor(PlayerColor.BLACK)
                .build();

        final Piece newPiece = new Piece.Builder()
                .entityDefinition((PieceDefinition) gameController.getLoaderController().getEntityCache().get("TestPack").get("new"))
                .gameId(5)
                .playerColor(PlayerColor.BLACK)
                .build();

        final Piece king = new Piece.Builder()
                .entityDefinition((PieceDefinition) gameController.getLoaderController().getEntityCache().get("StandardChessPieces").get("king"))
                .gameId(2)
                .playerColor(PlayerColor.WHITE)
                .build();

        final Piece bishop = new Piece.Builder()
                .entityDefinition((PieceDefinition) gameController.getLoaderController().getEntityCache().get("StandardChessPieces").get("bishop"))
                .gameId(3)
                .playerColor(PlayerColor.WHITE)
                .build();

        final Piece bishop2 = new Piece.Builder()
                .entityDefinition((PieceDefinition) gameController.getLoaderController().getEntityCache().get("StandardChessPieces").get("bishop"))
                .gameId(4)
                .playerColor(PlayerColor.WHITE)
                .build();

        this.match.getBoard().setEntity(new Point2D(1, 1), rook);
        this.match.getBoard().setEntity(new Point2D(KING_POS_X, KING_POS_Y), king);
        this.match.getBoard().setEntity(new Point2D(4, 4), bishop);
        this.match.getBoard().setEntity(new Point2D(5, 3), bishop2);
        this.match.getBoard().setEntity(new Point2D(5, 5), newPiece);
    }
}
