package it.unibo.turbochess.model.rules;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.unibo.turbochess.model.entity.impl.Piece;
import it.unibo.turbochess.model.entity.impl.PlayerColor;
import it.unibo.turbochess.model.entity.definition.PieceDefinition;

/**
 * Utility class for handling the counter of gameIds, assigned when iterating multiple pieces for tests.
 */
public final class TestUtilities {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String PIECES_PATH = "src/main/resources/EntityResources/StandardChessPieces/pieces/";

    private TestUtilities() { }

    /**
     * Static method that creates and returns a {@link Piece} by the given inputs.
     * 
     * @param color the {@link PlayerColor} of the piece.
     * @param id the id of the piece.
     * @param p the {@link PieceDefinition} of the piece.
     * @return the according {@link Piece}. 
     */
    private static Piece createPiece(final PlayerColor color, final Integer id, final PieceDefinition p) {
        return new Piece.Builder().entityDefinition(p).gameId(id).playerColor(color).build();
    }

    /**
     * Static method that creates a King {@link Piece}.
     * 
     * @param color the {@link PlayerColor} of the piece.
     * @param idCount the id of the piece.
     * @return a King {@link Piece}.
     * @throws StreamReadException if a low-level I/O problem (unexpected end-of-input, network error) occurs (passed through 
     *                             as-is without additional wrapping -- note that this is one case where 
     *                             DeserializationFeature.WRAP_EXCEPTIONS does NOT 
     *                             result in wrapping of exception even if enabled).
     * @throws DatabindException - if underlying input contains invalid content of type 
     *                             JsonParser supports (JSON for default case).
     * @throws IOException - if the input JSON structure does not match structure expected for result 
     *                       type (or has other mismatch issues).
     */
    public static Piece createKing(final PlayerColor color, final Integer idCount)
            throws StreamReadException, DatabindException, IOException {
        final PieceDefinition king = MAPPER.readValue(new File(PIECES_PATH + "King.json"), PieceDefinition.class);
        return createPiece(color, idCount, king);
    }

    /**
     * Static method that creates a Rook {@link Piece}.
     * 
     * @param color the {@link PlayerColor} of the piece.
     * @param idCount the id of the piece.
     * @return a Rook {@link Piece}.
     * @throws StreamReadException if a low-level I/O problem (unexpected end-of-input, network error) occurs (passed through 
     *                             as-is without additional wrapping -- note that this is one case where 
     *                             DeserializationFeature.WRAP_EXCEPTIONS does NOT 
     *                             result in wrapping of exception even if enabled).
     * @throws DatabindException - if underlying input contains invalid content of type 
     *                             JsonParser supports (JSON for default case).
     * @throws IOException - if the input JSON structure does not match structure expected for result 
     *                       type (or has other mismatch issues).
     */
    public static Piece createRook(final PlayerColor color, final Integer idCount)
            throws StreamReadException, DatabindException, IOException {
        final PieceDefinition rook = MAPPER.readValue(new File(PIECES_PATH + "Rook.json"), PieceDefinition.class);
        return createPiece(color, idCount, rook);
    }

    /**
     * Static method that creates a Bishop {@link Piece}.
     * 
     * @param color the {@link PlayerColor} of the piece.
     * @param idCount the id of the piece.
     * @return a Bishop {@link Piece}.
     * @throws StreamReadException if a low-level I/O problem (unexpected end-of-input, network error) occurs (passed through 
     *                             as-is without additional wrapping -- note that this is one case where 
     *                             DeserializationFeature.WRAP_EXCEPTIONS does NOT 
     *                             result in wrapping of exception even if enabled).
     * @throws DatabindException - if underlying input contains invalid content of type 
     *                             JsonParser supports (JSON for default case).
     * @throws IOException - if the input JSON structure does not match structure expected for result 
     *                       type (or has other mismatch issues).
     */
    public static Piece createBishop(final PlayerColor color, final Integer idCount)
            throws StreamReadException, DatabindException, IOException {
        final PieceDefinition bishop = MAPPER.readValue(new File(PIECES_PATH + "Bishop.json"), PieceDefinition.class);
        return createPiece(color, idCount, bishop);
    }

    /**
     * Static method that creates a Queen {@link Piece}.
     * 
     * @param color the {@link PlayerColor} of the piece.
     * @param idCount the id of the piece.
     * @return a Queen {@link Piece}.
     * @throws StreamReadException if a low-level I/O problem (unexpected end-of-input, network error) occurs (passed through 
     *                             as-is without additional wrapping -- note that this is one case where 
     *                             DeserializationFeature.WRAP_EXCEPTIONS does NOT 
     *                             result in wrapping of exception even if enabled).
     * @throws DatabindException - if underlying input contains invalid content of type 
     *                             JsonParser supports (JSON for default case).
     * @throws IOException - if the input JSON structure does not match structure expected for result 
     *                       type (or has other mismatch issues).
     */
    public static Piece createQueen(final PlayerColor color, final Integer idCount)
            throws StreamReadException, DatabindException, IOException {
        final PieceDefinition queen = MAPPER.readValue(new File(PIECES_PATH + "Queen.json"), PieceDefinition.class);
        return createPiece(color, idCount, queen);
    }

    /**
     * Static method that creates a Knight {@link Piece}.
     * 
     * @param color the {@link PlayerColor} of the piece.
     * @param idCount the id of the piece.
     * @return a Knight {@link Piece}.
     * @throws StreamReadException if a low-level I/O problem (unexpected end-of-input, network error) occurs (passed through 
     *                             as-is without additional wrapping -- note that this is one case where 
     *                             DeserializationFeature.WRAP_EXCEPTIONS does NOT 
     *                             result in wrapping of exception even if enabled).
     * @throws DatabindException - if underlying input contains invalid content of type 
     *                             JsonParser supports (JSON for default case).
     * @throws IOException - if the input JSON structure does not match structure expected for result 
     *                       type (or has other mismatch issues).
     */
    public static Piece createKnight(final PlayerColor color, final Integer idCount)
            throws StreamReadException, DatabindException, IOException {
        final PieceDefinition knight = MAPPER.readValue(new File(PIECES_PATH + "Knight.json"), PieceDefinition.class);
        return createPiece(color, idCount, knight);
    }

    /**
     * Static method that creates a Pawn {@link Piece}.
     * 
     * @param color the {@link PlayerColor} of the piece.
     * @param idCount the id of the piece.
     * @return a Pawn {@link Piece}.
     * @throws StreamReadException if a low-level I/O problem (unexpected end-of-input, network error) occurs (passed through 
     *                             as-is without additional wrapping -- note that this is one case where 
     *                             DeserializationFeature.WRAP_EXCEPTIONS does NOT 
     *                             result in wrapping of exception even if enabled).
     * @throws DatabindException - if underlying input contains invalid content of type 
     *                             JsonParser supports (JSON for default case).
     * @throws IOException - if the input JSON structure does not match structure expected for result 
     *                       type (or has other mismatch issues).
     */
    public static Piece createPawn(final PlayerColor color, final Integer idCount)
            throws StreamReadException, DatabindException, IOException {
        final PieceDefinition knight = MAPPER.readValue(new File(PIECES_PATH + "Pawn.json"), PieceDefinition.class);
        return createPiece(color, idCount, knight);
    }
}
