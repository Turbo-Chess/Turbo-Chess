package it.unibo.turbochess.controller.uicontroller.impl;

import java.util.HashSet;
import java.util.Set;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.turbochess.controller.coordinator.api.GameCoordinator;
import it.unibo.turbochess.controller.gamecontroller.api.GameController;
import it.unibo.turbochess.controller.uicontroller.api.PromotionController;
import it.unibo.turbochess.model.chessboard.boardfactory.api.DefinitionRegistry;
import it.unibo.turbochess.model.entity.impl.PieceType;
import it.unibo.turbochess.model.entity.impl.PlayerColor;
import it.unibo.turbochess.model.utils.LoadingUtils;
import it.unibo.turbochess.model.utils.RulesUtils;
import it.unibo.turbochess.model.loadout.api.Loadout;
import it.unibo.turbochess.model.loadout.impl.LoadoutEntry;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

/**
 * Implementation of {@link PromotionController}, handles the GUI for the promotion.
 */
public final class PromotionControllerImpl implements PromotionController {
    private static final int DEFAULT_VALUE = 0;
    private static final int IMAGE_BOX_SIZE = 64; // Standard chess piece size in px
    @FXML
    private GridPane promotionPane;
    private final Loadout white;
    private final Loadout black;
    private final GameController controller;
    private final GameCoordinator coordinator;
    private final DefinitionRegistry entityCache;
    private int x;
    private int y;

    /**
     * Constructor for the Promotion GUI.
     *
     * @param controller the {@link GameController} to access the promotion function.
     * @param entityCache the {@link DefinitionRegistry} containing entity definitions.
     * @param coordinator the {@link GameCoordinator} using to connect to the GUI.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "in a MVC-based structure"
            + " you have to pass instances of controllers.")
    public PromotionControllerImpl(
            final GameCoordinator coordinator,
            final GameController controller,
            final DefinitionRegistry entityCache) {
        this.controller = controller;
        this.coordinator = coordinator;
        this.entityCache = entityCache;
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
    @Override
    public void init(final PlayerColor currentColor) {
        populateGridPane(currentColor);
    }

    /**
     * Method that populates the GridPane with buttons.
     *
     * @param currentColor color of the player's promotion.
     */
    private void populateGridPane(final PlayerColor currentColor) {
        final Set<LoadoutEntry> set = new HashSet<>();
        switch (RulesUtils.swapColor(currentColor)) {
            case WHITE:
                set.addAll(getPromotionPieces(white));
                break;
            case BLACK:
                set.addAll(getPromotionPieces(black));
                break;
        }

        for (final LoadoutEntry entry : set) {
            final String imagePath = entityCache.getDefinition(entry.packId(), entry.pieceId()).getImagePath();
            final String imageColorPath = LoadingUtils.calculateImageColorPath(imagePath, 
                RulesUtils.swapColor(currentColor), entry.pieceId());
            final Button btn = new Button("");
            final ImageView imageView = new ImageView(new Image(imageColorPath));
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.setFitWidth(IMAGE_BOX_SIZE);
            imageView.setFitHeight(IMAGE_BOX_SIZE);

            final StackPane imageBox = new StackPane(imageView);
            imageBox.setPrefSize(IMAGE_BOX_SIZE, IMAGE_BOX_SIZE);
            imageBox.setMinSize(IMAGE_BOX_SIZE, IMAGE_BOX_SIZE);
            imageBox.setMaxSize(IMAGE_BOX_SIZE, IMAGE_BOX_SIZE);

            btn.setGraphic(imageBox);
            btn.setPrefSize(IMAGE_BOX_SIZE + 16, IMAGE_BOX_SIZE + 16); // Add some padding
            btn.setMinSize(IMAGE_BOX_SIZE + 16, IMAGE_BOX_SIZE + 16);
            btn.setMaxSize(IMAGE_BOX_SIZE + 16, IMAGE_BOX_SIZE + 16);
            btn.setOnAction(event -> isFinished(entry));
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
            final PieceType type = entityCache.getDefinition(entry.packId(), entry.pieceId()).getPieceType();
            if (type != PieceType.KING && type != PieceType.PAWN && !ids.contains(entry.pieceId())) {
                ids.add(entry.pieceId());
                set.add(entry);
            }
        }
        return set;
    }

    /**
     * Private method that is called whenever a piece is chosen, therefore closing the GUI and returning to the match.
     *
     * @param entry the {@link LoadoutEntry} of the chosen piece.
     */
    private void isFinished(final LoadoutEntry entry) {
        controller.promote(entry);
        coordinator.showGame();
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
