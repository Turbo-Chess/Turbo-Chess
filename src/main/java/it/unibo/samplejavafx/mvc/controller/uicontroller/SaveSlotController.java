package it.unibo.samplejavafx.mvc.controller.uicontroller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.io.File;
import java.util.function.Consumer;

public class SaveSlotController {

    @FXML
    private Label saveNameLabel;

    private File saveFile;
    private Consumer<File> onLoadAction;
    private Consumer<File> onDeleteAction;

    public void setSaveFile(final File saveFile) {
        this.saveFile = saveFile;
        if (saveFile != null) {
            this.saveNameLabel.setText(saveFile.getName().replace(".json", ""));
        }
    }

    public void setOnLoadAction(final Consumer<File> onLoadAction) {
        this.onLoadAction = onLoadAction;
    }

    public void setOnDeleteAction(final Consumer<File> onDeleteAction) {
        this.onDeleteAction = onDeleteAction;
    }

    @FXML
    private void onLoad() {
        if (onLoadAction != null && saveFile != null) {
            onLoadAction.accept(saveFile);
        }
    }

    @FXML
    private void onDelete() {
        if (onDeleteAction != null && saveFile != null) {
            onDeleteAction.accept(saveFile);
        }
    }
}
