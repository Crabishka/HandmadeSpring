package ru.vsu.csf.skofenko.logic.chesspieces;

import ru.vsu.csf.skofenko.logic.geometry.Coordinate;
import ru.vsu.csf.skofenko.logic.model.GameLogic;

import java.util.Collection;

public class Bishop extends ChessPiece {
    public Bishop(ChessColor color) {
        super(color);
    }

    @Override
    public Collection<Coordinate> getPossibleCells(GameLogic logic, boolean toGo) {
        return getCellsOfStraightPieces(logic, this, false, toGo);
    }

    private Bishop() {
    }
}
