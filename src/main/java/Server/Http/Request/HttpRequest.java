package Server.Http.Request;

import Server.Http.WrongHttpCreatingException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Class HttpRequest is used for create safe HTTP Request instead of String Solution in Server class
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

    private RequestType type;
    private String path = "/";
    private String parameters = "";
    private String version;
    private String host;
    private List<String> headers = new LinkedList<>();
    private String body = "";

    /**
     * ONLY WITH VALID STRING
     *
     * @param s is VALID string HTTP request that needs to be parsing
     */
    public HttpRequest(String s) throws WrongHttpCreatingException {
        String[] params = s.split("\n"); // split by line
        String[] requestLine = params[0].split(" ");
        if (requestLine[0].equals("GET")) {
            builder.setRequestType(RequestType.GET);
        } else if (requestLine[0].equals("POST")) {
            builder.setRequestType(RequestType.POST);
        }

        /**
         * need to be careful 'cause link should not be empty. "\\" at least
         */
        String[] link = requestLine[1].split("\\?");
        builder.setPath(link[0]);
        if (link.length > 1) builder.setParameters(link[1]);

        builder.setVersion(requestLine[2]);

        int i = 1;
        while (params.length > i && !params[i].equals("")) {      // осторожно, я хз как сплититься пустая строчка
            builder.addHeader(params[i]);
            i++;
        }


        if (type == RequestType.GET) return;
        StringBuilder bodyBuilder = new StringBuilder();
        for (i++; i < params.length; i++) {
            bodyBuilder.append(params[i]).append("\n");
        }
        builder.setBody(bodyBuilder.toString());


    }

    public HttpRequest() throws WrongHttpCreatingException {

    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(type.toString())
                .append(" ")
                .append(path)
                .append(parameters)
                .append(" ")
                .append(version)
                .append("\n");

        for (String pairs : headers) {
            stringBuilder.append(pairs).append("\n");
        }

        stringBuilder.append("\n");

        if (type != RequestType.GET) stringBuilder.append(body);

        return stringBuilder.toString();

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
            HttpRequest.this.parameters = "?" + s;
            return this;
        }

        @Override
        public HttpRequestBuilderImpl setVersion(String s) {
            HttpRequest.this.version = s;
            return this;
        }

        @Override
        public HttpRequestBuilderImpl addHeader(String s) {
            HttpRequest.this.headers.add(s);
            return this;
        }

        @Override
        public HttpRequestBuilderImpl addHeader(Collection<String> collection) {
            HttpRequest.this.headers.addAll(collection);
            return this;
        }

        @Override
        public HttpRequestBuilderImpl setBody(String s) {
            if (type != RequestType.GET) HttpRequest.this.body = s;
            return this;
        }

        public HttpRequest build() {
            return HttpRequest.this;
        }


    }


}
