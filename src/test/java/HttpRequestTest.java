import Server.Http.Request.HttpRequest;
import Server.Http.Request.RequestType;
import Server.Http.WrongHttpCreatingException;
import junit.framework.TestCase;

public class HttpRequestTest extends TestCase {

    public void testCreateHttpRequest() throws WrongHttpCreatingException {
        String s = "GET / HTTP/1.1\n" +
                "Host: 127.0.0.1:8080\n" +
                "Connection: keep-alive";
        HttpRequest httpRequest = new HttpRequest(s);
        System.out.println(httpRequest.toString());
    }

    public void testCreateHttpRequestWithBody() throws WrongHttpCreatingException {
        String s = "GET /resources HTTP/1.1\n" +
                "Host: 127.0.0.1:8080\n" +
                "Connection: keep-alive\n" +
                "\n" +
                "Cache-Control: max-age=0\n" +
                "sec-ch-ua: Chromium";
        HttpRequest httpRequest = new HttpRequest(s);
        System.out.println(httpRequest.toString());

    }

    public void testCreateHttpUsingBuilder() throws WrongHttpCreatingException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .setRequestType(RequestType.POST)
                .setPath("/resources")
                .setVersion("HTTP/1.1")
                .addHeader("Connection: keep-alive")
                .setBody("HA-HA")
                .build();
        System.out.println(httpRequest.toString());

    }

}