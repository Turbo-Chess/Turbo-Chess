package it.unibo.samplejavafx.mvc.controller.uicontroller;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import it.unibo.samplejavafx.mvc.controller.coordinator.GameCoordinator;
import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameController;
import it.unibo.samplejavafx.mvc.model.loadout.LoadoutEntry;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
    private CheckBox forBlack;
    private final GameController controller;
    private final GameCoordinator coordinator;
    private Map<String, String> loadoutIds = new HashMap<>();
    private List<LoadoutEntry> entries = new LinkedList<>();
    private String selectedLoadoutName;

    public LoadoutSelector(final GameController controller, final GameCoordinator coordinator) {
        this.controller = controller;
        this.coordinator = coordinator;
        this.loadoutIds.putAll(controller.getLoadoutManager().getAll().stream()
                               .collect(Collectors.toMap(lo -> lo.getName(), lo -> lo.getId())));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final ObservableList<String> names = FXCollections.observableArrayList(loadoutIds.keySet());
        loadoutListView.setItems(names);
        loadoutListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                selectedLoadoutName = loadoutListView.getSelectionModel().getSelectedItem();
            }
        });

        useButton.setOnAction(event -> {
            if (selectedLoadoutName != null
                && controller.getLoadoutManager().load(loadoutIds.get(selectedLoadoutName)).isPresent()) {
                if (!forBlack.isSelected()) {
                    controller.setWhiteLoadout(controller.getLoadoutManager().load(loadoutIds.get(selectedLoadoutName)).get());
                } else {
                    controller.setBlackLoadout(controller.getLoadoutManager().load(loadoutIds.get(selectedLoadoutName)).get());
                }
            }
        });

        loadButton.setOnAction(event -> {
            final Set<String> holder = new HashSet<>();
            if (selectedLoadoutName != null
                && controller.getLoadoutManager().load(loadoutIds.get(selectedLoadoutName)).isPresent()) {
                entries.addAll(controller.getLoadoutManager().load(loadoutIds.get(selectedLoadoutName)).get().getEntries());
                for (final LoadoutEntry entry : entries) {
                    int count = 0;
                    if (holder.contains(entry.pieceId())) {
                        continue;
                    }
                    holder.add(entry.pieceId());
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

    /**
     * Handles the "Back" button action.
     *
     * @param e the action event
     */
    public void backToMenu(final ActionEvent e) {
        this.coordinator.initMainMenu();
    }

    /**
     * Handles the "Back" button action.
     *
     * @param e the action event
     */
    public void toLoadoutEditor(final ActionEvent e) {
        this.coordinator.initLoadoutEditor();
    }
}
