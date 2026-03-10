package it.unibo.samplejavafx.mvc.controller.uicontroller;

import java.util.HashSet;
import java.util.Set;

import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameController;
import it.unibo.samplejavafx.mvc.controller.loadercontroller.LoaderController;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.loadout.Loadout;
import it.unibo.samplejavafx.mvc.model.loadout.LoadoutEntry;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

/**
 * Controller that handles the GUI for promotion.
 */
public final class PromotionController {
    private static final int DEFAULT_VALUE = 0;
    @FXML
    private GridPane promotionPane;
    private final Loadout white;
    private final Loadout black;
    private final GameController controller;
    private final LoaderController loaderController;
    private int x;
    private int y;

    /**
     * Constructor for the Promotion GUI.
     *
     * @param controller the {@link GameController} connected to this GUI.
     * @param loaderController the {@link LoaderController} to retrieve entity definitions.
     */
    public PromotionController(final GameController controller, final LoaderController loaderController) {
        this.controller = controller;
        this.loaderController = loaderController;
        this.white = controller.getWhiteLoadout();
        this.black = controller.getBlackLoadout();
        this.x = DEFAULT_VALUE;
        this.y = DEFAULT_VALUE;
    }

    /**
     * Method that initializes the GUI.
     *
     * @param currentColor color of the player's promotion.
     */
    public void init(final PlayerColor currentColor) {
        populateGridPane(currentColor);
    }

    /**
     * Method that populates the GridPane with buttons.
     *
     * @param currentColor color of the player's promotion.
     */
    public void populateGridPane(final PlayerColor currentColor) {
        final Set<LoadoutEntry> set = new HashSet<>();
        switch (currentColor) {
            case WHITE:
                set.addAll(getPromotionPieces(white));
                break;
            case BLACK:
                set.addAll(getPromotionPieces(black));
                break;
        }

        for (final LoadoutEntry entry : set) {
            final String imagePath = loaderController.getEntityCache()
                    .get(entry.packId()).get(entry.pieceId()).getImagePath();
            final String imageColorPath = controller.calculateImageColorPath(imagePath, currentColor, entry.pieceId());
            final Button btn = new Button("");
            final ImageView imageView = new ImageView(new Image(imageColorPath));

            btn.setGraphic(imageView);
            btn.setOnAction(event -> {
                isFinished(entry);
            });
            promotionPane.add(btn, x, y);
            increment();
        }
    }

    /**
     * Private method that filters all possible pieces for promotion, ignoring pawns and king.
     *
     * @param load the {@link Loadout} of the current player.
     * @return a set containing {@link LoadoutEntry} of all possible pieces.
     */
    private Set<LoadoutEntry> getPromotionPieces(final Loadout load) {
        final Set<LoadoutEntry> set = new HashSet<>();
        final Set<String> ids = new HashSet<>();
        for (final var entry : load.getEntries()) {
            if (!"pawn".equals(entry.pieceId()) && !"king".equals(entry.pieceId()) && !ids.contains(entry.pieceId())) {
                ids.add(entry.pieceId());
                set.add(entry);
            }
        }
        return set;
    }

    /**
     * Private method thay is called whenever a piece is chosen, therefore closing the GUI and returning to the match.
     *
     * @param entry the {@link LoadoutEntry} of the chosen piece.
     */
    private void isFinished(final LoadoutEntry entry) {
        controller.promote(entry);
        controller.showGame();
    }

    /**
     * Private method to handle buttons' placement in the grid.
     */
    private void increment() {
        x += 1;
        if (x == 3) {
            x = 0;
            y += 1;
        }
    }
}
