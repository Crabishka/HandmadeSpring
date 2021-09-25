package Server;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Server {

    private static final int PORT = 8080;
    private static InputStream in;

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(PORT);

        while (true) {

            Socket clientSocket = serverSocket.accept();

            try (BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
                 PrintWriter output = new PrintWriter(clientSocket.getOutputStream())) {

                System.out.println();
                while (input.ready()) {
                    System.out.println(input.readLine());
                }


                clientSocket.close();
            }

        }

    }
}