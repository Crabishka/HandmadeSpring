package ru.vsu.csf.skofenko.restserver;

import ru.vsu.csf.pryadchenko.server.dockerLogic.annotation.*;
import ru.vsu.csf.skofenko.logic.chesspieces.ChessPiece;
import ru.vsu.csf.skofenko.logic.geometry.Coordinate;
import ru.vsu.csf.skofenko.logic.model.LogicState;

@Controller("api")
public class GameController {

    @Inject
    private GameService gameService;

    @PostMapping("connect")
    public long connect() {
        return gameService.connect();
    }

    @GetMapping("state")
    public LogicState getLogicState(@Param("key") Long key) {
        return gameService.getLogicState(key);
    }

    @PostMapping("select")
    public boolean selectPiece(@Param("key") Long key, @RequestBody Coordinate cord) {
        return gameService.selectPiece(key, cord);
    }

    @PostMapping("promote")
    public boolean promotePawn(@Param("key") Long key, @RequestBody ChessPiece piece) {
        return gameService.promotePawn(key, piece);
    }

    @PostMapping("terminate")
    public void terminate(@Param("key") Long key) {
        gameService.terminate(key);
    }
}
