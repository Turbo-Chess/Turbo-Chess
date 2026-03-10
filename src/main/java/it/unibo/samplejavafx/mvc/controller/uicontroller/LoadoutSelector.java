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
import it.unibo.samplejavafx.mvc.model.loadout.Loadout;
import it.unibo.samplejavafx.mvc.model.loadout.LoadoutEntry;
import it.unibo.samplejavafx.mvc.model.loadout.LoadoutManager;
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

/**
 * Controller for the LoadoutSelector UI.
 */
public final class LoadoutSelector implements Initializable {
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
    private final LoadoutManager loadoutManager;
    private final Map<String, String> loadoutIds = new HashMap<>();
    private final List<LoadoutEntry> entries = new LinkedList<>();
    private String selectedLoadoutName;

    /**
     * Constructor for the LoadoutSelector UI.
     * 
     * @param controller the {@link GameController} needed for this class to operate.
     * @param coordinator the {@link GameCoordinator} needed for this class to operate.
     */
    public LoadoutSelector(final GameController controller, final GameCoordinator coordinator, final LoadoutManager loadoutManager) {
        this.controller = controller;
        this.coordinator = coordinator;
        this.loadoutManager = loadoutManager;
        this.loadoutIds.putAll(loadoutManager.getAll().stream()
                               .collect(Collectors.toMap(Loadout::getName, Loadout::getId)));
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        final ObservableList<String> names = FXCollections.observableArrayList(loadoutIds.keySet());
        loadoutListView.setItems(names);
        loadoutListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, 
                                final String oldValue, final String newValue) {
                selectedLoadoutName = loadoutListView.getSelectionModel().getSelectedItem();
            }
        });

        useButton.setOnAction(event -> {
            if (selectedLoadoutName != null
                && loadoutManager.load(loadoutIds.get(selectedLoadoutName)).isPresent()) {
                if (!forBlack.isSelected()) {
                    controller.setWhiteLoadout(loadoutManager.load(loadoutIds.get(selectedLoadoutName)).get());
                } else {
                    controller.setBlackLoadout(loadoutManager.load(loadoutIds.get(selectedLoadoutName)).get());
                }
            }
        });

        loadButton.setOnAction(event -> {
            final Set<String> holder = new HashSet<>();
            if (selectedLoadoutName != null
                && loadoutManager.load(loadoutIds.get(selectedLoadoutName)).isPresent()) {
                entries.addAll(loadoutManager.load(loadoutIds.get(selectedLoadoutName)).get().getEntries());
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
     * @param e the {@link ActionEvent} linked to the button.
     */
    public void backToMenu(final ActionEvent e) {
        this.coordinator.initMainMenu();
    }

    /**
     * Handles the "Loadout Editor" button action.
     *
     * @param e the {@link ActionEvent} linked to the button.
     */
    public void backToLoadoutEditor(final ActionEvent e) {
        this.coordinator.initLoadoutEditor();
    }

}
