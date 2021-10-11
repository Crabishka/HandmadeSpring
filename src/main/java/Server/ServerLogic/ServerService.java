package Server.ServerLogic;

import Server.Http.Request.HttpRequest;
import Server.Http.Request.RequestType;
import Server.Http.WrongHttpCreatingException;
import Server.Http.Response.HttpResponse;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.util.Properties;

/**
 * this class hasn't any state
 * You use it to create HTTP Response using HTTP Request
 */
public class ServerService {

    FileInputStream fis;
    Properties property = new Properties();



    public static void sendResponse() {

    }

    public static HttpResponse getHttpResponse(HttpRequest httpRequest) throws WrongHttpCreatingException, IOException {
        RequestType type = httpRequest.getType();
        if (type == RequestType.GET) {

            String path = httpRequest.getPath();
            String filePath = GetProperties.getProperty("path") + path;

            HttpResponse.HttpResponseBuilder builder = HttpResponse.newBuilder();


            String extension = getExtension(path);

            if (extension.equals("html")) {
                builder.setVersion("HTTP/1.1").setStatus("200 OK")
                        .addHeader("Content-type: text/html")
                        .setBody(readTextFromFile(new File(filePath)));
            } else if (extension.equals("jpg")) {
                byte[] image = readAllBytes(new File(filePath));
                builder.setVersion("HTTP/1.1").setStatus("200 OK")
                        .addHeader("Content-type: image/jpeg")
                        .addHeader("Content-Length: " + image.length)
                        .setBody(image);
            } else if (extension.equals("ico")) {
                byte[] image = readAllBytes(new File(filePath));
                builder.setVersion("HTTP/1.1").setStatus("200 OK")
                        .addHeader("Content-type: image/jpeg")
                        .addHeader("Content-Length: " + image.length)
                        .setBody(image); // затычка
            } else if (extension.equals("txt")) {
                builder.setVersion("HTTP/1.1").setStatus("200 OK")
                        .addHeader("Content-type: text/html")
                        .setBody(readTextFromFile(new File(filePath)));
            }

            return builder.build();
        } else return null;

    }

    private static String getExtension(String path) {
        int index = path.indexOf("\\?");
        String parsedPath = path;
        String extension = "";
        if (index > 0) {
            parsedPath = path.substring(0, index);
        }

        int i = parsedPath.lastIndexOf('.');
        if (i > 0) {
            extension = parsedPath.substring(i + 1);
        }
        return extension;
    }

    public static HttpRequest getHttpRequest(BufferedReader input) throws IOException, WrongHttpCreatingException {
        StringBuilder stringBuilder = new StringBuilder();
        while (input.ready()) {
            stringBuilder.append(input.readLine()).append("\n");
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

    public static byte[] readAllBytes(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bytes = new byte[fileInputStream.available()];
        fileInputStream.read(bytes, 0, bytes.length);
        return bytes;
    }


}
