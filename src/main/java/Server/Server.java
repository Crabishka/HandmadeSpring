package Server;

import Server.Http.Request.HttpRequest;
import Server.Http.Response.HttpResponse;
import Server.Http.WrongHttpCreatingException;
import Server.ServerLogic.GetProperties;
import Server.ServerLogic.ServerService;

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

    private static InputStream in;

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(PORT);

        while (true) {

            Socket clientSocket = serverSocket.accept();

            try (BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
                 OutputStream outputStream = clientSocket.getOutputStream()
            ) {


                while (!input.ready()) ; // пустая строчка

                HttpRequest httpRequest = ServerService.getHttpRequest(input);
                HttpResponse httpResponse = ServerService.getHttpResponse(httpRequest);
                outputStream.write(httpResponse.toByteArray());


                clientSocket.close();
            } catch (WrongHttpCreatingException e) {
                e.printStackTrace();
            }

        }

    }
}