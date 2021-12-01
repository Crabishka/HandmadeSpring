package ru.vsu.csf.george.pryadchenko.server.dockerLogic;


import org.reflections.Reflections;
import ru.vsu.csf.george.pryadchenko.server.http.request.HttpRequest;
import ru.vsu.csf.george.pryadchenko.server.http.request.RequestType;
import ru.vsu.csf.george.pryadchenko.server.http.response.HttpResponse;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Servlet {

    Map<String, Bean> beanMap = new HashMap<>();
    String packagePath;

    public Servlet(String pack) {
        packagePath = pack;
        Reflections reflections = null;
        reflections = new Reflections(pack); // TODO переписать


        Set<Class<?>> set = reflections.getTypesAnnotatedWith(Controller.class); // TODO переписать

        for (Class<?> aClass : set) {
            String path = aClass.getAnnotation(Controller.class).value();
            beanMap.put(path, new Bean(aClass));
        }
    }


    public void doGet(HttpRequest request, HttpResponse response) throws IOException {
        String[] path = request.getPath().split("/");
        Bean bean = beanMap.get("/" + path[2]);
        List<Method> methods = bean.getMethodsByAnnotation(GetMapping.class);
        Map<String, String> map = request.getParams();


        for (Method method : methods) {
            List<String> params = new ArrayList<>();

            for (Param annotation : bean.getParamsByMethod(method)) {
                params.add(map.get(annotation.name()) == null ? "" : map.get(annotation.name())); // TODO обработка аннотаций параметров
            }

            byte[] body = null;
            try {
                body = method.invoke(null, params.toArray()).toString().getBytes(StandardCharsets.UTF_16);  // FIXME
            } catch (Exception e) {
                response.setStatus("418 I’m a teapot");
                body = response.getStatus().getBytes(StandardCharsets.UTF_16);
                e.printStackTrace();
            }
            response.putHeader("Content-Type", "text/html; charset=UTF-8"); // FIXME
            response.setBody(body);
            response.send();

        }
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