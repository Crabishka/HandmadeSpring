package ru.vsu.csf.skofenko.restserver;

import ru.vsu.csf.pryadchenko.server.dockerLogic.annotation.*;
import ru.vsu.csf.skofenko.logic.chesspieces.ChessPiece;
import ru.vsu.csf.skofenko.logic.geometry.Coordinate;
import ru.vsu.csf.skofenko.logic.model.LogicState;


@Controller("api")
public class GameController {

    @Injection
    private static final GameService gameService = new GameService();

    @PostMapping("connect")
    public static long doHtml() {
        return gameService.connect();
    }

    @GetMapping("getState")
    @ContentType
    public static LogicState getLogicState(@Param(name = "key") String key) {
        return gameService.getLogicState(Long.parseLong(key));

    }

    @PostMapping("select")
    public static boolean selectPiece(@Param(name = "key") String key, @Param(requestBody = true, type = Coordinate.class) Coordinate cord) {
        return gameService.selectPiece(Long.parseLong(key), cord);
    }

    @PostMapping("promote")
    public static boolean promotePawn(@Param(name = "key") String key, @Param(requestBody = true, type = ChessPiece.class) ChessPiece piece) {
        return gameService.promotePawn(Long.parseLong(key), piece);
    }

    @PostMapping("terminate")
    public static void terminate(@Param(name = "key") String key) {
        gameService.terminate(Long.parseLong(key));
    }
}
