package it.unibo.samplejavafx.mvc.controller.uicontroller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameController;
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
    private Button useButton;
    @FXML
    private Button loadButton;
    @FXML
    private CheckBox forWhite;
    private GameController controller;
    private String selectedLoadoutName;

    public LoadoutSelector(final GameController controller) {
        this.controller = controller;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadoutListView.getItems().addAll(controller.getLoadoutManager().getAll().stream()
            .map(lo -> lo.getName())
            .collect(Collectors.toList()));
        loadoutListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                selectedLoadoutName = loadoutListView.getSelectionModel().getSelectedItem();
            }
        });

        loadButton.setOnAction(event -> {
            if (selectedLoadoutName != null) {
                
            }
        });
    }



}
