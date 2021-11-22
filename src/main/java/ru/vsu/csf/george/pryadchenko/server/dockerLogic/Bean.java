package ru.vsu.csf.george.pryadchenko.server.dockerLogic;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class Bean {

    List<Annotation> classAnnotation;
    Map<Method, List<Annotation>> methods = new HashMap<>();
    Map<Method, List<Param>> paramsOfMethods = new HashMap<>();

    public Bean(Class<?> parsedClass) {
        classAnnotation = Arrays.asList(parsedClass.getAnnotations());

        Method[] methods = parsedClass.getMethods();
        for (Method method : methods) {
            this.methods.put(method, Arrays.asList(method.getAnnotations()));
            List<Param> params = new ArrayList<>();

            Annotation[][] paramsAnnotation = method.getParameterAnnotations();
            for (Annotation[] annotations : paramsAnnotation) {
                for (Annotation annotation : annotations) {
                    params.add((Param) annotation);
                }
            }

            this.paramsOfMethods.put(method, params);
        }

    }

}
