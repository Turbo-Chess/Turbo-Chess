package it.unibo.samplejavafx.mvc.model;

import it.unibo.samplejavafx.mvc.controller.LoaderController.LoaderControllerImpl;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FirstLoadingTest {
    @Test
    void testFirstLoadingPiece() {

        final Piece p = new Piece("pawn", "Pawn", 0, "/home/giacomo/Documents/pawn.jpg", PlayerColor.BLACK,
                3, List.of(
                new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING)
        ));

        var loaderController = new LoaderControllerImpl();
        loaderController.load();
        System.out.println(loaderController.getEntityCache().getEntity("pawn"));
        assertEquals(p, loaderController.getEntityCache().getEntity("pawn"));
    }
}
