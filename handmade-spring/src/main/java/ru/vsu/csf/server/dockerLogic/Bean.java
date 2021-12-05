<<<<<<< HEAD:handmade-spring/src/main/java/ru/vsu/csf/pryadchenko/spring/dockerLogic/Bean.java
package ru.vsu.csf.pryadchenko.spring.dockerLogic;
=======
package ru.vsu.csf.server.dockerLogic;

import ru.vsu.csf.server.dockerLogic.annotation.Controller;
>>>>>>> BROKEN:handmade-spring/src/main/java/ru/vsu/csf/server/dockerLogic/Bean.java

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Bean is class-decorator for Class<?>
 * It contains all reflection information about one Class<?>
 */
public class Bean {

    String beanID = null;
    List<AnnotationBinder> classAnnotation = new ArrayList<>();
    Map<Method, Map<String, AnnotationBinder>> methods = new HashMap<>(); // TODO remake using Map<Method,Map<String, Annotation>>
    Map<Method, List<Annotation>> paramsOfMethods = new HashMap<>();

    public Bean(Class<?> parsedClass) {
        Annotation[] classAnnotations = parsedClass.getAnnotations();
        for (Annotation annotation : classAnnotations) {
            this.classAnnotation.add(new AnnotationBinder(annotation));
            if (annotation.annotationType().equals(Controller.class)) {
                beanID = ((Controller) annotation).value();
            }
        }


        Method[] methods = parsedClass.getDeclaredMethods();

        for (Method method : methods) {
            Annotation[] methodAnnotations = method.getAnnotations();
            this.methods.put(method, new HashMap<>());
            Map<String, AnnotationBinder> annotations = this.methods.get(method);
            for (Annotation annotation : methodAnnotations) {
                String name = annotation.toString();
                name = name.substring(0, name.indexOf('('));
                name = name.substring(name.lastIndexOf('.') + 1);
                annotations.put(name, new AnnotationBinder(annotation));
            }
            List<Annotation> params = new ArrayList<>();

            Annotation[][] paramsAnnotation = method.getParameterAnnotations();
            for (Annotation[] ParamAnnotations : paramsAnnotation) {
                params.addAll(Arrays.asList(ParamAnnotations));
            }

            this.paramsOfMethods.put(method, params);
        }

    }


    public Method getMethodsByAnnotationAndValue(AnnotationBinder annotation, String value) {
        for (Map.Entry<Method, Map<String, AnnotationBinder>> entry : this.methods.entrySet()) {
            for (Map.Entry<String,AnnotationBinder> annotationEntry : entry.getValue().entrySet()) {
                if (annotationEntry.getValue().equals(annotation) && annotationEntry.getValue().value.equals(value)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    public Map<String, AnnotationBinder> getAnnotationsByMethod(Method method) {
        return methods.get(method);
    }

    public AnnotationBinder getAnnotationByMethodAndName(String name, Method method) {
        return methods.get(method).get(name);
    }

    public List<Annotation> getParamsByMethod(Method method) {
        return this.paramsOfMethods.get(method);
    }


    public List<AnnotationBinder> getClassAnnotation() {
        return classAnnotation;
    }

}
