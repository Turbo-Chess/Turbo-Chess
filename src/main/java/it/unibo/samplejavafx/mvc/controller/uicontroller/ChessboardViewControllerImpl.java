package it.unibo.samplejavafx.mvc.controller.uicontroller;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import it.unibo.samplejavafx.mvc.controller.coordinator.GameCoordinatorImpl;
import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameController;
import it.unibo.samplejavafx.mvc.model.chessboard.BoardObserver;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatchObserver;
import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.handler.GameState;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

import static it.unibo.samplejavafx.mvc.view.ChessboardViewPseudoClasses.CHECK_KING;
import static it.unibo.samplejavafx.mvc.view.ChessboardViewPseudoClasses.HASEAT;
import static it.unibo.samplejavafx.mvc.view.ChessboardViewPseudoClasses.HASMOVED;
import static it.unibo.samplejavafx.mvc.view.ChessboardViewPseudoClasses.START;
import static it.unibo.samplejavafx.mvc.view.ChessboardViewPseudoClasses.VALID_CAPTURE_CELL;
import static it.unibo.samplejavafx.mvc.view.ChessboardViewPseudoClasses.VALID_MOVEMENT_CELL;

/**
 * placeholder.
 */
public final class ChessboardViewControllerImpl implements ChessboardViewController, BoardObserver, ChessMatchObserver {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChessboardViewControllerImpl.class);
    private static final double IMAGE_SCALE = 0.8;
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

    // TODO: modificare le label già presenti per essere statiche ed aggiungere quelle da bindare con i valori
    private final GameController gameController;
    private final GameCoordinatorImpl coordinator;
    private final BiMap<Point2D, Button> cells = HashBiMap.create();
    private Point2D lastStart;
    private Point2D lastEnd;

    /**
     * placeholder.
     *
     * @param gameController placeholder.
     * @param coordinator placeholder.
     */
    public ChessboardViewControllerImpl(final GameController gameController, final GameCoordinatorImpl coordinator) {
        this.gameController = gameController;
        this.coordinator = coordinator;
    }

    /**
     * placeholder.
     */
    @FXML
    public void initialize() {
        initChessboardPane();
        menuButton.setText("Surrender");
        menuButton.setOnAction(e -> gameController.surrender());
    }

    /**
     * placeholder.
     */
    @FXML
    public void initChessboardPane() {
        final int size = 8;

        // Bind GridPane size to the minimum of StackPane width/height to keep it square
        final NumberBinding squareSize = Bindings.min(
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
        final Button btn = cells.get(pos);
        btn.setText("");
        btn.setGraphic(createResponsiveImageView(gameController.calculateImageColorPath(
                entity.getImagePath(), entity.getPlayerColor(), entity.getId()), btn));
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
            final Button btn = cells.get(move);
            if (btn.getGraphic() != null) {
                btn.pseudoClassStateChanged(VALID_CAPTURE_CELL, true);
            } else {
                btn.pseudoClassStateChanged(VALID_MOVEMENT_CELL, true);
            }
        }
    }

   @Override
    public void hideMovementCells(final Set<Point2D> cellsToHide) {
        for (final var move : cellsToHide) {
            final Button btn = cells.get(move);
            btn.pseudoClassStateChanged(VALID_MOVEMENT_CELL, false);
            btn.pseudoClassStateChanged(VALID_CAPTURE_CELL, false);
        }
    }

    @Override
    public void highlightMovement(final Point2D start, final Point2D end) {
        clearLastHighlight();
        cells.get(start).pseudoClassStateChanged(START, true);
        cells.get(end).pseudoClassStateChanged(HASMOVED, true);
        this.lastStart = start;
        this.lastEnd = end;
    }

    @Override
    public void highlightEat(final Point2D start, final Point2D end) {
        clearLastHighlight();
        cells.get(start).pseudoClassStateChanged(START, true);
        cells.get(end).pseudoClassStateChanged(HASEAT, true);
        this.lastStart = start;
        this.lastEnd = end;
    }

    private void clearLastHighlight() {
        if (lastStart != null && lastEnd != null) {
            cells.get(lastStart).pseudoClassStateChanged(START, false);
            cells.get(lastEnd).pseudoClassStateChanged(HASMOVED, false);
            cells.get(lastEnd).pseudoClassStateChanged(HASEAT, false);
        }
    }

    @Override
    public void onEntityMoved(final Point2D from, final Point2D to) {
        highlightMovement(from, to);
    }

    @Override
    public void onEntityEaten(final Point2D from, final Point2D to) {
        highlightEat(from, to);
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
    public void onGameStateUpdated(final GameState gameState, final PlayerColor playerColor) {
        switch (gameState) {
            case CHECK, DOUBLE_CHECK -> {
                cells.get(gameController.getKingPos()).pseudoClassStateChanged(CHECK_KING, true);
            }

            case NORMAL -> {
                final var check = cells.inverse().keySet().stream()
                        .filter(b -> b.getPseudoClassStates().contains(CHECK_KING))
                        .findFirst();
                check.ifPresent(button -> button.pseudoClassStateChanged(CHECK_KING, false));

            }

            case CHECKMATE -> this.showEndingDialog("Checkmate!", " has won!", Optional.of(playerColor));

            case DRAW -> this.showEndingDialog("It's a draw!", "Neither player won", Optional.empty());

            /*case PROMOTION -> {
                // Finestra promozione
            }*/
        }
    }

    private void showEndingDialog(final String statusText, final String messageText, final Optional<PlayerColor> playerColor) {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/GameOver.fxml"));
        loader.setControllerFactory(c -> new GameOverController(coordinator));
        DialogPane root = new DialogPane();
        try {
            root = loader.load();
        } catch (final IOException e) {
            LOGGER.error("Failed to load GameOver dialog", e);
        }

        final GameOverController gameOverController = loader.getController();
        if (playerColor.isPresent()) {
            gameOverController.setTextLabel(statusText, playerColor.get() + messageText);
        } else {
            gameOverController.setTextLabel(statusText, messageText);
        }
        final Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(root);
        dialog.setTitle("Game Results");
        dialog.showAndWait();
    }
}
