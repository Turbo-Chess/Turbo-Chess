package it.unibo.samplejavafx.mvc.controller.uicontroller;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameController;
import it.unibo.samplejavafx.mvc.model.chessboard.BoardObserver;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatch;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatchImpl;
import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

import static it.unibo.samplejavafx.mvc.view.ChessboardViewPseudoClasses.VALID_MOVEMENT_CELL;

/**
 * placeholder.
 */
public final class ChessboardViewControllerImpl implements ChessboardViewController, BoardObserver {
    private static final int IMAGE_SIZE = 120;
    private static final Logger LOGGER = LoggerFactory.getLogger(ChessboardViewControllerImpl.class);

    // TODO: remove reference of the match in the view controller
    @Setter
    private GameController gameController;
    private final BiMap<Point2D, Button> cells = HashBiMap.create();


    @FXML
    private GridPane chessboardGridPane;

    /**
     * placeholder.
     */
    @FXML
    public void initialize() {
        initChessboardPane();
    }

    /**
     * placeholder.
     */
    @FXML
    public void initChessboardPane() {
        final int size = 8;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                final Button button = new Button();

                button.setOnAction(event -> {
                    // Lista:
                    // Vuota: click fa schifo
                    // Piena: mosse da mostrare
                    // Una sola (che deve essere la stessa cliccata): chiamato eat o move
                    final Point2D pointClicked = cells.inverse().get((Button) event.getSource());
                    gameController.handleClick(pointClicked);


                });

                button.getStyleClass().add("material-surface");
                button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                //button.setText("x: " + col + " y: " + row);
                cells.put(new Point2D(col, row), button);
                chessboardGridPane.add(button, col, row);
            }
        }
    }

    @Override
    public void onEntityAdded(final Point2D pos, final Entity entity) {
        LOGGER.debug("Added Entity: " + entity.getClass() + " at pos: " + pos);
        final Button btn = cells.get(pos);
        btn.setText(entity.getName());
        btn.setText("");
        btn.setGraphic(new ImageView(new Image("file:" + entity.getImagePath(),
                IMAGE_SIZE, IMAGE_SIZE, true, true)));
    }

    @Override
    public void onEntityRemoved(final Point2D pos, final Entity entity) {
        final Button btn = cells.get(pos);
        btn.setText("");
        btn.setGraphic(null);
    }

    @Override
     public void showMovementCells(final Set<Point2D> cellsToShow) {
        for (final var move : cellsToShow) {
            cells.get(move).pseudoClassStateChanged(VALID_MOVEMENT_CELL, true);
        }
    }

   @Override
    public void hideMovementCells(final Set<Point2D> cellsToHide) {
        for (final var move : cellsToHide) {
            cells.get(move).pseudoClassStateChanged(VALID_MOVEMENT_CELL, false);
        }
    }
}
