package Server.Http.Request;

import junit.framework.TestCase;
import org.junit.Test;

public class HttpRequestTest extends TestCase {

    public void testCreateHttpRequest() throws WrongHttpCreatingException {
        String s = "GET /resources HTTP/1.1\n" +
                "Host: 127.0.0.1:8080\n" +
                "Connection: keep-alive";
        HttpRequest httpRequest = new HttpRequest(s);
        System.out.println(httpRequest.builder.HttpRequestToSend());
    }

    public void testCreateHttpRequestWithBody() throws WrongHttpCreatingException{
        String s = "GET /resources HTTP/1.1\n" +
                "Host: 127.0.0.1:8080\n" +
                "Connection: keep-alive\n" +
                "\n" +
                "Cache-Control: max-age=0\n" +
                "sec-ch-ua: Chromium";
        HttpRequest httpRequest = new HttpRequest(s);
        System.out.println(httpRequest.builder.HttpRequestToSend());

    }

}