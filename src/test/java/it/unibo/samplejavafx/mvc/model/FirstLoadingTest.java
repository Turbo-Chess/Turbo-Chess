package it.unibo.samplejavafx.mvc.model;

import it.unibo.samplejavafx.mvc.controller.LoaderController.LoaderControllerImpl;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FirstLoadingTest {
    private static final String INTERNAL_ENTITIES_PATH = "src/main/resources/EntityResources";

    @Test
    void testFirstLoadingPiece() {

        Piece p = new Piece("pawn", "Pawn", 0, "/home/giacomo/Documents/pawn.jpg", PlayerColor.BLACK,
                3, List.of(
                new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING),
                new MoveRulesImpl(new Point2D(1, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING)
        ));
        Piece r = new Piece("rook", "Rook", 0, "/home/giacomo/Documents/pawn.jpg", PlayerColor.BLACK,
                3, List.of(
                new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING),
                new MoveRulesImpl(new Point2D(0, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING),
                new MoveRulesImpl(new Point2D(1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING),
                new MoveRulesImpl(new Point2D(-1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING)
        ));

        var loaderController = new LoaderControllerImpl(List.of(INTERNAL_ENTITIES_PATH));
        loaderController.load();
        System.out.println(loaderController.getEntityCache().get("pawn"));
        assertEquals(p, loaderController.getEntityCache().get("pawn"));
        assertEquals(r, loaderController.getEntityCache().get("rook"));
    }
}
