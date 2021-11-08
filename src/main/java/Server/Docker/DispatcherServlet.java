package Server.Docker;

import Server.Http.Request.HttpRequest;
import Server.Http.Response.HttpResponse;
import Server.ServerLogic.GetProperties;
import org.reflections.Reflections;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

public class DispatcherServlet implements ServletInterface {

    @Override
    public void doGet(HttpRequest request, HttpResponse response) {

    }

    public ServletInterface getServlet(HttpRequest request) {
        String path = request.getPath();

        Reflections reflections = null;
        try {
            reflections = new Reflections(GetProperties.getProperty("servlet_docker"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Set<Class<?>> set = reflections.getTypesAnnotatedWith(RequestMapping.class);

        for (Class<?> aClass : set) {  // TODO rename this
            if (Arrays.stream(aClass.getInterfaces()).noneMatch(ServletInterface.class::equals))
                continue;  // does class implement Servlet
            Annotation[] annotations = aClass.getAnnotations(); // get all annotations
            for (Annotation annotation : annotations) {
                if (annotation instanceof RequestMapping) {
                    if (((RequestMapping) annotation).value().equals(path)) {
                        try {
                            ServletInterface servletInterface = (ServletInterface) aClass.newInstance();
                            return servletInterface;
                        } catch (InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }

    public void doResponse(HttpRequest request, OutputStream outputStream) {
        ServletInterface servletInterface = getServlet(request);
        if (servletInterface == null) return;
        HttpResponse response = new HttpResponse(outputStream);
        switch (request.getRequestType()) {
            case GET:  // TODO
                Method[] methods = servletInterface.getClass().getMethods();
                for (Method method : methods) {
                    if (Arrays.asList(method.getAnnotations()).contains(GetMapping.class)) {
                        try {
                            method.invoke(servletInterface, request, response);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            default:
        }
    }

}
