package it.unibo.samplejavafx.mvc.model.rules;

/*
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import it.unibo.samplejavafx.mvc.model.chessboard.ChessBoard;
import it.unibo.samplejavafx.mvc.model.entity.Entity;
import it.unibo.samplejavafx.mvc.model.entity.Piece;
import it.unibo.samplejavafx.mvc.model.point2d.Point2D;
import it.unibo.samplejavafx.mvc.model.entity.Moveable;*/

/**
 * Placeholder.
 */
public interface AdvancedRules {


    /*static boolean check() {

    }*/

    /*
    static boolean checkmate() {

    }
    */

    /*static boolean draw() {

    }*/


    /*static boolean enPassant() {

    }*/


    /*static boolean castle(final ChessBoard cb, final boolean currentColor) {

    }*/

/* 
    private static List<Piece> scan(final ChessBoard cb, final boolean currentColor, final List<Point2D> targets) {
        final List<Point2D> list = cb.getCells().stream()
                .filter(pos -> !cb.isFree(pos))
                .filter(pos -> !cb.getEntity(pos).get().isWhite() == currentColor)
                .collect(Collectors.toList());

        for (Point2D piece : list) {
            if(cb.getEntity(piece).get() instanceof Moveable moveable) {
                Iterator<Point2D> it = moveable.getValidMoves(piece, cb).iterator();

                while (it.hasNext()) {
                    Point2D trgt = it.next();
                    if(trgt.equals(list))
                }
            }
        }
    }*/
}

