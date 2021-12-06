package ru.vsu.csf.pryadchenko.server.dockerLogic;


import com.fasterxml.jackson.databind.ObjectMapper;
import ru.vsu.csf.pryadchenko.server.dockerLogic.annotation.Controller;
import ru.vsu.csf.pryadchenko.server.dockerLogic.annotation.GetMapping;
import ru.vsu.csf.pryadchenko.server.dockerLogic.annotation.Param;
import ru.vsu.csf.pryadchenko.server.dockerLogic.annotation.PostMapping;
import ru.vsu.csf.pryadchenko.server.http.request.HttpRequest;
import ru.vsu.csf.pryadchenko.server.http.request.RequestType;
import ru.vsu.csf.pryadchenko.server.http.response.HttpResponse;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.JarFile;

/**
 * Servlet is used for making HTTP response by getting and parsing HTTP request.
 * It also contains Factory to
 */
public class Servlet {

    protected Factory factory;
    private String beanPath;
    private String methodPath;

    public Servlet(JarFile jar) {
        this.factory = new Factory(jar);
    }

//    @GetMapping
//    public void a(){}

    public void doGet(HttpRequest request, HttpResponse response) throws IOException {
        // Get needful Bean and Method
        String[] path = request.getPath().split("/");
        try {
            beanPath = path[2];
        } catch (Exception e) {
            beanPath = "";
        }
        Bean bean = factory.getByAnnotationAndName(Controller.class, beanPath);
        try {
            methodPath = path[3];
        } catch (Exception e) {
            methodPath = "";
        }

        Method method = bean.getMethodsByAnnotationAndValue(new AnnotationBinder(GetMapping.class), methodPath);
        //Annotation getMapping = AnnotationParser.annotationForMap(GetMapping.class, Collections.singletonMap("", ""));
        // Get params
        Map<String, String> map = request.getParams();
        List<String> params = new ArrayList<>();
        for (Annotation annotation : bean.getParamsByMethod(method)) {
            params.add(map.get(((Param) annotation).name()) == null ? "" : map.get(((Param) annotation).name())); // TODO обработка аннотаций параметров
        }
        // Prepare Body
        byte[] body = null;
        try {
            AnnotationBinder contentType = bean.getAnnotationByMethodAndName("ContentType", method);
            if (contentType != null) {
                response.putHeader("Content-Type", contentType.value);
                switch (contentType.value) {
                    case ("image/jpeg"):
                    case ("image/gif"): {
                        body = Base64.getDecoder().decode(method.invoke(null, params.toArray()).toString());  // FIXME
                        break;
                    }
                    case ("text/html; charset=UTF-8"):
                    case ("text/css"):
                    case ("application/javascript"): {
                        body = method.invoke(null, params.toArray()).toString().getBytes(StandardCharsets.UTF_8);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            response.setStatus("418 I’m a teapot");
            body = response.getStatus().getBytes(StandardCharsets.UTF_16);
            e.printStackTrace();
        }
        // Set response
        response.setBody(body);
        response.send();
    }

    public void doPost(HttpRequest request, HttpResponse response) throws IOException {
        String[] path = request.getPath().split("/");
        try {
            beanPath = path[2];
        } catch (Exception e) {
            beanPath = "";
        }
        Bean bean = factory.getByAnnotationAndName(Controller.class, beanPath);
        try {
            methodPath = path[3];
        } catch (Exception e) {
            methodPath = "";
        }
        ObjectMapper mapper = new ObjectMapper();
        Method method = bean.getMethodsByAnnotationAndValue(new AnnotationBinder(PostMapping.class), methodPath);
        Map<String, String> map = request.getParams();
        List<Object> params = new ArrayList<>();
        for (Annotation annotation : bean.getParamsByMethod(method)) {
            if (((Param) annotation).requestBody()) {

                Object object = mapper.readValue(request.getBody(), ((Param) annotation).type());
                params.add(object);
            } else {
                params.add(map.get(((Param) annotation).name()) == null ? "" : map.get(((Param) annotation).name()));
            }
        }
        // Prepare Body
        byte[] body = null;
        response.putHeader("Content-Type", "application/json");
        try {
            Object result = method.invoke(null, params.toArray());
//            Class<?> returnType = method.getReturnType();
            body = result.toString().getBytes(StandardCharsets.UTF_8);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        // Set response
        response.setBody(body);
        response.send();

    }

    public void doError(HttpResponse response) throws IOException {
        response.setStatus("501 Not Implemented");
        response.setBody("Type doesn't supported".getBytes(StandardCharsets.UTF_8));
        response.send();
    }

    public void doResponse(HttpRequest request, HttpResponse response) throws IOException {
        RequestType type = request.getRequestType();
        switch (type) {
            case GET: {
                doGet(request, response);
                break;
            }
            case POST: {
                doPost(request, response);
                break;
            }
            default: {
                doError(response);
            }
        }
    }

}