package ru.vsu.csf.skofenko.restclient;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import ru.vsu.csf.skofenko.logic.chesspieces.ChessColor;
import ru.vsu.csf.skofenko.logic.chesspieces.ChessPiece;
import ru.vsu.csf.skofenko.logic.geometry.Coordinate;
import ru.vsu.csf.skofenko.logic.model.BoardCell;
import ru.vsu.csf.skofenko.logic.model.GameState;
import ru.vsu.csf.skofenko.logic.model.IGameLogic;
import ru.vsu.csf.skofenko.logic.model.LogicState;

import javax.swing.*;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class RestClient implements IGameLogic {
    public static final String SERVER_URL = "http://127.0.0.1:8080/rest-server/api";

    private final RestTemplate template = new RestTemplate();
    private final Long SESSION_ID;
    private final Timer timer = new Timer();
    private final Consumer<String> returnToSingleFunc;
    private LogicState logicState;

    public RestClient(Consumer<String> returnToSingleFunc) {
        this.returnToSingleFunc = returnToSingleFunc;
        try {
            SESSION_ID = template.postForObject(String.format("%s/connect", SERVER_URL), null, Long.class);
        } catch (HttpStatusCodeException | ResourceAccessException e) {
            returnToSingleFunc.accept("no-connection");
            throw e;
        }
    }

    public void waitForOtherPlayer(Runnable repaintFunc) {
        updateLogicState();
        while (logicState == null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                terminate();
                return;
            }
            updateLogicState();
        }
        SwingUtilities.invokeLater(repaintFunc);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                LogicState previousState = logicState;
                updateLogicState();
                if (!Objects.equals(previousState, logicState)) {
                    SwingUtilities.invokeLater(repaintFunc);
                }
            }
        }, 1000, 1000);
    }

    private void updateLogicState() {
        try {
            logicState = template.getForObject(String.format("%s/getState?key={0}", SERVER_URL),
                    LogicState.class, SESSION_ID);
        } catch (HttpStatusCodeException e) {
            returnToSingleFunc.accept("player-left");
            timer.cancel();
        } catch (ResourceAccessException e) {
            returnToSingleFunc.accept("no-connection");
            timer.cancel();
            throw e;
        }
    }

    @Override
    public ChessColor getNowTurn() {
        return logicState.getNowTurn();
    }

    @Override
    public GameState getGameState() {
        return logicState.getGameState();
    }

    @Override
    public BoardCell[][] getBoard() {
        return logicState.getBoard();
    }

    @Override
    public boolean selectPiece(Coordinate coordinate) {
        try {
            Boolean result = template.postForObject(String.format("%s/select?key={0}", SERVER_URL),
                    getHttpEntity(coordinate),Boolean.class, SESSION_ID);
            updateLogicState();
            return Boolean.TRUE.equals(result);
        } catch (HttpStatusCodeException e) {
            returnToSingleFunc.accept("player-left");
            timer.cancel();
        } catch (ResourceAccessException e) {
            returnToSingleFunc.accept("no-connection");
            timer.cancel();
            throw e;
        }
        return false;
    }

    @Override
    public boolean promotePawn(ChessPiece piece) {
        try {
            Boolean result = template.postForObject(String.format("%s/promote?key={0}", SERVER_URL),
                    getHttpEntity(piece),Boolean.class, SESSION_ID);
            updateLogicState();
            return Boolean.TRUE.equals(result);
        } catch (HttpStatusCodeException e) {
            returnToSingleFunc.accept("player-left");
            timer.cancel();
        } catch (ResourceAccessException e) {
            returnToSingleFunc.accept("no-connection");
            timer.cancel();
            throw e;
        }
        return false;
    }

    public void terminate() {
        try {
            template.postForObject(String.format("%s/terminate?key={0}", SERVER_URL), null, Void.class, SESSION_ID);
        } catch (HttpStatusCodeException | ResourceAccessException e) {
            returnToSingleFunc.accept("no-connection");
            throw e;
        } finally {
            timer.cancel();
        }
    }

    private <T> HttpEntity<T> getHttpEntity(T value) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(value, headers);
    }
}
