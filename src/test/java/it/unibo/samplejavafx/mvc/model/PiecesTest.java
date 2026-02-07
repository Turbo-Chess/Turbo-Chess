package it.unibo.samplejavafx.mvc.model;

import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PieceDefinition;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 */
class PiecesTest {
    /**
     *
     */
    @Test
    void testPieceDefinitionInitialization() {
        final PieceDefinition p = new PieceDefinition.Builder()
                .setName("test-piece")
                .setId("test")
                .setImagePath("/home/giacomo/Documents/pawn.jpg")
                .setWeight(3)
                .setPieceType(PieceType.INFERIOR)
                .setMoveRules(List.of(new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING)))
                .build();

        assertEquals("test", p.getId());
        assertEquals("test-piece", p.getName());
        assertEquals("/home/giacomo/Documents/pawn.jpg", p.getImagePath());
        assertEquals(3, p.getWeight());
    }
}
