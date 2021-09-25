package Server.Http.Request;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Class HttpRequest is used for create safe HTTP Request instead of String Solution is Server class
 */

public class HttpRequest {

    HttpRequestBuilder builder = new HttpRequestBuilderImpl();

    public static HttpRequestBuilderImpl newBuilder() throws WrongHttpCreatingException {
        return new HttpRequest().new HttpRequestBuilderImpl();
    }

    public RequestType getType() {
        return type;
    }

    public String getPath() {
        return path;
    }

    public String getParameters() {
        return parameters;
    }

    public String getVersion() {
        return version;
    }

    public String getHost() {
        return host;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    /**
     * default type is GET
     */
    private RequestType type;
    private String path = "/";
    private String parameters;
    private String version;
    private String host;
    private List<String> headers = new LinkedList<>();
    private String body;

    /**
     * ONLY WITH VALID STRING
     *
     * @param s is VALID string HTTP request that needs to be parsing
     */
    public HttpRequest(String s) throws WrongHttpCreatingException {
        String[] params = s.split("\n"); // split by line
        String[] requestLine = params[0].split(" ");
        if (requestLine[0].equals("GET")) {
            this.type = RequestType.GET;
        }

        String[] kostil = requestLine[1].split("\\$"); // ты че мудак, назови нормально
        path = kostil[0];
        if (kostil.length > 1) parameters = kostil[1];

        version = requestLine[2];

        int i = 1;
        while (params.length > i && !params[i].equals(" ")) {      // осторожно, я хз как сплититься пустая строчка
            headers.add(params[i]);
            i++;
        }

        for (; i < params.length; i++) {
            body += params[i];
        }


    }

    public HttpRequest() throws WrongHttpCreatingException{

    }

    public class HttpRequestBuilderImpl implements HttpRequestBuilder {

        private HttpRequestBuilderImpl() {

        }

        @Override
        public HttpRequestBuilderImpl setRequestType(RequestType type) {
            HttpRequest.this.type = type;
            return this;
        }

        @Override
        public HttpRequestBuilderImpl setPath(String s) {
            HttpRequest.this.path = s;
            return this;
        }

        @Override
        public HttpRequestBuilderImpl setHost(String s) {
            HttpRequest.this.host = s;
            return this;
        }

        @Override
        public HttpRequestBuilderImpl setParameters(String s) {
            HttpRequest.this.parameters = "$" + s;
            return this;
        }

        @Override
        public HttpRequestBuilderImpl setVersion(String s) {
            HttpRequest.this.version = s;
            return this;
        }

        @Override
        public HttpRequestBuilderImpl setHeader(String s) {
            HttpRequest.this.headers.add(s);
            return this;
        }

        @Override
        public HttpRequestBuilderImpl setHeader(Collection<String> collection) {
            HttpRequest.this.headers.addAll(collection);
            return this;
        }

        @Override
        public HttpRequestBuilderImpl setBody(String s) {
            if (type != RequestType.GET)  HttpRequest.this.body = s;
            return this;
        }

        public HttpRequest build() {
            return HttpRequest.this;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(type.toString())
                    .append(path)
                    .append(parameters)
                    .append(version)
                    .append("\n");

            for (String pairs : headers) {
                stringBuilder.append(pairs).append("\n");
            }

            stringBuilder.append("\n");

            if (type != RequestType.GET) stringBuilder.append(body);

            return stringBuilder.toString();

        }

    }



}
