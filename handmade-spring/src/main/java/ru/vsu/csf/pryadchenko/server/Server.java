package ru.vsu.csf.pryadchenko.server;


import ru.vsu.csf.pryadchenko.server.dockerLogic.Servlet;
import ru.vsu.csf.pryadchenko.server.http.request.HttpRequest;
import ru.vsu.csf.pryadchenko.server.http.response.HttpResponse;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class Server implements Runnable {


    Socket clientSocket;


    public Server(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            try (InputStream input = clientSocket.getInputStream();
                 OutputStream outputStream = clientSocket.getOutputStream()) {
                List<Integer> queue = new LinkedList<>();
                StringBuilder stringBuilder = new StringBuilder();
                for (int ch; (ch = input.read()) != -1; ) {

                    if (queue.size() >= 4) {
                        if (queue.get(0) == 13 && queue.get(1) == 10 && queue.get(2) == 13 && queue.get(3) == 10) break;
                        queue.remove(0);

                    }
                    queue.add(ch);
                    stringBuilder.append((char) ch);
                }

                System.out.println(stringBuilder);
                System.out.println("вышел");

                HttpRequest httpRequest = new HttpRequest(stringBuilder.toString());
                System.out.println("Новое соединение установлено" + " " + httpRequest.getPath() + " " + httpRequest.getParams().toString());
                String[] path = httpRequest.getPath().split("/");
                Servlet servlet = Application.getServlet(path[1]);
                servlet.doResponse(httpRequest, new HttpResponse(outputStream));
            }


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