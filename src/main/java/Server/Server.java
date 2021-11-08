package Server;

import Server.Docker.DispatcherServlet;
import Server.Http.Request.HttpRequest;
import Server.ServerLogic.GetProperties;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Server implements Runnable {

    private static int PORT;

    static {
        try {
            PORT = Integer.parseInt(GetProperties.getProperty("port"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            Socket clientSocket = serverSocket.accept();
            try (BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
                 OutputStream outputStream = clientSocket.getOutputStream()
            ) {
                while (!input.ready());
                HttpRequest httpRequest = new HttpRequest(input);
                new DispatcherServlet().doResponse(httpRequest,outputStream);


            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}