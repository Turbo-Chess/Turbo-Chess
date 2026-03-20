package it.unibo.turbochess.controller.uicontroller.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.turbochess.controller.coordinator.api.GameCoordinator;
import it.unibo.turbochess.controller.uicontroller.api.LoadoutEditor;
import it.unibo.turbochess.model.chessboard.boardfactory.api.DefinitionRegistry;
import it.unibo.turbochess.model.loadout.impl.LoadoutEntry;
import it.unibo.turbochess.model.loadout.impl.LoadoutImpl;
import it.unibo.turbochess.model.loadout.api.LoadoutManager;
import it.unibo.turbochess.model.point2d.Point2D;
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
 * Implementation of {@link LoadoutEditor}, enables to choose all the pieces in memory and place them in order to create
 * a new loadout.
 */
public final class LoadoutEditorImpl implements Initializable, LoadoutEditor {
    private static final int ROW = 2;
    private static final int COLUMN = 8;
    private static final int SIZE = 16;
    private static final int OFFSET = 6;
    @FXML
    private Button saveButton;
    @FXML
    private GridPane gridPain;
    @FXML
    private ListView<String> pieceView;
    @FXML
    private TextField textLabel;
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "The loadout manager is an effectively"
            + "immutable object that is a shared service used to manage loadouts")
    private final LoadoutManager loadoutManager;
    private final DefinitionRegistry entityCache;
    private final GameCoordinator coordinator;
    private final Map<Point2D, LoadoutEntry> entries = new HashMap<>();
    private final Map<Button, Point2D> buttonGrid = new HashMap<>();
    private String selectedPiece;
    private int x;
    private int y;

    /**
     * Constructor for the LoadoutEditor.
     *
     * @param coordinator the {@link GameCoordinator} needed for this class to operate.
     * @param entityCache the {@link DefinitionRegistry} needed for this class to operate.
     * @param loadoutManager the {@link LoadoutManager} needed to access the methods to manipulate loadouts.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Game Coordinator is meant to be a shared dependency"
            + "for the MVC pattern")
    public LoadoutEditorImpl(
            final GameCoordinator coordinator,
            final DefinitionRegistry entityCache,
            final LoadoutManager loadoutManager
    ) {
        this.coordinator = coordinator;
        this.entityCache = entityCache;
        this.loadoutManager = loadoutManager;
        this.x = 0;
        this.y = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        final ObservableList<String> pieceNames = FXCollections.observableArrayList(entityCache.getAllIds());
        pieceView.setItems(pieceNames);
        pieceView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable,
                                final String oldValue, final String newValue) {
                selectedPiece = pieceView.getSelectionModel().getSelectedItem();
            }
        });

        saveButton.setOnAction(event -> {
            if (buttonGrid.size() == SIZE && !textLabel.getText().isBlank()) {
               loadoutManager.save(LoadoutImpl.create(textLabel.getText(), new ArrayList<>(entries.values())));
            }
        });

        populateGridPane();
    }

    /**
     * Places a selected piece in the grid.
     * 
     * @param event the Click event of the button.
     */
    private void placeOnClick(final ActionEvent event) {
        if (selectedPiece != null) {
            final Button btn = (Button) event.getSource();
            entries.put(buttonGrid.get(btn), new LoadoutEntry(buttonGrid.get(btn), ofPack(selectedPiece), selectedPiece));
            btn.setText(selectedPiece);
        }
    }

    /**
     * Given a pieceId, checks and returns the original pack from where it's from.
     * 
     * @param id the String of the pieceId.
     * @return the String of the original pack.
     */
    private String ofPack(final String id) {
        for (final var packEntry : entityCache.getResPackIds()) {
            if (entityCache.getPackData(packEntry).containsKey(id)) {
                return packEntry;
            }
        }
        return null;
    }

    /**
     * Populates the grid pane with the buttons for the loadout.
     */
    private void populateGridPane() {
        while (y != ROW) {
            final Button btn = new Button("");
            if ((x + y) % 2 == 0) {
                btn.getStyleClass().add("light-square");
            } else {
                btn.getStyleClass().add("dark-square");
            }
            btn.getStyleClass().add("material-surface");
            btn.setOnAction(event -> {
                placeOnClick(event);
            });
            gridPain.add(btn, x, y);
            buttonGrid.put(btn, new Point2D(x, y + OFFSET));
            x++;
            if (x == COLUMN) {
                x = 0;
                y++;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void backToLoadoutSelector(final ActionEvent e) {
        this.coordinator.initLoadout();
    }
}
