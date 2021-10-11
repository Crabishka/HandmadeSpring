package Server.Http.Response;

import Server.Http.WrongHttpCreatingException;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class HttpResponse {

    HttpResponseBuilder builder = new HttpResponseBuilder();

    public static HttpResponseBuilder newBuilder() throws WrongHttpCreatingException {
        return new HttpResponse().new HttpResponseBuilder();
    }

    private String version;

    public String getVersion() {
        return version;
    }

    public String getStatus() {
        return status;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

    private String status;
    private List<String> headers = new LinkedList<>();
    private byte[] body;



    public void setVersion(String version) {
        this.version = version;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void setBody(String body){
        this.body = body.getBytes(StandardCharsets.UTF_8);
    }


    public byte[] toByteArray() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(version)
                .append(" ")
                .append(status)
                .append("\n");

        for (String pair : headers) {
            stringBuilder.append(pair).append("\n");
        }

        stringBuilder.append("\n");
        byte[] bytes = stringBuilder.toString().getBytes(StandardCharsets.UTF_8);
        byte[] res = Arrays.copyOf(bytes,bytes.length + body.length);
        System.arraycopy(body,0,res,bytes.length,body.length);
        return res;
    }



    public class HttpResponseBuilder {

        private HttpResponseBuilder() {

        }


        public HttpResponseBuilder setVersion(String s) {
            HttpResponse.this.version = s;
            return this;
        }

        public HttpResponseBuilder setStatus(String s) {
            HttpResponse.this.status = s;
            return this;
        }

        public HttpResponseBuilder addHeader(String s) {
            HttpResponse.this.headers.add(s);
            return this;
        }


        public HttpResponseBuilder addHeader(Collection<String> collection) {
            HttpResponse.this.headers.addAll(collection);
            return this;
        }

        public HttpResponseBuilder setBody(byte[] s) {
            HttpResponse.this.body = s;
            return this;
        }

        public HttpResponseBuilder setBody(String s) {
            HttpResponse.this.body = s.getBytes(StandardCharsets.UTF_8);
            return this;
        }


        public HttpResponse build() {
            return HttpResponse.this;
        }
    }

}
