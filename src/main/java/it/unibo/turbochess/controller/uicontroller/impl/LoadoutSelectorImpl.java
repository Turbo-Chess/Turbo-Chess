package it.unibo.turbochess.controller.uicontroller.impl;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.turbochess.controller.coordinator.api.GameCoordinator;
import it.unibo.turbochess.controller.gamecontroller.api.GameController;
import it.unibo.turbochess.controller.uicontroller.api.LoadoutSelector;
import it.unibo.turbochess.model.loadout.api.Loadout;
import it.unibo.turbochess.model.loadout.impl.LoadoutEntry;
import it.unibo.turbochess.model.loadout.api.LoadoutManager;
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
import javafx.scene.layout.BorderPane;

/**
 * Implementation of {@link LoadoutSelector}, enables to load, see and use loadouts.
 */
public final class LoadoutSelectorImpl implements Initializable, LoadoutSelector {
    @FXML
    private BorderPane borderPain;
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
     * @param loadoutManager the manager to retrieve loadouts.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
            justification = "This controllers are meant to be shared objects to follow the MVC architecture")
    public LoadoutSelectorImpl(final GameController controller,
                           final GameCoordinator coordinator,
                           final LoadoutManager loadoutManager) {
        this.controller = controller;
        this.coordinator = coordinator;
        this.loadoutManager = loadoutManager;
        this.loadoutIds.putAll(loadoutManager.getAll().stream()
                               .collect(Collectors.toMap(Loadout::getName, Loadout::getId)));
    }

    /**
     * {@inheritDoc}
     */
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

        forBlack.selectedProperty().addListener((observable, oldValue, newValue) -> {
            final URL cssResource = getClass().getResource("/css/LoadoutSelector.css");
            if (cssResource != null) {
                final String css = cssResource.toExternalForm();
                if (newValue) {
                    if (!borderPain.getStylesheets().contains(css)) {
                        borderPain.getStylesheets().add(css);
                    }
                } else {
                    borderPain.getStylesheets().remove(css);
                }
            }
        });

        /*
         * When a loadout is selected and loaded, sets it as the actual loadout for the chosen color.
         */
        useButton.setOnAction(event -> {
            if (selectedLoadoutName != null
                && loadoutManager.load(loadoutIds.get(selectedLoadoutName)).isPresent()) {
                if (!forBlack.isSelected()) {
                    controller.setWhiteLoadout(loadoutManager.load(loadoutIds.get(selectedLoadoutName)).get());
                } else {
                    controller.setBlackLoadout(loadoutManager.load(loadoutIds.get(selectedLoadoutName)).get().mirrored());
                }
            }
        });

        /*
         * When a loadout is selected, views its pieces.
         */
        loadButton.setOnAction(event -> {
            loadoutView.getItems().clear();
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
                entries.clear();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void backToMenu(final ActionEvent e) {
        this.coordinator.initMainMenu();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void backToLoadoutEditor(final ActionEvent e) {
        this.coordinator.initLoadoutEditor();
    }

}
