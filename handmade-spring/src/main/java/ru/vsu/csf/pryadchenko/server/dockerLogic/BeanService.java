package ru.vsu.csf.pryadchenko.server.dockerLogic;


import ru.vsu.csf.pryadchenko.server.dockerLogic.annotation.Controller;
import ru.vsu.csf.pryadchenko.server.dockerLogic.annotation.GetMapping;
import ru.vsu.csf.pryadchenko.server.dockerLogic.annotation.Inject;
import ru.vsu.csf.pryadchenko.server.dockerLogic.annotation.PostMapping;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

class BeanService {

    static Object initialise(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                InvocationTargetException e) {
            throw new RuntimeException("Couldn't create instance of class", e);
        }
    }

    static void parseEndpoints(Class<?> clazz, Object instance, EndpointManager manager) {
        Controller annotation = clazz.getDeclaredAnnotation(Controller.class);
        String base = annotation.value();
        if (!base.equals("")) {
            base += "/";
        }
        for (Method method : clazz.getDeclaredMethods()) {
            GetMapping getMapping = method.getAnnotation(GetMapping.class);
            PostMapping postMapping = method.getAnnotation(PostMapping.class);
            if (getMapping != null) {
                manager.putGetPoint(base + getMapping.value(), new Endpoint(instance, method));
            } else if (postMapping != null) {
                manager.putPostPoint(base + postMapping.value(), new Endpoint(instance, method));
            }
        }
    }

    static void setFields(Class<?> clazz, Object instance, Map<Class<?>, Object> map) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getAnnotation(Inject.class) != null) {
                Object value = map.get(field.getType());
                field.setAccessible(true);
                try {
                    field.set(instance, value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Couldn't set injection fields",e);
                }
            }
        }
    }
}
