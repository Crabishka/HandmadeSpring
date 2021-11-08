package Server;

import Server.Docker.DispatcherServlet;
import Server.Http.Request.HttpRequest;
import Server.ServerLogic.GetProperties;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Server {

    private static int PORT;

    static {
        try {
            PORT = Integer.parseInt(GetProperties.getProperty("port"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(PORT);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            try (BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
                 OutputStream outputStream = clientSocket.getOutputStream()) {
                while (!input.ready());
                HttpRequest httpRequest = new HttpRequest(input);
                DispatcherServlet.getInstance().doResponse(httpRequest, outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}