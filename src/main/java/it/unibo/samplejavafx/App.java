package it.unibo.samplejavafx;

import it.unibo.samplejavafx.mvc.controller.coordinator.GameCoordinator;
import it.unibo.samplejavafx.mvc.controller.uicontroller.MainMenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    private static final String layoutsFolder = "src/main/resources/layouts/";
    private FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/MainMenu.fxml"));
    private GameCoordinator coordinator;
    @Override
    public void start(Stage stage) throws Exception {
//        var fxmlLocation = getClass().getResource( "/layouts/MainMenu.fxml");
//        if (fxmlLocation == null) {
//            throw new RuntimeException("CRITICAL: MainMenu.fxml not found in the classpath!");
//        }
        coordinator = new GameCoordinator(stage);
        Parent root = loader.load();
        Scene scene = new Scene(root, 800, 800);

        var cssLocation = getClass().getResource("/css/MainMenu.css");
        if (cssLocation != null) {
            scene.getStylesheets().add(cssLocation.toExternalForm());
        } else {
            System.err.println("Warning: MainMenu.css not found, loading without styles.");
        }

        final MainMenuController controller = loader.getController();
        controller.setCoordinator(coordinator);

        stage.setTitle("TurboChess - Main Menù");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
