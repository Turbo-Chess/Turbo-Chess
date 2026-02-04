package it.unibo.samplejavafx.mvc.model;

import it.unibo.samplejavafx.mvc.controller.loadercontroller.LoaderControllerImpl;
import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FirstLoadingTest {
    private static final String INTERNAL_ENTITIES_PATH = "src/main/resources/EntityResources";

    @Test
    void testFirstLoadingPiece() {

        final Piece p = new Piece("pawn", "Pawn", 0, "/home/giacomo/Documents/pawn.jpg", PlayerColor.NONE,
                3, List.of(
                new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING),
                new MoveRulesImpl(new Point2D(1, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING)
        ));
        final Piece r = new Piece("rook", "Rook", 0, "/home/giacomo/Documents/pawn.jpg", PlayerColor.NONE,
                5, List.of(
                new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING),
                new MoveRulesImpl(new Point2D(0, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING),
                new MoveRulesImpl(new Point2D(1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING),
                new MoveRulesImpl(new Point2D(-1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING)
        ));

        final var loaderController = new LoaderControllerImpl(List.of(INTERNAL_ENTITIES_PATH));
        loaderController.load();
        final Map<String, Entity> pieces = loaderController.getEntityCache().values().stream()
                .flatMap(entry -> entry.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));

        assertEquals(p, pieces.get("pawn"));
        assertEquals(r, pieces.get("rook"));
    }
}
