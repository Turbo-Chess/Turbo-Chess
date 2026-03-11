package it.unibo.samplejavafx.mvc.controller.uicontroller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.samplejavafx.mvc.controller.coordinator.GameCoordinator;
import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameController;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.AbstractEntityDefinition;
import it.unibo.samplejavafx.mvc.model.loadout.Loadout;
import it.unibo.samplejavafx.mvc.model.loadout.LoadoutEntry;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * Controller for the LoadoutEditor UI.
 */
public final class LoadoutEditor implements Initializable {
    private static final int SIZE = 16;
    private static final int ROW = 2;
    private static final int COLUMN = 8;
    @FXML
    private Button saveButton;
    @FXML
    private GridPane gridPain;
    @FXML
    private ListView<String> pieceView;
    @FXML
    private TextField textLabel;
    private final GameController controller;
    private final GameCoordinator coordinator;
    private final Map<String, Map<String, AbstractEntityDefinition>> entityCache = new HashMap<>();
    private final Map<Point2D, LoadoutEntry> entries = new HashMap<>();
    private String selectedPiece;
    private int x;
    private int y;

    /**
     * Constructor for the LoadoutEditor.
     * 
     * @param controller the {@link GameController} needed for this class to operate.
     * @param coordinator the {@link GameCoordinator} needed for this class to operate.
     */
    @SuppressFBWarnings("EI_EXPOSE_REP2") // in a MVC-based structure you have to pass instances of controllers.
    public LoadoutEditor(final GameController controller, final GameCoordinator coordinator) {
        this.controller = controller;
        this.coordinator = coordinator;
        this.entityCache.putAll(controller.getLoaderController().getEntityCache());
        this.x = 0;
        this.y = 0;
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        final ObservableList<String> pieceNames = FXCollections.observableArrayList(entityCache.values().stream()
                .map(Map::keySet)
                .flatMap(Set::stream)
                .collect(Collectors.toSet()));
        pieceView.setItems(pieceNames);
        pieceView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable,
                                final String oldValue, final String newValue) {
                selectedPiece = pieceView.getSelectionModel().getSelectedItem();
            }
        });

        saveButton.setOnAction(event -> {
            if (entries.size() == SIZE && !textLabel.getText().isBlank()) {
               controller.getLoadoutManager().save(Loadout.create(textLabel.getText(), new ArrayList<>(entries.values())));
            }
        });

        populateGridPane();
    }

    private void placeOnClick(final int posX, final int posY, final Button btn) {
        if (selectedPiece != null) {
            entries.put(new Point2D(posX, posY), new LoadoutEntry(new Point2D(posX, posY), ofPack(selectedPiece), selectedPiece));
            btn.setText(selectedPiece);
        }
    }

    private String ofPack(final String id) {
        for (final var packEntry : entityCache.entrySet()) {
            if (entityCache.get(packEntry.getKey()).containsKey(id)) {
                return packEntry.getKey();
            }
        }
        return null;
    }

    private void populateGridPane() {
        while (y != ROW) {
            final Button btn = new Button("");
            btn.setOnAction(event -> {
                placeOnClick(x, y, btn);
            });
            gridPain.add(btn, x, y);
            x++;
            if (x == COLUMN) {
                x = 0;
                y++;
            }
        }
    }

    /**
     * Handles the "Loadout Selector" button action.
     * 
     * @param e the {@link ActionEvent} linked to the button.
     */
    public void backToLoadoutSelector(final ActionEvent e) {
        this.coordinator.initLoadout();
    }
}
