package Server.ServerLogic;

import Server.Http.Request.HttpRequest;
import Server.Http.Request.RequestType;
import Server.Http.WrongHttpCreatingException;
import Server.Http.Response.HttpResponse;

import java.io.*;

/**
 * this class hasn't any state
 * You use it to create HTTP Response using HTTP Request
 */
public class ServerService {

    public static HttpResponse getHttpResponse(HttpRequest httpRequest) throws WrongHttpCreatingException, IOException {
        RequestType type = httpRequest.getType();
        if (type == RequestType.GET) {
            String path = httpRequest.getPath();
            String filePath = "src/main/resources" + path;
            HttpResponse httpResponse = HttpResponse.newBuilder().setVersion("HTTP/1.0")
                    .setStatus("200 OK")
                    .addHeader("Content-type: text/html")
                    .setBody(readTextFromFile(new File(filePath)))
                    .build();
            return httpResponse;
        } else return null;

    }

    public static HttpRequest getHttpRequest(BufferedReader input) throws IOException, WrongHttpCreatingException {
        StringBuilder stringBuilder = new StringBuilder();
        while (input.ready()) {
            stringBuilder.append(input.readLine());
        }
        return new HttpRequest(stringBuilder.toString());
    }

    private static String readTextFromFile(File file) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        fileReader.close();
        bufferedReader.close();
        return stringBuilder.toString();
    }


}
