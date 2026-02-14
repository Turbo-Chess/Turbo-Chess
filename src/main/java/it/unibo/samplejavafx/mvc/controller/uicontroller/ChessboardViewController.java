package it.unibo.samplejavafx.mvc.controller.uicontroller;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameController;
import it.unibo.samplejavafx.mvc.model.chessboard.BoardObserver;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatch;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatchImpl;
import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.handler.TurnHandler;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ChessboardViewController implements BoardObserver {
    private ChessMatch match = new ChessMatchImpl();
    @Setter
    private GameController gameController;
    private final BiMap<Point2D, Button> cells = HashBiMap.create();

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

                button.setOnAction(event -> {
                    //TODO: mettere il codice del turn handler
                    System.out.println("codice che chiama il turn handler");
                    //Only for testing
                    match.getBoard().removeEntity(cells.inverse().get(event.getSource()));
                });

                button.getStyleClass().add("material-surface");
                button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                //button.setText("x: " + col + " y: " + row);
                cells.put(new Point2D(col, row), button);
                chessboardGridPane.add(button, col, row);
            }
        }
    }

    public void setMatch(final ChessMatch match) {
        this.match = match;
        this.match.getBoard().addObserver(this);
    }

    @Override
    public void onEntityAdded(Point2D pos, Entity entity) {
        System.out.println("Added Entity: " + entity.getClass() + " at pos: " + pos);
        final Button btn = cells.get(pos);
        btn.setText(entity.getName());
        btn.setText("");
        btn.setGraphic(new ImageView(new Image("file:" + "/home/giacomo/Documents/pawn.jpg", 120, 120, true, true)));
    }

    @Override
    public void onEntityRemoved(Point2D pos, Entity entity) {
        final Button btn = cells.get(pos);
        btn.setText("");
        btn.setGraphic(null);
    }

}
