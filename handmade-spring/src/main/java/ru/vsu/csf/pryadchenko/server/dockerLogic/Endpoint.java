package ru.vsu.csf.pryadchenko.server.dockerLogic;


import java.lang.reflect.Method;

class Endpoint {
    private final Object instance;
    private final Method method;

    Object getInstance() {
        return instance;
    }

    Method getMethod() {
        return method;
    }

    Endpoint(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
    }
}
