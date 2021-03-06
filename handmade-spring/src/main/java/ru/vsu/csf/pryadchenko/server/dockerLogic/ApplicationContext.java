package ru.vsu.csf.pryadchenko.server.dockerLogic;

import ru.vsu.csf.pryadchenko.server.dockerLogic.annotation.Controller;
import ru.vsu.csf.pryadchenko.server.dockerLogic.annotation.Repository;
import ru.vsu.csf.pryadchenko.server.dockerLogic.annotation.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

class ApplicationContext {

    private final EndpointManager endpointManager = new EndpointManager();

    ApplicationContext(JarFile jar) {
        Collection<Class<?>> classes = getAllClasses(jar);
        Map<Class<?>, Object> classToInstanceMap = new HashMap<>();
        for (Class<?> clazz : classes) {
            if (clazz.getAnnotation(Controller.class) != null) {
                Object instance = BeanService.initialise(clazz);
                classToInstanceMap.put(clazz, instance);
                BeanService.parseEndpoints(clazz, instance, endpointManager);
            } else if (clazz.getAnnotation(Service.class) != null || clazz.getAnnotation(Repository.class) != null) {
                classToInstanceMap.put(clazz, BeanService.initialise(clazz));
            }
        }
        for (Map.Entry<Class<?>, Object> entry : classToInstanceMap.entrySet()) {
            BeanService.setFields(entry.getKey(), entry.getValue(), classToInstanceMap);
        }
    }

    private static Collection<Class<?>> getAllClasses(JarFile jar) {
        Collection<Class<?>> classes = new ArrayList<>();
        URLClassLoader loader;
        try {
            loader = new URLClassLoader(new URL[]{new URL("jar:file:" + jar.getName() + "!/")});
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        String jarName = Paths.get(jar.getName()).getFileName().toString();
        jarName = jarName.substring(0, jarName.length() - 4);
        File destDir = new File("resources/" + jarName);
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
            } else if (file.endsWith(".class")) {
                String classname = file.replace('/', '.').substring(0, file.length() - 6);
                try {
                    Class<?> clas = loader.loadClass(classname);
                    ResourceManager.put(clas, destDir.getPath());
                    classes.add(clas);
                } catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException("Failed to instantiate " + classname + " from " + file, e);
                }
            }
        }

        return classes;
    }

    private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName().substring(7));
        if (zipEntry.isDirectory()) {
            destFile.mkdirs();
        }
        return destFile;
    }

    EndpointManager getEndpointManager() {
        return endpointManager;
    }
}