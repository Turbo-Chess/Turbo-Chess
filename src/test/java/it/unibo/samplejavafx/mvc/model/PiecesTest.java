package it.unibo.samplejavafx.mvc.model;

import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.movement.JumpingMovement;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 *
 */
class PiecesTest {
    /**
     *
     */
    @Test
    void testPieceInitialization() {
        final Piece p = new Piece("test", "test-piece", Path.of("/home/giacomo/Documents/pawn.jpg"), PlayerColor.BLACK,
                3, List.of(
                        new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, new JumpingMovement())
        ));

        assertEquals("test", p.getId());
        assertEquals("test-piece", p.getName());
        assertEquals(Path.of("/home/giacomo/Documents/pawn.jpg"), p.getImagePath());
        assertFalse(p.isHasMoved());
        assertEquals(3, p.getWeight());
    }
}
