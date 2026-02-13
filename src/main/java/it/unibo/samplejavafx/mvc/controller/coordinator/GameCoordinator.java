package it.unibo.samplejavafx.mvc.controller.coordinator;

import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameController;
import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameControllerImpl;
import it.unibo.samplejavafx.mvc.controller.uicontroller.ChessboardViewController;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatch;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatchImpl;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GameCoordinator {
    private final FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/GameLayout.fxml"));
    private final Stage stage;
    private ChessMatch match = new ChessMatchImpl();
    private GameController gameController = new GameControllerImpl();

    public GameCoordinator(final Stage stage) {
        this.stage = stage;
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
    }
}
