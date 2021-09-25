import Server.Http.Request.HttpRequest;
import Server.Http.Response.HttpResponse;
import Server.Http.WrongHttpCreatingException;
import Server.Server;
import Server.ServerLogic.ServerService;
import junit.framework.TestCase;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Logic extends TestCase {

    public void testServerService() throws WrongHttpCreatingException, IOException {
        ServerSocket serverSocket = new ServerSocket(8080);

        while (true) {

            Socket clientSocket = serverSocket.accept();

            try (BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
                 PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), true)
            ) {

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
