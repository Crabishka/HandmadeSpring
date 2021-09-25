package Server.ServerLogic;

import Server.Http.Request.HttpRequest;
import Server.Http.Request.WrongHttpCreatingException;
import Server.Http.Response.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * this class hasn't any state
 * You use it to create HTTP Response using HTTP Request
 */
public class ServerService {

    public static HttpResponse getHttpResponse(HttpRequest httpRequest){
        return null;
    }

    public static HttpRequest getHttpRequest(BufferedReader input) throws IOException, WrongHttpCreatingException {
        StringBuilder stringBuilder = new StringBuilder();
        while (input.ready()) {
            stringBuilder.append(input.readLine());
        }
        return new HttpRequest(stringBuilder.toString());
    }


}
