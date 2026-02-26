package it.unibo.samplejavafx.mvc.model.chessboard.boardfactory;

import it.unibo.samplejavafx.mvc.controller.loadercontroller.LoaderController;
import it.unibo.samplejavafx.mvc.model.chessboard.BoardObserver;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoardImpl;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.entity.PlayerColor;
import it.unibo.samplejavafx.mvc.model.entity.entitydefinition.PieceDefinition;
import it.unibo.samplejavafx.mvc.model.loadout.Loadout;

public class BoardFactoryImpl {
    private final LoaderController loaderController;
    private int gameId = 0;

    public BoardFactoryImpl(final LoaderController loaderController) {
        this.loaderController = loaderController;
    }

    public ChessBoard createPopulatedChessboard(final Loadout whiteLoadout, final Loadout blackLoadout, final BoardObserver boardObserver) {
        final ChessBoard board = new ChessBoardImpl();
        board.addObserver(boardObserver);

        whiteLoadout.getEntries().forEach(
                wEntry -> {
                    board.setEntity(wEntry.position(), new Piece.Builder()
                            .entityDefinition((PieceDefinition) loaderController.getEntityCache().get(wEntry.packId()).get(wEntry.pieceId()))
                            .gameId(gameId)
                            .playerColor(PlayerColor.WHITE)
                            .build()
                    );
                    gameId++;
                }
        );

        blackLoadout.getEntries().forEach(
                bEntry -> {
                    board.setEntity(bEntry.position(), new Piece.Builder()
                            .entityDefinition((PieceDefinition) loaderController.getEntityCache().get(bEntry.packId()).get(bEntry.pieceId()))
                            .gameId(gameId)
                            .playerColor(PlayerColor.BLACK)
                            .build()
                    );
                    gameId++;
                }
        );
        return board;
    }

}
