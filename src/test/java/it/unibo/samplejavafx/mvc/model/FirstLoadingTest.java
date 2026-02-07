package it.unibo.samplejavafx.mvc.model;

import it.unibo.samplejavafx.mvc.controller.loadercontroller.LoaderControllerImpl;
import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.AbstractEntityDefinition;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PieceDefinition;
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

        final PieceDefinition pawn = new PieceDefinition(
                "Pawn",
                "pawn",
                "",
                1,
                PieceType.PAWN,
                List.of(
                        new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_ONLY, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(-1, 1), MoveRulesImpl.MoveType.EAT_ONLY, MoveRulesImpl.MoveStrategy.JUMPING),
                        new MoveRulesImpl(new Point2D(1, 1), MoveRulesImpl.MoveType.EAT_ONLY, MoveRulesImpl.MoveStrategy.JUMPING)
                )
        );
        final PieceDefinition rook = new PieceDefinition(
                "Rook",
                "rook",
                "",
                5,
                PieceType.TOWER,
                List.of(
                        new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING),
                        new MoveRulesImpl(new Point2D(0, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING),
                        new MoveRulesImpl(new Point2D(1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING),
                        new MoveRulesImpl(new Point2D(-1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING)
                )
        );

        final var loaderController = new LoaderControllerImpl(List.of(INTERNAL_ENTITIES_PATH));
        loaderController.load();
        final Map<String, AbstractEntityDefinition> pieces = loaderController.getEntityCache().values().stream()
                .flatMap(entry -> entry.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));

        assertEquals(pawn, pieces.get("pawn"));
        assertEquals(rook, pieces.get("rook"));
    }
}
