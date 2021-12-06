package ru.vsu.csf.pryadchenko.server.dockerLogic;

import ru.vsu.csf.pryadchenko.server.Application;
import ru.vsu.csf.pryadchenko.server.dockerLogic.annotation.Controller;
import ru.vsu.csf.pryadchenko.server.dockerLogic.annotation.Repository;
import ru.vsu.csf.pryadchenko.server.dockerLogic.annotation.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

/**
 * Factory is repository for Classes in any one package.
 * Factory get PackageURL to parse all classes in this URL.
 */
public class Factory {

    /**
     * if it's Controller name is value of annotation
     * else name is Class Name
     */
    private final static List<AnnotationBinder> AVAILABLE_CLASS_ANNOTATION = new ArrayList<>();

    static {
        AVAILABLE_CLASS_ANNOTATION.add(new AnnotationBinder(Controller.class));
        AVAILABLE_CLASS_ANNOTATION.add(new AnnotationBinder(Service.class));
        AVAILABLE_CLASS_ANNOTATION.add(new AnnotationBinder(Repository.class));
    }

    private final Map<AnnotationBinder, Map<String, Bean>> storage = new HashMap<>();

    public Factory(JarFile jar) {
        for (AnnotationBinder annotation : AVAILABLE_CLASS_ANNOTATION) {
            storage.put(annotation, new HashMap<>());
        }

        Collection<Class<?>> classes = getAllClasses(jar);

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
     * Возвращает классы из jar
     */
    public static Collection<Class<?>> getAllClasses(JarFile jar) {
        Collection<Class<?>> classes = new ArrayList<>();
        URLClassLoader loader;
        try {
            loader = new URLClassLoader(new URL[]{new URL("jar:file:" + jar.getName() + "!/")});
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        File destDir = new File(Application.RESOURCE_PATH);
        byte[] buffer = new byte[1024];
        for (Enumeration<JarEntry> entries = jar.entries(); entries.hasMoreElements(); ) {
            JarEntry entry = entries.nextElement();
            String file = entry.getName();
            if (file.startsWith("static/")) {
                try {
                    File newFile = newFile(destDir, entry);
                    if (!entry.isDirectory()) {
                        FileOutputStream fos = new FileOutputStream(newFile);
                        InputStream is = jar.getInputStream(entry);
                        int len;
                        while ((len = is.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                        is.close();
                        fos.close();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (file.endsWith(CLASS_FILE_SUFFIX)) {
                String classname = file.replace(DIR_SEPARATOR, PKG_SEPARATOR)
                        .substring(0, file.length() - CLASS_FILE_SUFFIX.length());
                try {
                    classes.add(loader.loadClass(classname));
                } catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException("Failed to instantiate " + classname + " from " + file, e);
                }
            }
        }

        return classes;
    }

    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());
        if (zipEntry.isDirectory()) {
            destFile.mkdirs();
        } else {
            destFile.createNewFile();
        }
        return destFile;
    }
}