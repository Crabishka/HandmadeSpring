import ru.vsu.csf.george.pryadchenko.server.docker.*;
import ru.vsu.csf.george.pryadchenko.server.logic.GetProperties;
import junit.framework.TestCase;
import org.reflections.Reflections;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ReflectionTests extends TestCase {

    public void testReflection() {

        String path = "/adder";

        Reflections reflections = new Reflections("ru.vsu.csf.george.pryadchenko.Server.Docker.Servlet");
        Set<Class<?>> set = reflections.getTypesAnnotatedWith(RequestMapping.class);

        for (Class<?> aClass : set) {
            if (Arrays.stream(aClass.getInterfaces()).noneMatch(Servlet.class::equals))
                continue;  // does class implement Servlet
            Annotation[] annotations = aClass.getAnnotations(); // get all annotations
            for (Annotation annotation : annotations) {
                if (annotation instanceof RequestMapping) {
                    if (((RequestMapping) annotation).value().equals(path)) {
                        try {
                            Servlet servlet = (Servlet) aClass.newInstance();

                        } catch (InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public void testAnnotation() throws IOException {
        Map<String, Class<?>> controllers = new HashMap<>();

        Reflections reflections = new Reflections(GetProperties.getProperty("servlet_docker"));


        Set<Class<?>> set = reflections.getTypesAnnotatedWith(RequestMapping.class);

        for (Class<?> aClass : set) {
            String path = aClass.getAnnotation(RequestMapping.class).value();
            controllers.put(path, aClass);
        }

        Class<?> servlet = controllers.get("/adder");
        Method[] methods = servlet.getMethods();
        Method method = null;

        for (Method aMethod : methods) {
            if (aMethod.getAnnotation(GetMapping.class) != null) {
                method = aMethod;
                break;
            }
        }
        Annotation[][] paramsAnnotation = method.getParameterAnnotations();

        for (Annotation[] annotations : paramsAnnotation) {
            for (Annotation annotation : annotations) {
                Param newAnnotation = (Param) annotation;
                System.out.println(newAnnotation.name());


            }
        }

    }

}
