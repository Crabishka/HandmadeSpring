package ru.vsu.csf.pryadchenko.server;

import ru.vsu.csf.pryadchenko.server.dockerLogic.Servlet;
import ru.vsu.csf.pryadchenko.server.logic.AppProperties;
import ru.vsu.csf.pryadchenko.server.logic.Server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.jar.JarFile;

public class Application {

    private static final int PORT = Integer.parseInt(AppProperties.get("port"));
    static int i = 0;
    static ConcurrentMap<String, Servlet> dispatchers = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        createServlet();
        drawTree();
        try (ServerSocket s = new ServerSocket(PORT)) {
            while (true) {
                Socket ClientSocket = s.accept();
                try {
                    i++;
                    new Thread(new Server(ClientSocket)).start();
                } catch (Exception e) {
                    ClientSocket.close();
                }
            }
        }
    }

    public static Servlet getServlet(String name) {
        return dispatchers.get(name);
    }

    public static File[] getAllFiles() {
        return new File("docker").listFiles();
    }

    public static void createServlet() throws IOException {
        for (File file : getAllFiles()) {
            if (file.getName().endsWith(".jar")) {
                JarFile jarFile = new JarFile(file);
                dispatchers.put(jarFile.getName().substring(7, jarFile.getName().length() - 4), new Servlet(jarFile));
            }
        }
    }

    public static void drawTree() {
        System.out.println("      ccee88oo\n" +
                "  C8O8O8Q8PoOb o8oo\n" +
                " dOB69QO8PdUOpugoO9bD\n" +
                "CgggbU8OU qOp qOdoUOdcb\n" +
                "    6OuU  /p u gcoUodpP\n" +
                "      \\\\\\//  /douUP\n" +
                "        \\\\\\//\n" +
                "         ||||\\\n" +
                "         |||\\/\n" +
                "         |||||\n" +
                "   .....//||||\\....");
    }
}