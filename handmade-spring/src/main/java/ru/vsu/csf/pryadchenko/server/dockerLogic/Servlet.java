package ru.vsu.csf.pryadchenko.server.dockerLogic;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.vsu.csf.pryadchenko.server.dockerLogic.annotation.*;
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

public class Servlet {

    private final ApplicationContext applicationContext;

    public Servlet(JarFile jar) {
        this.applicationContext = new ApplicationContext(jar);
    }

    private void doGet(HttpRequest request, HttpResponse response) throws IOException {
        String mapping = request.getPath().split("/", 3)[2];
        if (mapping.endsWith("/")) {
            mapping = mapping.substring(0, mapping.length() - 1);
        }
        Endpoint endpoint = applicationContext.getEndpointManager().fetchGetPoint(mapping);
        Method method = endpoint.getMethod();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> requestParams = request.getParams();
        List<String> params = new ArrayList<>();
        for (Annotation[] annotations : method.getParameterAnnotations()) {
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(Param.class)) {
                    String name = ((Param) annotation).value();
                    params.add(requestParams.get(name));
                }
            }
        }
        byte[] body = null;
        try {
            ContentType typeAnnotation = method.getAnnotation(ContentType.class);
            String contentType = typeAnnotation == null ? "application/json" : typeAnnotation.value();
            response.putHeader("Content-Type", contentType);
            switch (contentType) {
                case ("image/jpeg"):
                case ("image/gif"): {
                    body = Base64.getDecoder().decode(method.invoke(endpoint.getInstance(), params.toArray()).toString());
                    break;
                }
                case ("text/html; charset=UTF-8"):
                case ("text/css"):
                case ("application/javascript"): {
                    body = method.invoke(endpoint.getInstance(), params.toArray()).toString().getBytes(StandardCharsets.UTF_8);
                    break;
                }
                case ("application/json"): {
                    Object result = method.invoke(endpoint.getInstance(), params.toArray());
                    String str = mapper.writeValueAsString(result);
                    body = str.getBytes(StandardCharsets.UTF_8);
                    break;
                }
            }
        } catch (Exception e) {
            response.setStatus("418 I’m a teapot");
            body = response.getStatus().getBytes(StandardCharsets.UTF_8);
            e.printStackTrace();
        }
        response.setBody(body);
        response.send();
    }

    private void doPost(HttpRequest request, HttpResponse response) throws IOException {
        String mapping = request.getPath().split("/", 3)[2];
        if (mapping.endsWith("/")) {
            mapping = mapping.substring(0, mapping.length() - 1);
        }
        Endpoint endpoint = applicationContext.getEndpointManager().fetchPostPoint(mapping);
        Method method = endpoint.getMethod();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> requestParams = request.getParams();
        List<Object> params = new ArrayList<>();
        Iterator<Class<?>> typeIterator = Arrays.stream(method.getParameterTypes()).iterator();
        for (Annotation[] annotations : method.getParameterAnnotations()) {
            Class<?> paramType = typeIterator.next();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(Param.class)) {
                    params.add(
                            requestParams.get(((Param) annotation).value())
                    );
                } else if (annotation.annotationType().equals(RequestBody.class)) {
                    Object object = mapper.readValue(request.getBody(), paramType);
                    params.add(object);
                }
            }
        }
        byte[] body = null;
        response.putHeader("Content-Type", "application/json");
        try {
            Object result = method.invoke(endpoint.getInstance(), params.toArray());
            String str = mapper.writeValueAsString(result);
            body = str.getBytes(StandardCharsets.UTF_8);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        response.setBody(body);
        response.send();
    }

    private void doError(HttpResponse response) throws IOException {
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