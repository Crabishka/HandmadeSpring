package ru.vsu.csf.pryadchenko.spring;

import ru.vsu.csf.pryadchenko.spring.dockerLogic.Servlet;
import ru.vsu.csf.pryadchenko.spring.logic.GetProperties;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class Application {

    private static int PORT;
    static int i = 0;
    static ConcurrentMap<String, Servlet> dispatchers = new ConcurrentHashMap<>();

    static {
        try {
            PORT = Integer.parseInt(GetProperties.getProperty("port"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        createServlet();
        drawTree();
        try (ServerSocket s = new ServerSocket(PORT)) {
            while (true) {
                Socket ClientSocket = s.accept();
                try {
                    i++;
                    System.out.println("Новое соединение установлено" + " " + i); // just for debug
                    new Server(ClientSocket);
                } catch (Exception e) {
                    ClientSocket.close();
                }
            }
        }
    }

    public synchronized static Servlet getServlet(String name) {
        return dispatchers.get(name);
    }

    public static List<File> getAllPackage() throws IOException {
        String pathToPackage = GetProperties.getProperty("servlet_docker").replace('.', '/');
        File file = new File(pathToPackage);
        return Arrays.asList(file.listFiles());
    }

    public static void createServlet() throws IOException {
        for (File file : getAllPackage()) {
            dispatchers.put(file.getName(), new Servlet(file.getPath().replace('\\', '.')
                    .replace("src.main.java.", ""))); // FIXME FIXME FIXME FIXME FIXME FIXME FIXME
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
