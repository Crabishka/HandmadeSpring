package ru.vsu.csf.pryadchenko.spring.dockerLogic;


import ru.vsu.csf.pryadchenko.spring.http.request.HttpRequest;
import ru.vsu.csf.pryadchenko.spring.http.request.RequestType;
import ru.vsu.csf.pryadchenko.spring.http.response.HttpResponse;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Servlet is used for making HTTP response by getting and parsing HTTP request.
 * It also contains Factory to
 */
public class Servlet {

    private final Factory factory;

    public Servlet(String pack) {

        this.factory = new Factory(pack);
    }


    public void doGet(HttpRequest request, HttpResponse response) throws IOException {
        String[] path = request.getPath().split("/");
        Bean bean = factory.getByAnnotationAndName(Controller.class,"/" + path[2]);
        List<Method> methods = bean.getMethodsByAnnotation(GetMapping.class);
        Map<String, String> map = request.getParams();


        for (Method method : methods) {
            List<String> params = new ArrayList<>();

            for (Annotation  annotation : bean.getParamsByMethod(method)) {
                params.add(map.get( ((Param) annotation).name()) == null ? "" : map.get(((Param) annotation).name())); // TODO обработка аннотаций параметров
            }



            byte[] body;
            try {
                body = method.invoke(null, params.toArray()).toString().getBytes(StandardCharsets.UTF_16);  // FIXME
            } catch (Exception e) {
                response.setStatus("418 I’m a teapot");
                body = response.getStatus().getBytes(StandardCharsets.UTF_16);
                e.printStackTrace();
            }
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