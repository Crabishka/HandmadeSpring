package ru.vsu.csf.george.pryadchenko.server.dockerLogic;


import org.reflections.Reflections;
import ru.vsu.csf.george.pryadchenko.server.http.request.HttpRequest;
import ru.vsu.csf.george.pryadchenko.server.http.request.RequestType;
import ru.vsu.csf.george.pryadchenko.server.http.response.HttpResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Servlet {

    Map<String, Bean> beanMap = new HashMap<>();
    String packagePath;

    public Servlet(String pack) {
        packagePath = pack;
        Reflections reflections = null;
        reflections = new Reflections(pack); // TODO переписать


        Set<Class<?>> set = reflections.getTypesAnnotatedWith(RequestMapping.class); // TODO переписать

        for (Class<?> aClass : set) {
            String path = aClass.getAnnotation(RequestMapping.class).value();
            beanMap.put(path, new Bean(aClass));
        }
    }


    public void doGet(HttpRequest request, HttpResponse response) throws IOException {

    }


    public void doResponse(HttpRequest request, HttpResponse response) throws IOException {
        RequestType type = request.getRequestType();
        switch (type) {
            case GET:
                doGet(request, response);
                break;
        }
    }

}