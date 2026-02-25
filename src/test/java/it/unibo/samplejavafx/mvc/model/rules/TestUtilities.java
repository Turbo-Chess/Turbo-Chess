package it.unibo.samplejavafx.mvc.model.rules;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PieceDefinition;

public class TestUtilities {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String PIECES_PATH = "src/main/resources/EntityResources/StandardChessPieces/pieces/";

    private TestUtilities() {}

    private static Piece createPiece(final PlayerColor color, final Integer id, final PieceDefinition p) throws StreamReadException, DatabindException, IOException {
        return new Piece.Builder().entityDefinition(p).gameId(id).playerColor(color).build();
    }

    public static Piece createKing(final PlayerColor color, final Integer idCount) throws StreamReadException, DatabindException, IOException {
        final PieceDefinition king = MAPPER.readValue(new File(PIECES_PATH + "King.json"), PieceDefinition.class);
        return createPiece(color, idCount, king);
    }

    public static Piece createRook(final PlayerColor color, final Integer idCount) throws StreamReadException, DatabindException, IOException {
        final PieceDefinition rook = MAPPER.readValue(new File(PIECES_PATH + "Rook.json"), PieceDefinition.class);
        return createPiece(color, idCount, rook);
    }

    public static Piece createBishop(final PlayerColor color, final Integer idCount) throws StreamReadException, DatabindException, IOException {
        final PieceDefinition bishop = MAPPER.readValue(new File(PIECES_PATH + "Bishop.json"), PieceDefinition.class);
        return createPiece(color, idCount, bishop);
    }

    public static Piece createQueen(final PlayerColor color, final Integer idCount) throws StreamReadException, DatabindException, IOException {
        final PieceDefinition queen = MAPPER.readValue(new File(PIECES_PATH + "Queen.json"), PieceDefinition.class);
        return createPiece(color, idCount, queen);
    }

    public static Piece createKnight(final PlayerColor color, final Integer idCount) throws StreamReadException, DatabindException, IOException {
        final PieceDefinition knight = MAPPER.readValue(new File(PIECES_PATH + "Knight.json"), PieceDefinition.class);
        return createPiece(color, idCount, knight);
    }
    public static Piece createPawn(final PlayerColor color, final Integer idCount) throws StreamReadException, DatabindException, IOException {
        final PieceDefinition knight = MAPPER.readValue(new File(PIECES_PATH + "Pawn.json"), PieceDefinition.class);
        return createPiece(color, idCount, knight);
    }
}
