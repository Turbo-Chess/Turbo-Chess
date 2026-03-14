package it.unibo.samplejavafx.mvc.controller.uicontroller;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.samplejavafx.mvc.controller.coordinator.GameCoordinator;
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
import javafx.application.Platform;
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
 * A concrete implementation of the {@link ChessboardViewController} interface responsible for managing the
 * JavaFX view of the chessboard.
 *
 * <p>
 * This class coordinates user interaction with the board (clicks), updates the visual state based on model changes,
 * and handles UI-related game logic such as highlighting moves, displaying turn information, and managing
 * replay controls.
 * </p>
 *
 * <p>
 * It acts as an observer for both the {@link ChessMatchObserver} and {@link BoardObserver}, ensuring
 * real-time synchronization between the game state and the UI.
 * </p>
 */
public final class ChessboardViewControllerImpl implements ChessboardViewController, BoardObserver, ChessMatchObserver {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChessboardViewControllerImpl.class);
    private static final String PLUS_SIGN = "+";
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
    private Label whiteScoreLabel;

    @FXML
    private Label blackScoreLabel;

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

    private final GameController gameController;
    private final GameCoordinator coordinator;
    private final BiMap<Point2D, Button> cells = HashBiMap.create();
    private Point2D lastStart;
    private Point2D lastEnd;

    // Replay related fields
    private boolean isReplayMode;
    private ReplayController replayController;
    private ChessBoard replayBoard;

    /**
     * Constructs a new {@code ChessboardViewControllerImpl}.
     *
     * @param gameController The central {@link GameController} mediating game logic.
     * @param coordinator    The {@link GameCoordinator} managing high-level application flow.
     */
    // This is intended to be a shared controller to make the MVC working.
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public ChessboardViewControllerImpl(final GameController gameController, final GameCoordinator coordinator) {
        this.gameController = gameController;
        this.coordinator = coordinator;
    }

    /**
     * Initializes the controller after its root element has been completely processed.
     *
     * <p>
     * Sets up event handlers for UI components, initializes the board grid, and configures
     * the replay mode controls.
     * </p>
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
            if (coordinator.saveGame(fileToSave)) {
                LOGGER.info("Game saved to: " + fileToSave.toAbsolutePath());
                coordinator.initMainMenu();
            } else {
                LOGGER.error("Failed to save game to: " + fileToSave.toAbsolutePath() + " (unknown reason)");
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
            updateReplayScore(0, 0); // At start, score is always 0
        });
        btnPrev.setOnAction(e -> {
            replayController.prev().ifPresent(event -> {
                syncReplayView();
                // When going back, we want the score of the event *before* the one we just reverted.
                // Or if we are at the beginning (index 0), score is 0.
                // Wait, if we revert the event at index i, we are now at state after event i-1.
                final int idx = replayController.getCurrentIndex();
                if (idx == 0) {
                    updateReplayScore(0, 0);
                } else {
                    final var prevEvent = gameController.getGameHistory().getEvents().get(idx - 1);
                    updateReplayScore(prevEvent.getWhiteScore(), prevEvent.getBlackScore());
                }
            });
        });
        btnNext.setOnAction(e -> {
            replayController.next().ifPresent(event -> {
                syncReplayView();
                updateReplayScore(event.getWhiteScore(), event.getBlackScore());
            });
        });
        btnEnd.setOnAction(e -> {
            replayController.jumpToEnd();
            syncReplayView();
            final var events = gameController.getGameHistory().getEvents();
            if (!events.isEmpty()) {
                final var lastEvent = events.get(events.size() - 1);
                updateReplayScore(lastEvent.getWhiteScore(), lastEvent.getBlackScore());
            }
        });
        onTimerUpdated(PlayerColor.WHITE, gameController.getMatch().getGameTimer().getTimeRemaining(PlayerColor.WHITE));
    }

    private void enableReplayMode() {
        isReplayMode = true;
        replayControlsBox.setDisable(false);
        gameController.getMatch().getGameTimer().stop();

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
        final var match = gameController.getMatch();
        if (match.getGameState() != GameState.CHECKMATE
                && match.getGameState() != GameState.DRAW
                && match.getGameState() != GameState.TIMEOUT) {
            match.getGameTimer().start();
        }

        // Restore Live Board View
        refreshBoardView(gameController.getLiveBoard());
    }

    /**
     * {@inheritDoc}
     */
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

    private void updateReplayScore(final int whiteScore, final int blackScore) {
        final int diff = whiteScore - blackScore;
        if (diff >= 0) {
            whiteScoreLabel.setText(PLUS_SIGN + diff);
            blackScoreLabel.setText(String.valueOf(-diff));
        } else {
            whiteScoreLabel.setText(String.valueOf(diff));
            blackScoreLabel.setText(PLUS_SIGN + Math.abs(diff));
        }
    }

    /**
     * Initializes the chessboard grid pane with buttons and binds their size to the window.
     *
     * <p>
     * This method creates a 8x8 grid of buttons, setting up their event handlers for user interaction
     * and styling. It also initializes the turn and player labels.
     * </p>
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
        //TODO: add exception if file doesn't exist
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.fitHeightProperty().bind(button.heightProperty().multiply(IMAGE_SCALE));
        imageView.fitWidthProperty().bind(button.widthProperty().multiply(IMAGE_SCALE));
        return imageView;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEntityAdded(final Point2D pos, final Entity entity) {
        if (isReplayMode && !isReplayBoardEvent()) {
             return;
        }

        final Button btn = cells.get(pos);
        if (btn != null) {
            btn.setText("");
            final var imagePath = gameController.calculateImageColorPath(
                    entity.getImagePath(), entity.getPlayerColor(), entity.getId());
            btn.setGraphic(createResponsiveImageView(imagePath, btn));
        }
    }

    private boolean isReplayBoardEvent() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEntityRemoved(final Point2D pos, final Entity entity) {
        if (isReplayMode) {
            return;
        }

        final Button btn = cells.get(pos);
        btn.setText("");
        btn.setGraphic(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEntityMoved(final Point2D from, final Point2D to, final Entity entity) {
        onEntityRemoved(from, entity);
        onEntityAdded(to, entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEntityMoved(final Point2D from, final Point2D to) {
        if (isReplayMode) {
            return;
        }
        highlightMovement(from, to);
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTurnUpdated(final int turnNumber) {
        if (isReplayMode) {
            return;
        }
        turnValueLabel.setText(String.valueOf(turnNumber));
        updateHistoryList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPlayerUpdated(final PlayerColor playerColor) {
        if (isReplayMode) {
            return;
        }
        playerColorValueLabel.setText(String.valueOf(playerColor));
    }

    /**
     * {@inheritDoc}
     */
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

            case PROMOTION -> {
                coordinator.initPromotion();
            }

            case TIMEOUT -> this.showEndingDialog("Time's up!", " has won!", Optional.of(playerColor));
        }
    }

    @Override
    public void onScoreChanged(final PlayerColor player, final int newScore) {
        Platform.runLater(() -> {
            final var match = gameController.getMatch();
            final int whiteTotal = match.getScore(PlayerColor.WHITE);
            final int blackTotal = match.getScore(PlayerColor.BLACK);
            final int diff = whiteTotal - blackTotal;

            if (diff >= 0) {
                    whiteScoreLabel.setText(PLUS_SIGN + diff);
                    blackScoreLabel.setText(String.valueOf(-diff));
                } else {
                    whiteScoreLabel.setText(String.valueOf(diff));
                    blackScoreLabel.setText(PLUS_SIGN + Math.abs(diff));
                }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTimerUpdated(final PlayerColor player, final long timeRemaining) {
        Platform.runLater(() -> {
            final var match = gameController.getMatch();
            final var timer = match.getGameTimer();
            final long whiteTime = timer.getTimeRemaining(PlayerColor.WHITE);
            final long blackTime = timer.getTimeRemaining(PlayerColor.BLACK);

            final String whiteStr = formatTime(whiteTime);
            final String blackStr = formatTime(blackTime);

            timeValueLabel.setText("White: " + whiteStr + " - Black: " + blackStr);
        });
    }

    private String formatTime(final long seconds) {
        return String.format("%d:%02d", seconds / 60, seconds % 60);
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
        final var match = gameController.getMatch();
        final String scoreText = String.format("White: %d - Black: %d", 
            match.getScore(PlayerColor.WHITE), match.getScore(PlayerColor.BLACK));

        if (playerColor.isPresent()) {
            gameOverController.setTextLabel(statusText, playerColor.get() + messageText, scoreText);
        } else {
            gameOverController.setTextLabel(statusText, messageText, scoreText);
        }
        final Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(root);
        dialog.setTitle("Game Results");
        dialog.showAndWait();
    }
}
