package it.unibo.samplejavafx.mvc.controller.uicontroller;

import java.util.HashSet;
import java.util.Set;

import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameController;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.loadout.Loadout;
import it.unibo.samplejavafx.mvc.model.loadout.LoadoutEntry;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class PromotionController {
    @FXML
    private GridPane maionese;
    private final Loadout white;
    private final Loadout black;
    private final GameController controller;
    private int x, y = 0;

    public PromotionController(final GameController controller) {
        this.controller = controller;
        this.white = controller.getWhiteLoadout();
        this.black = controller.getBlackLoadout();
    }

    public void init(final PlayerColor currentColor) {
        populateScrollPanel(currentColor);
    }

    public void populateScrollPanel(final PlayerColor currentColor) {
        Set<LoadoutEntry> set = new HashSet<>();
        switch (currentColor) {
            case WHITE:
                set = getPromotionPieces(white);
                break;
            case BLACK:
                set = getPromotionPieces(black);
                break;
        }

        for (LoadoutEntry entry : set) {
            final String imagePath = controller.getLoaderController().getEntityCache()
                                               .get(entry.packId()).get(entry.pieceId()).getImagePath();
            final String imageColorPath = controller.calculateImageColorPath(imagePath, currentColor, entry.pieceId());
            final Button btn = new Button("");
            final ImageView imageView = new ImageView(new Image(imageColorPath));
            
            btn.setGraphic(imageView);
            btn.setOnAction(event -> {
                    isFinished(entry);
                });
            maionese.add(btn, x, y);
            increment();
        }
    }

    private Set<LoadoutEntry> getPromotionPieces(final Loadout list) {
        final Set<LoadoutEntry> set = new HashSet<>();
        for (var entry : list.getEntries()) {
            if (!entry.pieceId().equals("pawn") && !entry.pieceId().equals("king")) {
                set.add(entry);
            }
        }
        return set;
    }

    private void isFinished(final LoadoutEntry entry) {
        controller.promote(entry);
    }

    private void increment() {
        x += 1;
        y += 1;
        if (x == 3) {
            x = 0;
        }
        if (y == 3) {
            y = 0;
        }
    }
}
