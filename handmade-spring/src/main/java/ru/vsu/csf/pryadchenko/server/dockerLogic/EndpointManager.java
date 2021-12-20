package ru.vsu.csf.pryadchenko.server.dockerLogic;

import java.util.HashMap;
import java.util.Map;

class EndpointManager {
    private final Map<String, Endpoint> getPointsMap = new HashMap<>();
    private final Map<String, Endpoint> postPointsMap = new HashMap<>();

    Endpoint putGetPoint(String mapping, Endpoint endpoint){
        return getPointsMap.put(mapping, endpoint);
    }

    Endpoint putPostPoint(String mapping, Endpoint endpoint){
        return postPointsMap.put(mapping, endpoint);
    }

    Endpoint fetchGetPoint(String mapping){
        return getPointsMap.get(mapping);
    }

    Endpoint fetchPostPoint(String mapping){
        return postPointsMap.get(mapping);
    }
}
