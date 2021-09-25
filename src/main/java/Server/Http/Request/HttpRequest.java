package Server.Http.Request;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Class HttpRequest is used for create safe HTTP Request instead of String Solution is Server class
 */

public class HttpRequest {

    HttpRequestBuilder builder = new HttpRequestBuilderImpl();

    /**
     * default type is GET
     */
    RequestType type = RequestType.GET;
    String path = "/";
    String parameters;
    String version;
    String host;
    List<String> headers = new LinkedList<>();
    String body;

    public class HttpRequestBuilderImpl implements HttpRequestBuilder {

        private HttpRequestBuilderImpl() {

        }

        @Override
        public void setRequestType(RequestType type) {
            HttpRequest.this.type = type;
        }

        @Override
        public void setPath(String s) {
            HttpRequest.this.path = s;
        }

        @Override
        public void setHost(String s) {
            HttpRequest.this.host = s;
        }

        @Override
        public void setParameters(String s) {
            HttpRequest.this.parameters = "$" + s;
        }

        @Override
        public void setVersion(String s) {
            HttpRequest.this.version = s;
        }

        @Override
        public void setHeader(String s) {
            HttpRequest.this.headers.add(s);
        }

        @Override
        public void setHeader(Collection<String> collection) {
            HttpRequest.this.headers.addAll(collection);
        }

        @Override
        public void setBody(String s) {
            if (type == RequestType.GET) return;
            else HttpRequest.this.body = s;
        }

        public HttpRequest build() {
            return HttpRequest.this;
        }

        public String HttpRequestToSend() {
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

    /**
     * ONLY WITH VALID STRING
     *
     * @param s is VALID string HTTP request that needs to be parsing
     */
    public HttpRequest(String s) throws WrongHttpCreatingException {
        String[] params = s.split("\n"); // split by line
        String[] firstString = params[0].split(" ");
        if (firstString[0].equals("GET")) {
            this.type = RequestType.GET;
        }

        String[] kostil = firstString[1].split("\\$"); // ты че мудак, назови нормально
        path = kostil[0];
        if (kostil.length > 1) parameters = kostil[1];

        version = firstString[2];

        int i = 1;
        while (params.length > i && !params[i].equals(" ")) {      // осторожно, я хз как сплититься пустая строчка
            headers.add(params[i]);
            i++;
        }

        for (; i < params.length; i++) {
            body += params[i];
        }


    }

}
