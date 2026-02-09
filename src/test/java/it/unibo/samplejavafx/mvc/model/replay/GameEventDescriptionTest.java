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
            .entityDefinition(new PieceDefinition.Builder()
                .name("Pawn")
                .id("p1")
                .imagePath("packs/default/pieces/Pawn.png")
                .weight(1)
                .pieceType(PieceType.PAWN)
                .moveRules(Collections.emptyList())
                .build())
            .gameId(1)
            .playerColor(PlayerColor.WHITE)
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
            .entityDefinition(new PieceDefinition.Builder()
                .name("King")
                .id("k1")
                .imagePath("packs/default/pieces/King.png")
                .weight(10)
                .pieceType(PieceType.KING)
                .moveRules(Collections.emptyList())
                .build())
            .gameId(1)
            .playerColor(PlayerColor.BLACK)
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
            .entityDefinition(new PieceDefinition.Builder()
                .name("Queen")
                .id("q1")
                .imagePath("packs/default/pieces/Queen.png")
                .weight(9)
                .pieceType(PieceType.SUPERIOR)
                .moveRules(Collections.emptyList())
                .build())
            .gameId(1)
            .playerColor(PlayerColor.WHITE)
            .build();
        final Point2D pos = new Point2D(3, 3);
        final DespawnEvent event = new DespawnEvent(5, piece, pos);

        final String description = event.getEventDescription();

        assertTrue(description.startsWith("Despawn | Queen | (3, 3)"));
    }
}
