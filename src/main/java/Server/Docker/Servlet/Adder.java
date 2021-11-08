package Server.Docker.Servlet;

import Server.Docker.GetMapping;
import Server.Docker.RequestMapping;
import Server.Docker.Params;
import Server.Http.Request.HttpRequest;
import Server.Http.Response.HttpResponse;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@RequestMapping("/adder")
public class Adder{

    @GetMapping
    public void doGet(HttpRequest request, HttpResponse response) {
        int a = Integer.parseInt(request.getParam("a"));
        int b = Integer.parseInt(request.getParam("b"));
        String c = String.valueOf((a + b));
        response.setBody(c.getBytes(StandardCharsets.UTF_8));
        response.send();

    }
}
