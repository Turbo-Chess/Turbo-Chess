// CHECKSTYLE: MagicNumber OFF

package it.unibo.turbochess.model.loading;

import it.unibo.turbochess.controller.loadercontroller.impl.LoaderControllerImpl;
import it.unibo.turbochess.model.loader.impl.DefinitionCacheEntry;
import it.unibo.turbochess.model.entity.impl.PieceType;
import it.unibo.turbochess.model.entity.definition.AbstractEntityDefinition;
import it.unibo.turbochess.model.entity.definition.PieceDefinition;
import it.unibo.turbochess.model.movement.impl.MoveRulesImpl;
import it.unibo.turbochess.model.point2d.Point2D;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class made to verify that the loading controller and caching system works correctly.
 */
class FirstLoadingTest {
    /**
     * Tests the first step of loading a piece into the cache.
     *
     * <p>
     * Creates expected piece definitions (pawn, rook) manually and asserts they match
     * the ones loaded by the {@link LoaderControllerImpl}.
     * </p>
     */
    @Test
    void testFirstLoadingPiece() {
        final PieceDefinition pawn = new PieceDefinition.Builder()
                .name("Pawn")
                .id("pawn")
                .imagePath("classpath:/assets/images/")
                .weight(1)
                .pieceType(PieceType.PAWN)
                .moveRules(List.of(
                        new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_ONLY,
                                MoveRulesImpl.MoveStrategy.JUMPING, false),
                        new MoveRulesImpl(new Point2D(-1, 1), MoveRulesImpl.MoveType.EAT_ONLY,
                                MoveRulesImpl.MoveStrategy.JUMPING, false),
                        new MoveRulesImpl(new Point2D(1, 1), MoveRulesImpl.MoveType.EAT_ONLY,
                                MoveRulesImpl.MoveStrategy.JUMPING, false),
                        new MoveRulesImpl(new Point2D(0, 2), MoveRulesImpl.MoveType.MOVE_ONLY,
                                MoveRulesImpl.MoveStrategy.STEPPING, true)
                ))
                .build();
        final PieceDefinition rook = new PieceDefinition.Builder()
                .name("Rook")
                .id("rook")
                .imagePath("classpath:/assets/images/")
                .weight(5)
                .pieceType(PieceType.TOWER)
                .moveRules(List.of(
                        new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT,
                                MoveRulesImpl.MoveStrategy.SLIDING, false),
                        new MoveRulesImpl(new Point2D(0, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT,
                                MoveRulesImpl.MoveStrategy.SLIDING, false),
                        new MoveRulesImpl(new Point2D(1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT,
                                MoveRulesImpl.MoveStrategy.SLIDING, false),
                        new MoveRulesImpl(new Point2D(-1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT,
                                MoveRulesImpl.MoveStrategy.SLIDING, false)
                ))
                .build();

        final var loaderController = new LoaderControllerImpl();
        loaderController.load();
        final Map<String, AbstractEntityDefinition> pieces = loaderController
                .getEntityDefinitionCacheEntries()
                .stream()
                .collect(Collectors.toMap(
                        DefinitionCacheEntry::pieceId,
                        DefinitionCacheEntry::abstractEntityDefinition,
                        (existing, replacement) -> existing
                ));

        assertEquals(pawn, pieces.get("pawn"));
        assertEquals(rook, pieces.get("rook"));
    }
}

// CHECKSTYLE: MagicNumber ON
