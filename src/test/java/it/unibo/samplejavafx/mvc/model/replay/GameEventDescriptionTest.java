package it.unibo.samplejavafx.mvc.model.replay;

import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PieceDefinition;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GameEventDescriptionTest {

    @Test
    void testMoveEventDescription() {
        final Piece piece = new Piece.Builder()
            .setHasMoved(false)
            .setEntityDefinition(new PieceDefinition.Builder()
                .setName("Pawn")
                .setId("p1")
                .setImagePath("packs/default/pieces/Pawn.png")
                .setWeight(1)
                .setPieceType(PieceType.PAWN)
                .setMoveRules(Collections.emptyList())
                .build())
            .setGameId(1)
            .setPlayerColor(PlayerColor.WHITE)
            .build();
        final Point2D from = new Point2D(0, 6);
        final Point2D to = new Point2D(0, 5);
        final MoveEvent event = new MoveEvent(1, piece, from, to);

        final String description = event.getEventDescription();

        assertTrue(description.startsWith("Move | Pawn | (0, 6)->(0, 5)"));
    }

    @Test
    void testSpawnEventDescription() {
        final Piece piece = new Piece.Builder()
            .setHasMoved(false)
            .setEntityDefinition(new PieceDefinition.Builder()
                .setName("King")
                .setId("k1")
                .setImagePath("packs/default/pieces/King.png")
                .setWeight(10)
                .setPieceType(PieceType.KING)
                .setMoveRules(Collections.emptyList())
                .build())
            .setGameId(1)
            .setPlayerColor(PlayerColor.BLACK)
            .build();
        final Point2D pos = new Point2D(4, 0);
        final SpawnEvent event = new SpawnEvent(1, piece, pos);

        final String description = event.getEventDescription();

        assertTrue(description.startsWith("Spawn | King | (4, 0)"));
    }

    @Test
    void testDespawnEventDescription() {
        final Piece piece = new Piece.Builder()
            .setHasMoved(false)
            .setEntityDefinition(new PieceDefinition.Builder()
                .setName("Queen")
                .setId("q1")
                .setImagePath("packs/default/pieces/Queen.png")
                .setWeight(9)
                .setPieceType(PieceType.SUPERIOR)
                .setMoveRules(Collections.emptyList())
                .build())
            .setGameId(1)
            .setPlayerColor(PlayerColor.WHITE)
            .build();
        final Point2D pos = new Point2D(3, 3);
        final DespawnEvent event = new DespawnEvent(5, piece, pos);

        final String description = event.getEventDescription();

        assertTrue(description.startsWith("Despawn | Queen | (3, 3)"));
    }
}
