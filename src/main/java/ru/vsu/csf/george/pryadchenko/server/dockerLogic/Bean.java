package ru.vsu.csf.george.pryadchenko.server.dockerLogic;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class Bean {

    List<Annotation> classAnnotation;
    Map<Class<? extends Annotation>, List<Method>> methods = new HashMap<>();
    Map<Method, List<Param>> paramsOfMethods = new HashMap<>();

    public Bean(Class<?> parsedClass) {
        classAnnotation = Arrays.asList(parsedClass.getAnnotations());

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


    public List<Method> getMethodsByAnnotation(Class<? extends Annotation> annotation) {
        return this.methods.get(annotation);
    }

    public List<Param> getParamsByMethod(Method method) {
        return this.paramsOfMethods.get(method);
    }

}
