package it.unibo.samplejavafx.mvc.controller.uicontroller;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import it.unibo.samplejavafx.mvc.controller.coordinator.GameCoordinator;
import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameController;
import it.unibo.samplejavafx.mvc.model.chessboard.BoardObserver;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatchObserver;
import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.handler.GameState;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static it.unibo.samplejavafx.mvc.view.ChessboardViewPseudoClasses.VALID_MOVEMENT_CELL;

/**
 * placeholder.
 */
public final class ChessboardViewControllerImpl implements ChessboardViewController, BoardObserver, ChessMatchObserver {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChessboardViewControllerImpl.class);
    private static final double IMAGE_SCALE = 0.8;
    // TODO: modificare le label già presenti per essere statiche ed aggiungere quelle da bindare con i valori
    private final GameController gameController;
    private final GameCoordinator coordinator;
    private final BiMap<Point2D, Button> cells = HashBiMap.create();

    /**
     * placeholder.
     *
     * @param gameController placeholder.
     * @param coordinator placeholder.
     */
    public ChessboardViewControllerImpl(final GameController gameController, final GameCoordinator coordinator) {
        this.gameController = gameController;
        this.coordinator = coordinator;
    }

    @FXML
    private GridPane chessboardGridPane;

    @FXML
    private StackPane gameMainPane;

    @FXML
    private Label turnValueLabel;

    @FXML
    private Label playerColorValueLabel;

    @FXML
    private Label timeValueLabel;

    @FXML
    private Button menuButton;

    /**
     * placeholder.
     */
    @FXML
    public void initialize() {
        initChessboardPane();
        menuButton.setText("Surrender");
        menuButton.setOnAction(e -> coordinator.initMainMenu());
    }

    /**
     * placeholder.
     */
    @FXML
    public void initChessboardPane() {
        final int size = 8;

        // Bind GridPane size to the minimum of StackPane width/height to keep it square
        final javafx.beans.binding.NumberBinding squareSize = javafx.beans.binding.Bindings.min(
            gameMainPane.widthProperty(), gameMainPane.heightProperty()
        );
        chessboardGridPane.prefWidthProperty().bind(squareSize);
        chessboardGridPane.prefHeightProperty().bind(squareSize);
        chessboardGridPane.maxWidthProperty().bind(squareSize);
        chessboardGridPane.maxHeightProperty().bind(squareSize);

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                final Button button = new Button();

                button.setOnAction(event -> {
                    final Point2D pointClicked = cells.inverse().get((Button) event.getSource());
                    gameController.handleClick(pointClicked);
                });

                button.getStyleClass().add("material-surface");
                button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                cells.put(new Point2D(col, row), button);
                chessboardGridPane.add(button, col, row);
            }
        }

        turnValueLabel.setText("1");
        playerColorValueLabel.setText("WHITE");
    }

    /**
     * Creates an ImageView whose size tracks the button's actual size.
     *
     * @param imagePath path to the image file.
     * @param button    the button to track.
     * @return a responsive ImageView.
     */
    private ImageView createResponsiveImageView(final String imagePath, final Button button) {
        final ImageView imageView = new ImageView(new Image(imagePath));
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.fitHeightProperty().bind(button.heightProperty().multiply(IMAGE_SCALE));
        imageView.fitWidthProperty().bind(button.widthProperty().multiply(IMAGE_SCALE));
        return imageView;
    }

    @Override
    public void onEntityAdded(final Point2D pos, final Entity entity) {
        LOGGER.debug("Added Entity: " + entity.getClass() + " at pos: " + pos);
        final Button btn = cells.get(pos);
        btn.setText("");
        btn.setGraphic(createResponsiveImageView("file:" + entity.getImagePath(), btn));
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

    @Override
    public void onTurnUpdated(final int turnNumber) {
        turnValueLabel.setText(String.valueOf(turnNumber));
    }

    @Override
    public void onPlayerUpdated(final PlayerColor playerColor) {
        playerColorValueLabel.setText(String.valueOf(playerColor));
    }

    @Override
    public void onGameStateUpdated(final GameState gameState) {
        // TODO: set label
    }
}
