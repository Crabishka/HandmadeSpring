package ru.vsu.csf.server.dockerLogic;

import ru.vsu.csf.server.dockerLogic.annotation.Controller;
import ru.vsu.csf.server.dockerLogic.annotation.Repository;
import ru.vsu.csf.server.dockerLogic.annotation.Service;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.*;

/**
 * Factory is repository for Classes in any one package.
 * Factory get PackageURL to parse all classes in this URL.
 */
public class Factory {

    /**
     * if it's Controller name is value of annotation
     * else name is Class Name
     */
    Map<AnnotationBinder, Map<String, Bean>> storage = new HashMap<>();
    private final List<AnnotationBinder> AVAILABLE_CLASS_ANNOTATION = new ArrayList<>();


    public Factory(String packURL) {

        AVAILABLE_CLASS_ANNOTATION.add(new AnnotationBinder(Controller.class));
        AVAILABLE_CLASS_ANNOTATION.add(new AnnotationBinder(Service.class));
        AVAILABLE_CLASS_ANNOTATION.add(new AnnotationBinder(Repository.class));

        for (AnnotationBinder annotation : AVAILABLE_CLASS_ANNOTATION) {
            storage.put(annotation, new HashMap<>());
        }

//        File mainFile = new File(packURL);
//        List<Class<?>> classes = new ArrayList<>();
//        if (mainFile.listFiles() == null) return;
//        for (File file : mainFile.listFiles()) {
//            try {
//                classes.add(Class.forName(file.getName()));
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//        }

        List<Class<?>> classes = find(packURL);

        for (Class<?> clazz : classes) {
            Bean bean = new Bean(clazz);

            for (AnnotationBinder annotation : bean.getClassAnnotation()) {
                if (AVAILABLE_CLASS_ANNOTATION.contains(annotation)) {
                    String name = bean.beanID == null ? clazz.getCanonicalName() : bean.beanID;
                    storage.get(annotation).put(name, bean);
                    break;
                }
            }
        }
    }

    /**
     * just plain get from map method
     * use .getClass() to find by annotation
     *
     * @param annotation - in our case is {Controller, Service, Repository}
     * @param name       - name of Class
     * @return Bean (nullable)
     */
    public Bean getByAnnotationAndName(Class<? extends Annotation> annotation, String name) {
        return storage.get(new AnnotationBinder(annotation)).get(name);
    }


    private static final char PKG_SEPARATOR = '.';
    private static final char DIR_SEPARATOR = '/';
    private static final String CLASS_FILE_SUFFIX = ".class";

    /**
     * Возвращает список классов в пакете
     * @return
     */
    public static List find(String scannedPackage) {
        String scannedPath = scannedPackage.replace(PKG_SEPARATOR, DIR_SEPARATOR);
        URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource(scannedPath);
        if (scannedUrl == null) {
            return Collections.EMPTY_LIST;
        }
        File scannedDir = new File(scannedUrl.getFile());
        List<Class<?>> classes = new ArrayList<>();
        for (File file : scannedDir.listFiles()) {
            classes.addAll(find(file, scannedPackage));
        }
        return classes;
    }

    private static List<Class<?>> find(File file, String scannedPackage) {
        List<Class<?>> classes = new ArrayList<>();
        String resource = scannedPackage + PKG_SEPARATOR + file.getName();
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                classes.addAll(find(child, resource));
            }
        } else if (resource.endsWith(CLASS_FILE_SUFFIX)) {
            int endIndex = resource.length() - CLASS_FILE_SUFFIX.length();
            String className = resource.substring(0, endIndex);
            try {
                classes.add(Class.forName(className));
            } catch (ClassNotFoundException ignore) {
            }
        }
        return classes;
    }


}
