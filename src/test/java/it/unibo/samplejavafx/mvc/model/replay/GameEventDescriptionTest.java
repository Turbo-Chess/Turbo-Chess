package it.unibo.samplejavafx.mvc.model.replay;

import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PieceType;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PieceDefinition;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GameEventDescriptionTest {

    @Test
    void testMoveEventDescription() {
        final String entityName = "Pawn";
        final Point2D from = new Point2D(0, 6);
        final Point2D to = new Point2D(0, 5);
        final Piece capturedPiece = new Piece.Builder()
            .hasMoved(false)
            .entityDefinition(new PieceDefinition.Builder()
                .name("Pawn")
                .id("ep")
                .imagePath("path")
                .weight(1)
                .pieceType(PieceType.PAWN)
                .moveRules(Collections.emptyList())
                .build())
            .gameId(1)
            .playerColor(PlayerColor.BLACK)
            .build();

        final MoveEvent event = new MoveEvent(1, entityName, PlayerColor.WHITE, from, to, captured);
        final String description = event.getEventDescription();

        assertTrue(description.contains("Move"));
        assertTrue(description.contains("Pawn"));
        assertTrue(description.contains("WHITE"));
        assertTrue(description.contains("(0, 6)->(0, 5)"));
        assertTrue(description.contains("Capture: EnemyPawn"));
    }

    @Test
    void testSpawnEventDescription() {
        final Piece piece = new Piece.Builder()
            .hasMoved(false)
            .entityDefinition(new PieceDefinition.Builder()
                .name("King")
                .id("k1")
                .imagePath("classpath:/assets/images/white_king.png")
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
            .hasMoved(false)
            .entityDefinition(new PieceDefinition.Builder()
                .name("Queen")
                .id("q1")
                .imagePath("classpath:/assets/images/white_queen.png")
                .weight(10)
                .pieceType(PieceType.KING)
                .moveRules(Collections.emptyList())
                .build())
            .gameId(1)
            .playerColor(PlayerColor.BLACK)
            .build();
        final Point2D pos = new Point2D(3, 3);
        final DespawnEvent event = new DespawnEvent(5, piece, pos);

        final String description = event.getEventDescription();

        // Expect Name
        assertTrue(description.startsWith("Despawn | Queen | (3, 3)"));
    }
}
