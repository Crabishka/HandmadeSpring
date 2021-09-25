package Response;

import Server.Http.Response.HttpResponse;
import Server.Http.WrongHttpCreatingException;
import junit.framework.TestCase;

public class HttpResponseTest extends TestCase {

    public void testHttpResponseCreate() throws WrongHttpCreatingException {
        HttpResponse httpResponse = HttpResponse.newBuilder()
                .setVersion("HTTP/1.0")
                .setStatus("200 OK")
                .addHeader("Content-Language: ru")
                .addHeader("Content-Type: text/html; charset=utf-8")
                .addHeader("Content-Length: 1234")
                .addHeader("Connection: close")
                .setBody("... САМА HTML-СТРАНИЦА ...")
                .build();
        System.out.println(httpResponse.toString());
    }
}
