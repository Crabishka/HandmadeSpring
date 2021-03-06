package ru.vsu.csf.skofenko.restserver;

import ru.vsu.csf.pryadchenko.server.dockerLogic.annotation.Repository;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class LogicRepository {
    private final Map<Long, MultiPlayerLogic> logicMap = new ConcurrentHashMap<>();

    public MultiPlayerLogic saveNewLogic(Long clientID, MultiPlayerLogic logic) {
        return logicMap.put(clientID, logic);
    }

    public MultiPlayerLogic getLogicByClientID(Long clientID) {
        MultiPlayerLogic logic = logicMap.get(clientID);
        if (logic != null) {
            return logic;
        } else {
            throw new NoSuchElementException("No user is registered with that ID!");
        }
    }

    public MultiPlayerLogic removeLogicByID(Long clientID) {
        return logicMap.remove(clientID);
    }
}
