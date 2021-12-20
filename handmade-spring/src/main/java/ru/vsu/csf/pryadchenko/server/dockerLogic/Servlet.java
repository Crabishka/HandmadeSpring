package ru.vsu.csf.pryadchenko.server.dockerLogic;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.vsu.csf.pryadchenko.server.dockerLogic.annotation.ContentType;
import ru.vsu.csf.pryadchenko.server.dockerLogic.annotation.Param;
import ru.vsu.csf.pryadchenko.server.dockerLogic.annotation.RequestBody;
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
        String mapping = parseMapping(request);
        Endpoint endpoint = applicationContext.getEndpointManager().fetchGetPoint(mapping);
        Method method = endpoint.getMethod();
        ObjectMapper mapper = new ObjectMapper();
        Object[] params = parseParams(method, request, mapper);
        byte[] body = null;
        try {
            ContentType typeAnnotation = method.getAnnotation(ContentType.class);
            String contentType = typeAnnotation == null ? "application/json" : typeAnnotation.value();
            response.putHeader("Content-Type", contentType);
            switch (contentType) {
                case ("image/jpeg"):
                case ("image/gif"): {
                    body = Base64.getDecoder().decode(method.invoke(endpoint.getInstance(), params).toString());
                    break;
                }
                case ("text/html; charset=UTF-8"):
                case ("text/css"):
                case ("application/javascript"): {
                    body = method.invoke(endpoint.getInstance(), params).toString().getBytes(StandardCharsets.UTF_8);
                    break;
                }
                case ("application/json"): {
                    Object result = method.invoke(endpoint.getInstance(), params);
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
        String mapping = parseMapping(request);
        Endpoint endpoint = applicationContext.getEndpointManager().fetchPostPoint(mapping);
        Method method = endpoint.getMethod();
        ObjectMapper mapper = new ObjectMapper();
        Object[] params = parseParams(method, request, mapper);
        byte[] body = null;
        response.putHeader("Content-Type", "application/json");
        try {
            Object result = method.invoke(endpoint.getInstance(), params);
            String str = mapper.writeValueAsString(result);
            body = str.getBytes(StandardCharsets.UTF_8);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        response.setBody(body);
        response.send();
    }

    private String parseMapping(HttpRequest request) {
        String mapping = request.getPath().split("/", 3)[2];
        if (mapping.endsWith("/")) {
            mapping = mapping.substring(0, mapping.length() - 1);
        }
        return mapping;
    }

    private Object[] parseParams(Method method, HttpRequest request, ObjectMapper mapper) throws IOException {
        Map<String, String> requestParams = request.getParams();
        List<Object> params = new ArrayList<>();
        Iterator<Class<?>> typeIterator = Arrays.stream(method.getParameterTypes()).iterator();
        for (Annotation[] annotations : method.getParameterAnnotations()) {
            Class<?> paramType = typeIterator.next();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(Param.class)) {
                    String str = requestParams.get(((Param) annotation).value());
                    Object object;
                    if (paramType == String.class) {
                        object = str;
                    } else {
                        object = mapper.readValue(str, paramType);
                    }
                    params.add(object);
                } else if (annotation.annotationType().equals(RequestBody.class)) {
                    Object object = mapper.readValue(request.getBody(), paramType);
                    params.add(object);
                }
            }
        }
        return params.toArray();
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