package ru.vsu.csf.pryadchenko.server;


import ru.vsu.csf.pryadchenko.server.dockerLogic.Servlet;
import ru.vsu.csf.pryadchenko.server.http.request.HttpRequest;
import ru.vsu.csf.pryadchenko.server.http.response.HttpResponse;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Server implements Runnable {



    Socket clientSocket;


    public Server(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            BufferedReader input = null;
            OutputStream outputStream = null;
            try {
                input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
                outputStream = clientSocket.getOutputStream();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            StringBuilder stringBuilder = new StringBuilder();
            while (!input.ready()) {
            }
            while (input.ready()){
                String str = input.readLine();
                System.out.println(str);
                stringBuilder.append(str);
                if (str.equals("Accept-Language: ru,en;q=0.9,pl;q=0.8")) break;
            }
            System.out.println("вышел");

            HttpRequest httpRequest = new HttpRequest(stringBuilder.toString());
            System.out.println("Новое соединение установлено" + " " + httpRequest.getPath() + " " + httpRequest.getParams().toString());
            String[] path = httpRequest.getPath().split("/");
            Servlet servlet = Application.getServlet(path[1]);
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