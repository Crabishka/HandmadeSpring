package Server;

import Server.Http.Request.HttpRequest;
import Server.Http.Response.HttpResponse;
import Server.Http.WrongHttpCreatingException;
import Server.ServerLogic.ServerService;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Server {

    private static final int PORT = 8080;
    private static InputStream in;

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(PORT);

        while (true) {

            Socket clientSocket = serverSocket.accept();

            try (BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
                 PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), true)
            ) {



                while (!input.ready()); // Спасибо за пустую строчку

                HttpRequest httpRequest = ServerService.getHttpRequest(input);
                HttpResponse httpResponse = ServerService.getHttpResponse(httpRequest);
                out.println(httpResponse.toString());


                clientSocket.close();
            } catch (WrongHttpCreatingException e) {
                e.printStackTrace();
            }

        }

    }
}