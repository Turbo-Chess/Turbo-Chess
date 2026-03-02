package it.unibo.samplejavafx.mvc.controller.uicontroller;

import java.util.HashSet;
import java.util.Set;

import it.unibo.samplejavafx.mvc.controller.gamecontroller.GameController;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.loadout.Loadout;
import it.unibo.samplejavafx.mvc.model.loadout.LoadoutEntry;

public class PromotionController {
    private final Loadout white;
    private final Loadout black;

    public PromotionController(final GameController controller) {
        this.white = controller.getWhiteLoadout();
        this.black = controller.getBlackLoadout();
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
}
