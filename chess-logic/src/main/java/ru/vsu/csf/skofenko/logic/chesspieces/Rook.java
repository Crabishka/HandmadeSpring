package ru.vsu.csf.skofenko.logic.chesspieces;

import ru.vsu.csf.skofenko.logic.geometry.Coordinate;
import ru.vsu.csf.skofenko.logic.model.GameLogic;

import java.util.Collection;

public class Rook extends ChessPiece {
    public Rook(ChessColor color) {
        super(color);
    }

    @Override
    public Collection<Coordinate> getPossibleCells(GameLogic logic, boolean toGo) {
        return getCellsOfStraightPieces(logic, this, true, toGo);
    }

    private Rook(){
    }
}
