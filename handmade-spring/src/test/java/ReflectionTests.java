import ru.vsu.csf.george.pryadchenko.server.dockerLogic.*;
import ru.vsu.csf.george.pryadchenko.server.logic.GetProperties;
import junit.framework.TestCase;
import org.reflections.Reflections;
import sun.reflect.annotation.AnnotationParser;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class ReflectionTests extends TestCase {

    public void testReflection() {
        Annotation getMapping = AnnotationParser.annotationForMap(GetMapping.class, Collections.singletonMap("value", "123"));
        AnnotationBinder annotationBinder = new AnnotationBinder(getMapping);
    }

    public void testAnnotation() throws IOException {
        Map<String, Class<?>> controllers = new HashMap<>();

        Reflections reflections = new Reflections(GetProperties.getProperty("servlet_docker"));


        Set<Class<?>> set = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> aClass : set) {
            String path = aClass.getAnnotation(Controller.class).value();
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

    public void testPackage() throws IOException {
        String pathToPackage = "src/main/java/ru/vsu/csf/george/pryadchenko/server/docker".replace('.', '/');
        File file = new File(pathToPackage);
        List<File> list = Arrays.asList(file.listFiles());
        System.out.println(list.get(1).getName());
        System.out.println(list.get(1).getPath());

    }

}
