package ru.vsu.csf.george.pryadchenko.server.dockerLogic;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Factory is repository for Classes in any one package.
 * Factory get PackageURL to parse all classes in this URL.
 */
public class Factory {

    /**
     * if it's Controller name is value of annotation
     * else name is Class Name
     */
    Map<Class<? extends Annotation>, Map<String, Bean>> storage = new HashMap<>();
    private final List<Class<? extends Annotation>> AVAILABLE_CLASS_ANNOTATION = new ArrayList<>();



    public Factory(String packURL) {

        AVAILABLE_CLASS_ANNOTATION.add(Controller.class);
        AVAILABLE_CLASS_ANNOTATION.add(Service.class);
        AVAILABLE_CLASS_ANNOTATION.add(Repository.class);

        for (Class<? extends Annotation> annotation : AVAILABLE_CLASS_ANNOTATION){
            storage.put(annotation, new HashMap<>());
        }

        List<Class<?>> classes = getAllClassesFrom(packURL);
        for (Class<?> clazz : classes){
            Bean bean = new Bean(clazz);

            for (Class<? extends Annotation> annotation : bean.getClassAnnotation()){
                if (AVAILABLE_CLASS_ANNOTATION.contains(annotation)){
                    String name = bean.beanID == null ? clazz.getCanonicalName() : bean.beanID;
                    storage.get(annotation).put(name, bean);
                    break;
                }
            }
        }
    }



//    private static List<Class<?>> getAllClassesFrom(String packageName) { // FIXME pizdec
//        return new Reflections(packageName, new SubTypesScanner(true))
//                .getAllTypes()
//                .stream()
//                .map(name -> {
//                    try {
//                        return Class.forName(name);
//                    } catch (ClassNotFoundException e) {
//                        return null;
//                    }
//                })
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList());
//    }

    /**
     * just plain get from map method
     * use .getClass() to find by annotation
     *
     * @param annotation - in our case is {Controller, Service, Repository}
     * @param name       - name of Class
     * @return Bean (nullable)
     */
    public Bean getByAnnotationAndName(Class<? extends Annotation> annotation, String name) {
        return storage.get(annotation).get(name);
    }



}
