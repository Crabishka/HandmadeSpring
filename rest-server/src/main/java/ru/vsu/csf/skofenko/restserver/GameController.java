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
    public LogicState getLogicState(@Param("key") String key) {
        return gameService.getLogicState(Long.parseLong(key));
    }

    @PostMapping("select")
    public boolean selectPiece(@Param("key") String key, @RequestBody Coordinate cord) {
        return gameService.selectPiece(Long.parseLong(key), cord);
    }

    @PostMapping("promote")
    public boolean promotePawn(@Param("key") String key, @RequestBody ChessPiece piece) {
        return gameService.promotePawn(Long.parseLong(key), piece);
    }

    @PostMapping("terminate")
    public void terminate(@Param("key") String key) {
        gameService.terminate(Long.parseLong(key));
    }
}
