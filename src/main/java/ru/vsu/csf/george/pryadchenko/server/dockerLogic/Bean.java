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
    Map<Method, List<Annotation>> methods = new HashMap<>();
    Map<Method, List<Annotation>> paramsOfMethods = new HashMap<>();

    public Bean(Class<?> parsedClass) {
        Annotation[] classAnnotations = parsedClass.getAnnotations();
        for (Annotation annotation : classAnnotations) {
            this.classAnnotation.add(annotation);
            if (annotation.annotationType().equals(Controller.class)) {
                beanID = ((Controller) annotation).value();
            }
        }


        Method[] methods = parsedClass.getDeclaredMethods();

        for (Method method : methods) {
            Annotation[] methodAnnotations = method.getAnnotations();
            this.methods.put(method, new ArrayList<>());
            List<Annotation> annotations = this.methods.get(method);
            for (Annotation annotation : methodAnnotations){
                annotations.add(annotation);
            }
            List<Annotation> params = new ArrayList<>();

            Annotation[][] paramsAnnotation = method.getParameterAnnotations();
            for (Annotation[] ParamAnnotations : paramsAnnotation) {
                params.addAll(Arrays.asList(ParamAnnotations));
            }

            this.paramsOfMethods.put(method, params);
        }

    }


    public List<Method> getMethodsByAnnotation(Annotation annotation) {
        List<Method> list = new ArrayList<>();
        for (Map.Entry<Method, List<Annotation>> entry : this.methods.entrySet()) {
            if (entry.getValue().contains(annotation)) list.add(entry.getKey());
        }

        return list;
    }

    public List<Annotation> getParamsByMethod(Method method) {
        return this.paramsOfMethods.get(method);
    }


    public List<Annotation> getClassAnnotation() {
        return classAnnotation;
    }

}
