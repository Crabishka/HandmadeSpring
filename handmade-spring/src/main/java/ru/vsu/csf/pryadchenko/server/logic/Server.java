package ru.vsu.csf.pryadchenko.server.logic;

import ru.vsu.csf.pryadchenko.server.Application;
import ru.vsu.csf.pryadchenko.server.dockerLogic.Servlet;
import ru.vsu.csf.pryadchenko.server.http.request.HttpRequest;
import ru.vsu.csf.pryadchenko.server.http.response.HttpResponse;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class Server implements Runnable {

    private final Socket clientSocket;

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
                StringBuilder body = new StringBuilder();
                int ch;
                for (; ; ) {
                    ch = input.read();
                    if (queue.size() >= 4) {
                        queue.remove(0);
                        queue.add(ch);
                        if (queue.get(0) == 13 && queue.get(1) == 10 && queue.get(2) == 13 && queue.get(3) == 10) {
                            body.append((char) ch);
                            break;
                        }
                    } else {
                        queue.add(ch);
                    }
                    stringBuilder.append((char) ch);
                }
                HttpRequest httpRequest = new HttpRequest(stringBuilder.toString());

                String str = httpRequest.getHeader("Content-Length");
                if (str != null) {
                    str = str.trim().replace("\r", "");
                    int contentLength = Integer.parseInt(str); // FIXME

                    for (int i = 0; i < contentLength; i++) {
                        char c = (char) input.read();
                        body.append(c);
                    }
                    if (body.length() > 1) httpRequest.setBody(body.toString().getBytes(StandardCharsets.UTF_8));
                }

                System.out.println("New connection established " + httpRequest.getPath() + " " + httpRequest.getParams().toString());
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