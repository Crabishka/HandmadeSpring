package ru.vsu.csf.skofenko.restserver;


import ru.vsu.csf.pryadchenko.server.dockerLogic.annotation.Injection;
import ru.vsu.csf.pryadchenko.server.dockerLogic.annotation.Service;
import ru.vsu.csf.skofenko.logic.chesspieces.ChessColor;
import ru.vsu.csf.skofenko.logic.chesspieces.ChessPiece;
import ru.vsu.csf.skofenko.logic.geometry.Coordinate;
import ru.vsu.csf.skofenko.logic.model.GameLogic;
import ru.vsu.csf.skofenko.logic.model.LogicState;


@Service
public class GameService {

    @Injection
    private static final LogicRepository repository = new LogicRepository();

    private long clientID = Long.MIN_VALUE;

    public long connect() {
        long id = clientID++;
        if (id % 2 == 0) {
            repository.saveNewLogic(id, new MultiPlayerLogic(ChessColor.WHITE));
        } else {
            GameLogic logic = new GameLogic();
            repository.getLogicByClientID(id - 1).setLogic(logic);
            MultiPlayerLogic multiLogic = new MultiPlayerLogic(ChessColor.BLACK);
            multiLogic.setLogic(logic);
            repository.saveNewLogic(id, multiLogic);
        }
        return id;
    }

    public LogicState getLogicState(long key) {
        return repository.getLogicByClientID(key).getLogicState();

    }

    public boolean selectPiece(long key, Coordinate cord) {
        return repository.getLogicByClientID(key).selectPiece(cord);
    }

    public boolean promotePawn(long key, ChessPiece piece) {
        return repository.getLogicByClientID(key).promotePawn(piece);
    }

    public void terminate(long key) {
        if (key % 2 == 0) {
            repository.removeLogicByID(key);
            repository.removeLogicByID(key + 1);
            if (clientID == key + 1) {
                clientID--;
            }
        } else {
            repository.removeLogicByID(key - 1);
            repository.removeLogicByID(key);
        }
    }
}
