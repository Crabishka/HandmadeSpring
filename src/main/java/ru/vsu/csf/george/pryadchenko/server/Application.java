package ru.vsu.csf.george.pryadchenko.server;

import ru.vsu.csf.george.pryadchenko.server.docker.Servlet;
import ru.vsu.csf.george.pryadchenko.server.logic.GetProperties;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class Application {

    private static int PORT;
    static int i = 0;
    ConcurrentMap<String, Servlet> dispatchers = new ConcurrentHashMap<>();

    static {
        try {
            PORT = Integer.parseInt(GetProperties.getProperty("port"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        ServerSocket s = new ServerSocket(PORT);
        try {
            while (true) {
                Socket ClientSocket = s.accept();
                try {
                    i++;
                    System.out.println("Новое соединение установлено" + " " +  i); // just for debug
                    new Server(ClientSocket);
                } catch (Exception e) {
                    ClientSocket.close();
                }
            }
        } finally {
            s.close();
        }
    }

}
