package Server.Http.Response;

import Server.Http.Request.HttpRequest;
import Server.Http.WrongHttpCreatingException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class HttpResponse {

    HttpResponseBuilder builder = new HttpResponseBuilderImpl();

    public static HttpResponseBuilderImpl newBuilder() throws WrongHttpCreatingException {
        return new HttpResponse().new HttpResponseBuilderImpl();
    }

    private String version;
    private String status;
    private List<String> headers = new LinkedList<>();
    private String body;  // пока только HTML возвращаем



    public void setVersion(String version) {
        this.version = version;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(version)
                .append(" ")
                .append(status)
                .append("\n");

        for (String pair : headers) {
            stringBuilder.append(pair).append("\n");
        }

        stringBuilder.append("\n");
        stringBuilder.append(body);

        return stringBuilder.toString();
    }

    public class HttpResponseBuilderImpl implements HttpResponseBuilder {

        private HttpResponseBuilderImpl() {

        }

        @Override
        public HttpResponseBuilder setVersion(String s) {
            HttpResponse.this.version = s;
            return this;
        }

        @Override
        public HttpResponseBuilder setStatus(String s) {
            HttpResponse.this.status = s;
            return this;
        }

        @Override
        public HttpResponseBuilder addHeader(String s) {
            HttpResponse.this.headers.add(s);
            return this;
        }

        @Override
        public HttpResponseBuilder addHeader(Collection<String> collection) {
            HttpResponse.this.headers.addAll(collection);
            return this;
        }

        @Override
        public HttpResponseBuilder setBody(String s) {
            HttpResponse.this.body = s;
            return this;
        }

        public HttpResponse build() {
            return HttpResponse.this;
        }
    }

}
