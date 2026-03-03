package it.unibo.samplejavafx.mvc.controller.uicontroller;

import it.unibo.samplejavafx.mvc.controller.coordinator.GameCoordinator;
import it.unibo.samplejavafx.mvc.model.properties.GameProperties;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Controller for the custom Load Game view.
 */
public final class LoadGameController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoadGameController.class);
    private final GameCoordinator coordinator;

    @FXML
    private VBox savesContainer;

    /**
     * Constructor.
     *
     * @param coordinator the game coordinator.
     */
    public LoadGameController(final GameCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    /**
     * Initializes the controller and loads the save list.
     */
    @FXML
    public void initialize() {
        loadSaves();
    }

    private void loadSaves() {
        savesContainer.getChildren().clear();
        final File saveDir = new File(GameProperties.SAVES_FOLDER.getPath());

        if (saveDir.exists() && saveDir.isDirectory()) {
            final File[] files = saveDir.listFiles((dir, name) -> name.endsWith(".json"));

            if (files != null) {
                // Sort by last modified (newest first)
                Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());

                for (final File file : files) {
                    addSaveSlot(file);
                }
            }
        } else {
            LOGGER.warn("Save directory not found or not a directory: {}", saveDir.getAbsolutePath());
        }
    }

    private void addSaveSlot(final File file) {
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/SaveSlot.fxml"));
            final Node slot = loader.load();
            final SaveSlotController controller = loader.getController();

            controller.setSaveFile(file);
            controller.setOnLoadAction(this::onLoadGame);
            controller.setOnDeleteAction(this::onDeleteGame);

            savesContainer.getChildren().add(slot);
        } catch (final IOException e) {
            LOGGER.error("Failed to load save slot for file: {}", file.getName(), e);
        }
    }

    private void onLoadGame(final File file) {
        LOGGER.info("Loading game from: {}", file.getName());
        this.coordinator.loadGame(file.toPath());
    }

    private void onDeleteGame(final File file) {
        if (file.delete()) {
            LOGGER.info("Deleted save file: {}", file.getName());
            loadSaves();
        } else {
            LOGGER.error("Failed to delete save file: {}", file.getName());
        }
    }

    /**
     * Handles the back button action.
     */
    @FXML
    public void onBack() {
        this.coordinator.initMainMenu();
    }
}
