package Server.Docker;

import Server.Http.Request.HttpRequest;
import Server.Http.Request.RequestType;
import Server.Http.Response.HttpResponse;
import Server.ServerLogic.GetProperties;
import org.reflections.Reflections;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class DispatcherServlet implements ServletInterface {

    private static DispatcherServlet instance;
    private Map<String, Class<?>> controllers = new HashMap<>();

    public DispatcherServlet() {
        Reflections reflections = null;
        try {
            reflections = new Reflections(GetProperties.getProperty("servlet_docker"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Set<Class<?>> set = reflections.getTypesAnnotatedWith(RequestMapping.class);

        for (Class<?> aClass : set) {
            String path = aClass.getAnnotation(RequestMapping.class).value();
            controllers.put(path, aClass);
        }
    }

    public static DispatcherServlet getInstance() {
        if (instance == null) {
            instance = new DispatcherServlet();
        }
        return instance;
    }
    /*
     Servlet (controller) return body of response
     */

    @Override
    public void doGet(HttpRequest request, HttpResponse response) throws IOException {
        Class<?> servlet = controllers.get(request.getPath());
        if (servlet == null) return;
        Method[] methods = servlet.getMethods();
        Method method = null;

        for (Method aMethod : methods) {
            if (aMethod.getAnnotation(GetMapping.class) != null) {
                method = aMethod;
                break;
            }
        }

        List<String> params = new ArrayList<>();
        Map<String, String> map = request.getParams();
        Annotation[][] paramsAnnotation = method.getParameterAnnotations(); // how to parse this..............

        for (Annotation[] annotations : paramsAnnotation) {
            for (Annotation annotation : annotations) {
                Param newAnnotation = (Param) annotation;
                params.add(map.get(newAnnotation.name()) == null ? "" : map.get(newAnnotation.name()));
            }
        }

        String body = null;
        try {
            body
                    = (String) method.invoke(null, params.toArray());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        response.setBody(body.getBytes(StandardCharsets.UTF_8));
        response.send();
    }

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        // TODO
    }

    public void doResponse(HttpRequest request, OutputStream outputStream) throws IOException {
           /*
        Получили запрос - check
        распарсили - check
        получили путь - check
        из мапы получили класс - check
        получили тип запроса - check
        передали дальше - check
         */

        RequestType type = request.getRequestType();
        switch (type) {
            case GET:
                doGet(request, new HttpResponse(outputStream));
                break;
            case POST:
                doPost(request, new HttpResponse(outputStream));
                break;
            default:
                doGet(request, new HttpResponse(outputStream));
                break;
        }

    }

}
