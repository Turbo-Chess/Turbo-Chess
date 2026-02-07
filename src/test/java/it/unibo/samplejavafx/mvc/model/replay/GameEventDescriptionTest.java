package it.unibo.samplejavafx.mvc.model.replay;

import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PieceDefinition;
import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import org.junit.jupiter.api.Test;

import java.util.List;  

import static org.junit.jupiter.api.Assertions.assertTrue;

class GameEventDescriptionTest {
    private Piece createTestPiece(final int gameId, final PlayerColor color) {
        final PieceDefinition def = new PieceDefinition.Builder()
            .setName("test-piece")
            .setId("test")
            .setImagePath("/home/giacomo/Documents/pawn.jpg")
            .setWeight(3)
            .setPieceType(PieceType.INFERIOR)
            .setMoveRules(List.of(new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING)))
            .build();
        return new Piece(def, gameId, color, false);
    }

    @Test
    void testMoveEventDescription() {
        final Piece piece = createTestPiece(1, PlayerColor.WHITE);
        final Point2D from = new Point2D(0, 6);
        final Point2D to = new Point2D(0, 5);
        final MoveEvent event = new MoveEvent(1, piece, from, to);

        final String description = event.getEventDescription();

        assertTrue(description.startsWith("Move | test-piece | (0, 6)->(0, 5)"));
    }

    @Test
    void testSpawnEventDescription() {
        final Piece piece = createTestPiece(1, PlayerColor.BLACK); 
        final Point2D pos = new Point2D(4, 0);
        final SpawnEvent event = new SpawnEvent(1, piece, pos);

        final String description = event.getEventDescription();

        assertTrue(description.startsWith("Spawn | test-piece | (4, 0)"));
    }

    @Test
    void testDespawnEventDescription() {
        final Piece piece = createTestPiece(5, PlayerColor.WHITE); 
        final Point2D pos = new Point2D(3, 3);
        final DespawnEvent event = new DespawnEvent(5, piece, pos);

        final String description = event.getEventDescription();

        assertTrue(description.startsWith("Despawn | test-piece | (3, 3)"));
    }
}
