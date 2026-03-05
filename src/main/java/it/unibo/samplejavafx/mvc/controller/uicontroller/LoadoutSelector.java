package it.unibo.samplejavafx.mvc.controller.uicontroller;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameController;
import it.unibo.samplejavafx.mvc.model.loadout.LoadoutEntry;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;

public class LoadoutSelector implements Initializable{
    @FXML
    private ListView<String> loadoutListView;
    @FXML
    private ListView<String> loadoutView;
    @FXML
    private Button useButton;
    @FXML
    private Button loadButton;
    @FXML
    private CheckBox forWhite;
    private final GameController controller;
    private Map<String, String> loadoutIds;
    private List<LoadoutEntry> entries;
    private String selectedLoadoutName;

    public LoadoutSelector(final GameController controller) {
        this.controller = controller;
        this.loadoutIds.putAll(controller.getLoadoutManager().getAll().stream()
                               .collect(Collectors.toMap(lo -> lo.getName(), lo -> lo.getId())));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadoutListView.getItems().addAll(loadoutIds.keySet());
        loadoutListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                selectedLoadoutName = loadoutListView.getSelectionModel().getSelectedItem();
            }
        });

        useButton.setOnAction(event -> {
            if (selectedLoadoutName != null
                && controller.getLoadoutManager().load(loadoutIds.get(selectedLoadoutName)).isPresent()) {
                if (forWhite.isSelected()) {
                    controller.setWhiteLoadout(controller.getLoadoutManager().load(loadoutIds.get(selectedLoadoutName)).get());
                } else {
                    controller.setBlackLoadout(controller.getLoadoutManager().load(loadoutIds.get(selectedLoadoutName)).get());
                }
            }
        });

        loadButton.setOnAction(event -> {
            final List<String> holder = new LinkedList<>();
            if (selectedLoadoutName != null
                && controller.getLoadoutManager().load(loadoutIds.get(selectedLoadoutName)).isPresent()) {
                entries.addAll(controller.getLoadoutManager().load(loadoutIds.get(selectedLoadoutName)).get().getEntries());
                for (final LoadoutEntry entry : entries) {
                    int count = 0;
                    if (holder.contains(entry.pieceId())) {
                        break;
                    }
                    for (final LoadoutEntry comp: entries) {
                        if (entry.pieceId().equals(comp.pieceId())) {
                            count++;
                        }
                    }
                    loadoutView.getItems().add(count + "x " + entry.pieceId());
                }
            }
        });
    }
}
