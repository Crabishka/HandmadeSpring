package ru.vsu.csf.george.pryadchenko.server.dockerLogic;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class Factory {
    Map<Class<? extends Annotation>, Map<String, Bean>> storage = new HashMap<>();

    /*
    if it's Controller name is value of annotation
    in others cases name is Class Name
     */
    public Bean getByAnnotationAndName(Class<? extends Annotation> annotation, String name) {
        return storage.get(annotation).get(name);
    }

    public Factory(){

    }
}
