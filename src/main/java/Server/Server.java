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
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));


            File file = new File("src/main/resources/index.html");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }


            out.write("HTTP/1.0 200 OK \r\n" + "Content-type: text/html \r\n" + "\r\n" + stringBuilder.toString());


            out.close();
            clientSocket.close();
        }

    }
}
