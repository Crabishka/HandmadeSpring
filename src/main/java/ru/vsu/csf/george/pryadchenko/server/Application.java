package ru.vsu.csf.george.pryadchenko.server;

import ru.vsu.csf.george.pryadchenko.server.dockerLogic.Servlet;
import ru.vsu.csf.george.pryadchenko.server.logic.GetProperties;

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
        ServerSocket s = new ServerSocket(PORT);
        try {
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
        } finally {
            s.close();
        }
    }


    public static List<File> getAllPackage() throws IOException {
        String pathToPackage = GetProperties.getProperty("servlet_docker").replace('.', '/');
        File file = new File(pathToPackage);
        return Arrays.asList(file.listFiles());
    }

    public static void createServlet() throws IOException {
        for (File file : getAllPackage()) {
            dispatchers.put(file.getName(), new Servlet(file.getPath().replace('\\', '.')));
        }
    }

}
