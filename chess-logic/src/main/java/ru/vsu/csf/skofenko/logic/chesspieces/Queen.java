package ru.vsu.csf.skofenko.logic.chesspieces;

import ru.vsu.csf.skofenko.logic.geometry.Coordinate;
import ru.vsu.csf.skofenko.logic.model.GameLogic;

import java.util.Collection;

public class Queen extends ChessPiece {
    public Queen(ChessColor color) {
        super(color);
    }

    @Override
    protected Collection<Coordinate> getPossibleCells(GameLogic logic, boolean toGo) {
        Collection<Coordinate> collection = getCellsOfStraightPieces(logic, this, false, toGo);
        collection.addAll(getCellsOfStraightPieces(logic, this, true, toGo));
        return collection;
    }

    private Queen(){
    }
}
