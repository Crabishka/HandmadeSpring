package Server;

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

            try (BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8))) {
            // TODO It's just test code
            // I have to delete this and create HTTP Request that will be parse in Server Logic Package
                System.out.println();
                while (input.ready()) {
                    System.out.println(input.readLine());
                }

                clientSocket.close();
            }

        }

    }
}