package Server.Http.Response;

import java.util.LinkedList;
import java.util.List;

public class HttpResponse {
    String version;
    String status;
    List<String> headers = new LinkedList<>();
    Object body;
}
