package ru.vsu.csf.george.pryadchenko.server.docker.math;

import ru.vsu.csf.george.pryadchenko.server.docker.*;
import ru.vsu.csf.george.pryadchenko.server.http.request.HttpRequest;
import ru.vsu.csf.george.pryadchenko.server.http.request.RequestType;
import ru.vsu.csf.george.pryadchenko.server.http.response.HttpResponse;
import ru.vsu.csf.george.pryadchenko.server.logic.GetProperties;
import org.reflections.Reflections;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


@WebSocket("/math")
public class DispatcherServlet extends Servlet {


    private Map<String, Class<?>> controllers = new HashMap<>();

    public DispatcherServlet() {
        Reflections reflections = null;
        try {
            reflections = new Reflections(GetProperties.getProperty("servlet_docker")); // TODO переписать
        } catch (IOException e) {
            e.printStackTrace();
        }

        Set<Class<?>> set = reflections.getTypesAnnotatedWith(RequestMapping.class);

        for (Class<?> aClass : set) {
            String path = aClass.getAnnotation(RequestMapping.class).value();
            controllers.put(path, aClass);
        }
    }


    public void doGet(HttpRequest request, HttpResponse response) throws IOException {
        Class<?> servlet = controllers.get(request.getPath());
        if (servlet == null) return;
        List<Method> methods = Arrays.stream(servlet.getMethods()).filter(method -> {
            return method.getAnnotation(GetMapping.class) != null;  // TODO
        }).collect(Collectors.toList());
        Method method = null;

        for (Method aMethod : methods) {
            if (aMethod.getAnnotation(GetMapping.class) != null) {
                method = aMethod;
                break;
            }
        }

        List<String> params = new ArrayList<>();
        Map<String, String> map = request.getParams();
        Annotation[][] paramsAnnotation = method.getParameterAnnotations();

        for (Annotation[] annotations : paramsAnnotation) {
            for (Annotation annotation : annotations) {
                Param newAnnotation = (Param) annotation;
                params.add(map.get(newAnnotation.name()) == null ? "" : map.get(newAnnotation.name())); // TODO обработка аннотаций параметров
            }
        }

        String body = null;
        try {
            body = (String) method.invoke(null, params.toArray());  // FIXME
        } catch (Exception e) {
            response.setStatus("418 I’m a teapot");
            body = response.getStatus();
            e.printStackTrace();
        }
        response.putHeader("Content-Type", "text/html; charset=utf-8");
        response.setBody(body.getBytes(StandardCharsets.UTF_8));
        response.send();
    }


    public void doResponse(HttpRequest request, OutputStream outputStream) throws IOException {
        RequestType type = request.getRequestType();
        switch (type) {
            case GET:
                doGet(request, new HttpResponse(outputStream));
                break;
        }

    }

}
