package ru.vsu.csf.pryadchenko.spring;


import ru.vsu.csf.pryadchenko.spring.dockerLogic.Servlet;
import ru.vsu.csf.pryadchenko.spring.http.request.HttpRequest;
import ru.vsu.csf.pryadchenko.spring.http.response.HttpResponse;


import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Server extends Thread {


    BufferedReader input = null;
    OutputStream outputStream = null;
    Socket clientSocket;


    public Server(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
            outputStream = clientSocket.getOutputStream();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        start();
    }

    @Override
    public void run() {
        try {
            while (!input.ready()) ;
            HttpRequest httpRequest = new HttpRequest(input);
            String[] path = httpRequest.getPath().split("/");
            Servlet servlet = Application.getServlet(path[1]); // Is it good?
            if (servlet == null) return;
            servlet.doResponse(httpRequest, new HttpResponse(outputStream));

        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}