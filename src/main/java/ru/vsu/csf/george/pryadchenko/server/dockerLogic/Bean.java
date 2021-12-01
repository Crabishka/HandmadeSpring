package ru.vsu.csf.george.pryadchenko.server.dockerLogic;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Bean is class-decorator for Class<?>
 * It contains all reflection information about one Class<?>
 */
public class Bean {

    String beanID = null;
    List<Annotation> classAnnotation = new ArrayList<>();
    Map<Class<? extends Annotation>, List<Method>> methods = new HashMap<>();
    Map<Method, List<Annotation>> paramsOfMethods = new HashMap<>();

    public Bean(Class<?> parsedClass) {
        Annotation[] classAnnotations = parsedClass.getAnnotations();
        for (Annotation annotation : classAnnotations) {
            this.classAnnotation.add(annotation);
            if (annotation.annotationType().equals(Controller.class)) {
                beanID = ((Controller) annotation).value(); // FIXME
            }
        }

        Method[] methods = parsedClass.getDeclaredMethods();
        for (Method method : methods) {
            Annotation[] methodAnnotations = method.getAnnotations();
            for (Annotation annotation : methodAnnotations) {
                if (this.methods.containsKey(annotation)) {
                    this.methods.get(annotation).add(method);
                } else {
                    List<Method> list = new ArrayList<>();
                    list.add(method);
                    this.methods.put(annotation.annotationType(), list);
                }
            }
            List<Annotation> params = new ArrayList<>();

            Annotation[][] paramsAnnotation = method.getParameterAnnotations();
            for (Annotation[] annotations : paramsAnnotation) {
                params.addAll(Arrays.asList(annotations));
            }

            this.paramsOfMethods.put(method, params);
        }

    }


    public List<Method> getMethodsByAnnotation(Class<? extends Annotation> annotation) {
        return this.methods.get(annotation);
    }

    public List<Annotation> getParamsByMethod(Method method) {
        return this.paramsOfMethods.get(method);
    }


    public List<Annotation> getClassAnnotation() {
        return classAnnotation;
    }

}
