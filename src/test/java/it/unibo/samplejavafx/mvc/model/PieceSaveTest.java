//package it.unibo.samplejavafx.mvc.model;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import it.unibo.samplejavafx.mvc.model.entity.PieceType;
//import it.unibo.samplejavafx.mvc.model.loader.JsonViews;
//import it.unibo.samplejavafx.mvc.model.entity.Piece;
//import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
//import it.unibo.samplejavafx.mvc.model.movement.MoveRulesImpl;
//import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
//import org.junit.jupiter.api.Test;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class PieceSaveTest {
//    @Test
//    void test() throws IOException {
//        final ObjectMapper mapper = new ObjectMapper();
//        mapper.enable(SerializationFeature.INDENT_OUTPUT);
//        final Piece p = new Piece("pawn", "Pawn", 0, "/home/giacomo/Documents/pawn.jpg", PlayerColor.NONE,
//                3, PieceType.PAWN, List.of(
//                new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING),
//                new MoveRulesImpl(new Point2D(1, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING)
//        ));
//
//            mapper.writerWithView(JsonViews.FirstLoading.class).writeValue(new File("src/main/resources/EntityResources/StandardChessPieces/pieces/Pawn.json"), p);
//        }
//
//    @Test
//    void testPawnFirstLoading() throws IOException {
//        final ObjectMapper mapper = new ObjectMapper();
//        final Piece pInit = mapper.readerWithView(JsonViews.FirstLoading.class).readValue(new File("src/main/resources/EntityResources/StandardChessPieces/pieces/Pawn.json"), Piece.class);
//        final Piece p = new Piece("pawn", "Pawn", 0, "/home/giacomo/Documents/pawn.jpg", PlayerColor.NONE,
//                3, PieceType.PAWN,  List.of(
//                new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.JUMPING),
//                new MoveRulesImpl(new Point2D(1, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING)
//        ));
//        assertEquals(p, pInit);
//    }
//
//    @Test
//    void testRookFirstLoading() throws IOException {
//        final ObjectMapper mapper = new ObjectMapper();
//        final Piece pInit = mapper.readerWithView(JsonViews.FirstLoading.class).readValue(new File("src/main/resources/EntityResources/StandardChessPieces/pieces/Rook.json"), Piece.class);
//        final Piece r = new Piece("rook", "Rook", 0, "/home/giacomo/Documents/pawn.jpg", PlayerColor.NONE,
//                5, PieceType.TOWER,  List.of(
//                new MoveRulesImpl(new Point2D(0, 1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING),
//                new MoveRulesImpl(new Point2D(0, -1), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING),
//                new MoveRulesImpl(new Point2D(1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING),
//                new MoveRulesImpl(new Point2D(-1, 0), MoveRulesImpl.MoveType.MOVE_AND_EAT, MoveRulesImpl.MoveStrategy.SLIDING)
//        ));
//        assertEquals(r, pInit);
//    }
//}
