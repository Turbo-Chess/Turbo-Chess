package it.unibo.samplejavafx.mvc.controller.uicontroller;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.samplejavafx.mvc.controller.coordinator.GameCoordinatorImpl;
import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameController;
import it.unibo.samplejavafx.mvc.controller.replay.ReplayController;
import it.unibo.samplejavafx.mvc.controller.replay.ReplayControllerImpl;
import it.unibo.samplejavafx.mvc.model.chessboard.BoardObserver;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoardImpl;
import it.unibo.samplejavafx.mvc.model.chessmatch.ChessMatchObserver;
import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.handler.GameState;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import it.unibo.samplejavafx.mvc.model.properties.GameProperties;
import it.unibo.samplejavafx.mvc.model.replay.MoveEvent;
import it.unibo.samplejavafx.mvc.model.replay.SpawnEvent;
import it.unibo.samplejavafx.mvc.model.utils.FileSystemUtils;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @FXML
    private Button saveButton;

    @FXML
    private ToggleButton toggleReplayBtn;

    @FXML
    private ListView<String> historyListView;

    @FXML
    private HBox replayControlsBox;

    @FXML
    private Button btnStart;

    @FXML
    private Button btnPrev;

    @FXML
    private Button btnNext;

    @FXML
    private Button btnEnd;

    // to-do: modificare le label già presenti per essere statiche ed aggiungere quelle da bindare con i valori
    private final GameController gameController;
    private final GameCoordinatorImpl coordinator;
    private final BiMap<Point2D, Button> cells = HashBiMap.create();
    private Point2D lastStart;
    private Point2D lastEnd;

    // Replay related fields
    private boolean isReplayMode;
    private ReplayController replayController;
    private ChessBoardImpl replayBoard;

    /**
     * placeholder.
     *
     * @param gameController placeholder.
     * @param coordinator placeholder.
     */
    // This is intended to be a shared controller to make the MVC working.
    @SuppressFBWarnings("EI_EXPOSE_REP2")
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

        saveButton.setText("Save Game");
        saveButton.setOnAction(e -> {
            Path fileToSave = coordinator.getCurrentSaveFile();

            if (fileToSave == null) {
                final Path saveDir = Paths.get(GameProperties.SAVES_FOLDER.getPath());
                try {
                    FileSystemUtils.ensureDirectoryExists(saveDir);
                    final String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                    final String uuid = UUID.randomUUID().toString().substring(0, 8);
                    final String filename = "save_" + timestamp + "_" + uuid + ".json";
                    fileToSave = saveDir.resolve(filename);
                } catch (final IOException ex) {
                    LOGGER.error("Failed to create save directory", ex);
                    return;
                }
            }

            try {
                if (coordinator.saveGame(fileToSave)) {
                    LOGGER.info("Game saved to: " + fileToSave.toAbsolutePath());
                    coordinator.initMainMenu();
                } else {
                    LOGGER.error("Failed to save game to: " + fileToSave.toAbsolutePath() + " (unknown reason)");
                }
            } catch (final IOException ex) {
                LOGGER.error("Failed to save game", ex);
            }
        });

        this.replayBoard = new ChessBoardImpl();
        this.replayController = new ReplayControllerImpl(this.replayBoard);

        toggleReplayBtn.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (Boolean.TRUE.equals(newVal)) {
                enableReplayMode();
            } else {
                disableReplayMode();
            }
        });

        btnStart.setOnAction(e -> {
            replayController.jumpToStart();
            syncReplayView();
        });
        btnPrev.setOnAction(e -> {
            replayController.prev();
            syncReplayView();
        });
        btnNext.setOnAction(e -> {
            replayController.next();
            syncReplayView();
        });
        btnEnd.setOnAction(e -> {
            replayController.jumpToEnd();
            syncReplayView();
        });
    }

    private void enableReplayMode() {
        isReplayMode = true;
        replayControlsBox.setDisable(false);

        final var history = gameController.getGameHistory();
        replayController.loadHistory(history);

        final int initialSpawnCount = (int) history.getEvents().stream()
            .takeWhile(event -> event instanceof SpawnEvent)
            .count();
        replayController.setMinIndex(initialSpawnCount);

        replayController.jumpToEnd();

        refreshBoardView(replayBoard);

        updateHistoryList();
    }

    private void disableReplayMode() {
        isReplayMode = false;
        replayControlsBox.setDisable(true);

        // Restore Live Board View
        refreshBoardView(gameController.getLiveBoard());
    }

    @Override
    public void refreshBoardView(final ChessBoard board) {
        cells.values().forEach(btn -> {
            btn.setGraphic(null);
            btn.setText("");
            btn.pseudoClassStateChanged(VALID_MOVEMENT_CELL, false);
            btn.pseudoClassStateChanged(VALID_CAPTURE_CELL, false);
            btn.pseudoClassStateChanged(START, false);
            btn.pseudoClassStateChanged(HASMOVED, false);
            btn.pseudoClassStateChanged(HASEAT, false);
            btn.pseudoClassStateChanged(CHECK_KING, false);
        });

        // Fill from board
        board.getBoard().forEach(this::setCellGraphic);
    }

    private void setCellGraphic(final Point2D pos, final Entity entity) {
         final Button btn = cells.get(pos);
         if (btn != null) {
            btn.setText("");
            btn.setGraphic(createResponsiveImageView(gameController.calculateImageColorPath(
                entity.getImagePath(), entity.getPlayerColor(), entity.getId()), btn));
        }
    }

    private void syncReplayView() {
        refreshBoardView(replayBoard);
    }

    private void updateHistoryList() {
        final var history = gameController.getGameHistory();
        historyListView.setItems(FXCollections.observableArrayList(
            history.getEvents().stream()
                .filter(e -> e instanceof MoveEvent)
                .map(e -> {
                    final MoveEvent me = (MoveEvent) e;
                    return me.getEventDescription();
                })
                .collect(Collectors.toList())
        ));
    }

    /**
     * placeholder.
     */
    @FXML
    public void initChessboardPane() {
        final int size = 8;

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
                    if (isReplayMode) {
                        return; // Disable interaction in replay mode
                    }

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
        if (isReplayMode && !isReplayBoardEvent()) {
             return;
        }

        final Button btn = cells.get(pos);
        if (btn != null) {
            btn.setText("");
            btn.setGraphic(createResponsiveImageView(gameController.calculateImageColorPath(
                entity.getImagePath(), entity.getPlayerColor(), entity.getId()), btn));
        }
    }

    private boolean isReplayBoardEvent() {
        return false;
    }

    @Override
    public void onEntityRemoved(final Point2D pos, final Entity entity) {
        if (isReplayMode) {
            return;
        }

        final Button btn = cells.get(pos);
        btn.setText("");
        btn.setGraphic(null);
    }

    @Override
    public void onEntityMoved(final Point2D from, final Point2D to, final Entity entity) {
        onEntityRemoved(from, entity);
        onEntityAdded(to, entity);
    }

    @Override
    public void onEntityMoved(final Point2D from, final Point2D to) {
        if (isReplayMode) {
            return;
        }
        highlightMovement(from, to);
    }

    @Override
    public void showMovementCells(final Set<Point2D> cellsToShow) {
        if (isReplayMode) {
            return;
        }

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
        if (isReplayMode) {
            return;
        }

        for (final var move : cellsToHide) {
            final Button btn = cells.get(move);
            btn.pseudoClassStateChanged(VALID_MOVEMENT_CELL, false);
            btn.pseudoClassStateChanged(VALID_CAPTURE_CELL, false);
        }
    }

    @Override
    public void highlightMovement(final Point2D start, final Point2D end) {
        if (isReplayMode) {
            return;
        }

        clearLastHighlight();
        cells.get(start).pseudoClassStateChanged(START, true);
        cells.get(end).pseudoClassStateChanged(HASMOVED, true);
        this.lastStart = start;
        this.lastEnd = end;
    }

    @Override
    public void highlightEat(final Point2D start, final Point2D end) {
        if (isReplayMode) {
            return;
        }

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
    public void onTurnUpdated(final int turnNumber) {
        if (isReplayMode) {
            return;
        }
        turnValueLabel.setText(String.valueOf(turnNumber));
        updateHistoryList();
    }

    @Override
    public void onPlayerUpdated(final PlayerColor playerColor) {
        if (isReplayMode) {
            return;
        }
        playerColorValueLabel.setText(String.valueOf(playerColor));
    }

    @Override
    public void onGameStateUpdated(final GameState gameState, final PlayerColor playerColor) {
        if (isReplayMode) {
            return;
        }

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
