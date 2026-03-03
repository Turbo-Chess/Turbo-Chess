package it.unibo.samplejavafx.mvc.controller.uicontroller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.io.File;
import java.util.function.Consumer;

/**
 * Controller for a single save slot in the Load Game view.
 */
public final class SaveSlotController {

    @FXML
    private Label saveNameLabel;

    private File saveFile;
    private Consumer<File> onLoadAction;
    private Consumer<File> onDeleteAction;

    /**
     * Sets the save file for this slot.
     *
     * @param saveFile the save file.
     */
    public void setSaveFile(final File saveFile) {
        this.saveFile = saveFile;
        if (saveFile != null) {
            this.saveNameLabel.setText(saveFile.getName().replace(".json", ""));
        }
    }

    /**
     * Sets the action to perform when the Load button is clicked.
     *
     * @param onLoadAction the action.
     */
    public void setOnLoadAction(final Consumer<File> onLoadAction) {
        this.onLoadAction = onLoadAction;
    }

    /**
     * Sets the action to perform when the Delete button is clicked.
     *
     * @param onDeleteAction the action.
     */
    public void setOnDeleteAction(final Consumer<File> onDeleteAction) {
        this.onDeleteAction = onDeleteAction;
    }

    @FXML
    @SuppressWarnings("PMD.UnusedPrivateMethod")
    private void onLoad() {
        if (onLoadAction != null && saveFile != null) {
            onLoadAction.accept(saveFile);
        }
    }

    @FXML
    @SuppressWarnings("PMD.UnusedPrivateMethod")
    private void onDelete() {
        if (onDeleteAction != null && saveFile != null) {
            onDeleteAction.accept(saveFile);
        }
    }
}
