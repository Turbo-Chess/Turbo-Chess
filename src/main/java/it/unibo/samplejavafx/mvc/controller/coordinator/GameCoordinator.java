package it.unibo.samplejavafx.mvc.controller.coordinator;

import ch.qos.logback.core.util.Loader;
import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameController;
import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameControllerImpl;
import it.unibo.samplejavafx.mvc.controller.loadercontroller.LoaderController;
import it.unibo.samplejavafx.mvc.controller.uicontroller.ChessboardViewController;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatch;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatchImpl;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PieceDefinition;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class GameCoordinator {
    private final FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/GameLayout.fxml"));
    private final Stage stage;
    private ChessMatch match = new ChessMatchImpl();
    private GameController gameController = new GameControllerImpl();

    public GameCoordinator(final Stage stage) {
        this.stage = stage;
    }

    public void loadPieces() {
        gameController.getLoaderController().load();
        System.out.println(gameController.getLoaderController().getEntityCache());
    }

    public void initGame() throws IOException {
        final ChessMatch match = new ChessMatchImpl();
        Parent root = loader.load();
        final ChessboardViewController viewController = loader.getController();
        viewController.setMatch(this.match);
        viewController.setGameController(this.gameController);

        var cssLocation = getClass().getResource("/css/GameLayout.css");
        Scene scene = new Scene(root, 800, 800);
        stage.setTitle("TurboChess - Game");
        stage.setScene(scene);
        scene.getStylesheets().add(cssLocation.toExternalForm());
        stage.show();
        //Only for testing
        final PieceDefinition p = new PieceDefinition.Builder()
                .name("test-piece")
                .id("test")
                .imagePath("/home/giacomo/Documents/pawn.jpg")
                .weight(3)
                .pieceType(PieceType.INFERIOR)
                .moveRules(List.of(new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING)))
                .build();
        final Piece piece = new Piece.Builder()
                .entityDefinition(p)
                .gameId(1)
                .playerColor(PlayerColor.BLACK)
                .build();
        this.match.getBoard().setEntity(new Point2D(1, 1), piece);
    }
}
