package it.unibo.samplejavafx.mvc.controller.uicontroller;

import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameController;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatch;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatchImpl;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class ChessboardViewController {
    @Setter
    private ChessMatch match = new ChessMatchImpl();
    @Setter
    private GameController gameController;
    private final Map<Point2D, Button> cells = new HashMap<>();

    @FXML
    private GridPane chessboardGridPane;

    @FXML
    public void initialize() {
        initChessboardPane();
    }

    @FXML
    public void initChessboardPane() {
        final int size = 8;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Button button = new Button();

                button.getStyleClass().add("material-surface");
                button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                button.setText("x: " + col + " y: " + row);
                cells.put(new Point2D(col, row), button);
                chessboardGridPane.add(button, col, row);
            }
        }
    }
}
