package it.unibo.samplejavafx.mvc.model.chessboard.boardfactory;

import it.unibo.samplejavafx.mvc.controller.loadercontroller.LoaderController;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.AbstractEntityDefinition;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PieceDefinition;
import it.unibo.samplejavafx.mvc.model.loadout.Loadout;
import it.unibo.samplejavafx.mvc.model.loadout.LoadoutEntry;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A concrete implementation of the {@link BoardFactory} interface.
 *
 * <p>
 * This class uses a {@link LoaderController} to fetch the necessary entity definitions from disk or cache.
 * It manages the creation of unique game IDs for each instantiated piece to ensure proper tracking during the match.
 * </p>
 */
public final class BoardFactoryImpl implements BoardFactory, DefinitionRegistry {
    private final Map<String, Map<String, AbstractEntityDefinition>> entityCache = new HashMap<>();
    private int gameId;

    /**
     * Constructs a new {@code BoardFactoryImpl}.
     *
     * @param definitions the list of preloaded entity definitions to cache.
     */
    public BoardFactoryImpl(final List<DefinitionCacheEntry> definitions) {
        for (final var definitionEntry : definitions) {
            entityCache.computeIfAbsent(definitionEntry.packId(), map -> new HashMap<>());
            entityCache.get(definitionEntry.packId()).put(definitionEntry.pieceId(), definitionEntry.abstractEntityDefinition());
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Iterates through the entries in the provided loadouts and places corresponding pieces onto a new board instance
     * and incrementing the gameId counter to not make duplicates.
     * </p>
     */
    @Override
    public void populateChessboard(
        final Loadout whiteLoadout,
        final Loadout blackLoadout,
        final ChessBoard board
    ) {
        whiteLoadout.getEntries().forEach(wEntry -> addPieceToBoard(board, wEntry, PlayerColor.WHITE));
        blackLoadout.getEntries().forEach(bEntry -> addPieceToBoard(board, bEntry, PlayerColor.BLACK));
    }

    private void addPieceToBoard(final ChessBoard board, final LoadoutEntry entry, final PlayerColor color) {
        board.setEntity(entry.position(), new Piece.Builder()
                .entityDefinition(
                    (PieceDefinition) entityCache.get(entry.packId()).get(entry.pieceId())
                )
                .gameId(gameId)
                .playerColor(color)
                .moved(false)
                .build()
        );
        gameId++;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Utilizes the internal game ID counter to instantiate a new piece with the given definition
     * and places it at the specified coordinate on the board.
     * In that way, newly added pieces will have a different ID from the ones already instantiated.
     * </p>
     */
    @Override
    public void createNewPiece(final Point2D pos, final ChessBoard board, 
                               final String packId, final String pieceId, final PlayerColor color) {
        final var newPiece = new Piece.Builder()
                .entityDefinition((PieceDefinition) entityCache.get(packId).get(pieceId))
                .gameId(gameId)
                .moved(false)
                .playerColor(color)
                .build();

        board.setEntity(pos, newPiece);

        this.gameId++;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getResPackIds() {
        return List.copyOf(entityCache.keySet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, AbstractEntityDefinition> getPackData(final String packId) {
        return Map.copyOf(entityCache.get(packId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AbstractEntityDefinition> getAllDefinitions() {
        return entityCache.values().stream()
                .flatMap(inner -> inner.values().stream())
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractEntityDefinition getDefinition(final String packId, final String pieceId) {
        return entityCache.get(packId).get(pieceId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAllIds() {
        return List.copyOf(entityCache.values().stream()
                .flatMap(pack -> pack.keySet().stream()).toList());
    }
}
