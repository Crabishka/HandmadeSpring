package ru.vsu.csf.george.pryadchenko.server.dockerLogic;


import org.reflections.Reflections;
import ru.vsu.csf.george.pryadchenko.server.http.request.HttpRequest;
import ru.vsu.csf.george.pryadchenko.server.http.request.RequestType;
import ru.vsu.csf.george.pryadchenko.server.http.response.HttpResponse;
import sun.reflect.annotation.AnnotationParser;


import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Servlet is used for making HTTP response by getting and parsing HTTP request.
 * It also contains Factory to
 */
public class Servlet {

    private Factory factory;
    String packagePath;

    public Servlet(String pack) {

        this.factory = new Factory(pack);
    }

//    @GetMapping
//    public void a(){}

    public void doGet(HttpRequest request, HttpResponse response) throws IOException {
        String[] path = request.getPath().split("/");
        Bean bean = factory.getByAnnotationAndName(Controller.class, "/" + path[2]);

        // FIXME Я НЕ ПОНИМАЮ, ЧТО ТУТ ПРОИСХОДИТ
        List<Method> methods = bean.getMethodsByAnnotation(AnnotationParser.annotationForMap(GetMapping.class, Collections.singletonMap("", "")));

        Map<String, String> map = request.getParams();


        for (Method method : methods) {
            List<String> params = new ArrayList<>();

            for (Annotation annotation : bean.getParamsByMethod(method)) {
                params.add(map.get(((Param) annotation).name()) == null ? "" : map.get(((Param) annotation).name())); // TODO обработка аннотаций параметров
            }


            byte[] body = null;
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