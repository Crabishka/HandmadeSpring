package ru.vsu.csf.skofenko.restserver;

import ru.vsu.csf.pryadchenko.server.dockerLogic.annotation.*;
import ru.vsu.csf.skofenko.logic.chesspieces.ChessPiece;
import ru.vsu.csf.skofenko.logic.geometry.Coordinate;
import ru.vsu.csf.skofenko.logic.model.LogicState;

@Controller("api")
public class GameController {

    @Injection
    private GameService gameService;

    @PostMapping("connect")
    public long connect() {
        return gameService.connect();
    }

    @GetMapping("state")
    @ContentType
    public LogicState getLogicState(@Param(name = "key") String key) {
        return gameService.getLogicState(Long.parseLong(key));

    }

    @PostMapping("select")
    public boolean selectPiece(@Param(name = "key") String key, @Param(requestBody = true, type = Coordinate.class) Coordinate cord) {
        return gameService.selectPiece(Long.parseLong(key), cord);
    }

    @PostMapping("promote")
    public boolean promotePawn(@Param(name = "key") String key, @Param(requestBody = true, type = ChessPiece.class) ChessPiece piece) {
        return gameService.promotePawn(Long.parseLong(key), piece);
    }

    @PostMapping("terminate")
    public void terminate(@Param(name = "key") String key) {
        gameService.terminate(Long.parseLong(key));
    }
}
