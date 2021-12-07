package ru.vsu.csf.skofenko.restserver;

import ru.vsu.csf.skofenko.logic.chesspieces.ChessColor;
import ru.vsu.csf.skofenko.logic.chesspieces.ChessPiece;
import ru.vsu.csf.skofenko.logic.geometry.Coordinate;
import ru.vsu.csf.skofenko.logic.model.*;

public class MultiPlayerLogic implements IGameLogic {

    private final ChessColor color;
    private GameLogic logic;

    public ChessColor getColor() {
        return color;
    }

    public GameLogic getLogic() {
        return logic;
    }

    public void setLogic(GameLogic logic) {
        this.logic = logic;
    }

    public MultiPlayerLogic(ChessColor color) {
        this.color = color;
    }

    @Override
    public GameState getGameState() {
        if (logic.getNowTurn() == color) {
            return logic.getGameState();
        } else {
            if (logic.getGameState() == GameState.STALEMATE) {
                return GameState.STALEMATE;
            }
            if (logic.getGameState() == GameState.CHECKMATE) {
                return GameState.VICTORY;
            }
            return GameState.PLAYING;
        }
    }

    @Override
    public ChessColor getNowTurn() {
        return color;
    }

    @Override
    public boolean selectPiece(Coordinate cord) {
        return logic.getNowTurn() == color && logic.selectPiece(cord);
    }

    @Override
    public boolean promotePawn(ChessPiece piece) {
        return logic.getNowTurn() == color && logic.promotePawn(piece);
    }

    @Override
    public BoardCell[][] getBoard() {
        BoardCell[][] board = logic.getBoard();
        if (logic.getNowTurn() == color) {
            return board;
        } else {
            BoardCell[][] boardRepresent = new BoardCell[IGameLogic.N][IGameLogic.N];
            for (int i = 0; i < IGameLogic.N; i++) {
                for (int j = 0; j < IGameLogic.N; j++) {
                    boardRepresent[i][j] = new BoardCell(board[i][j].getPiece());
                }
            }
            return boardRepresent;
        }
    }

    public LogicState getLogicState() {
        if (logic == null) {
            return null;
        } else {
            return new LogicState(getNowTurn(), getGameState(), getBoard());
        }
    }
}
