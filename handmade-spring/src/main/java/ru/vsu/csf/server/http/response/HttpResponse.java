<<<<<<< HEAD:handmade-spring/src/main/java/ru/vsu/csf/pryadchenko/spring/http/response/HttpResponse.java
package ru.vsu.csf.pryadchenko.spring.http.response;


import ru.vsu.csf.pryadchenko.spring.logic.GetProperties;
=======
package ru.vsu.csf.server.http.response;


import ru.vsu.csf.server.logic.GetProperties;
>>>>>>> BROKEN:handmade-spring/src/main/java/ru/vsu/csf/server/http/response/HttpResponse.java

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/*
HttpResponse can send response by itself
 */
public class HttpResponse {

    private final OutputStream output;

    private String version;

    {
        try {
            version = GetProperties.getProperty("protocol_version");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    private String status = "200 OK";
    private final Map<String, String> headers = new HashMap<>();
    private byte[] body;

    public String getHeader(String key) {
        return  headers.get(key);
    }

    public String putHeader(String key, String value) {
        return headers.put(key, value);
    }

    public HttpResponse(OutputStream outputStream) {
        this.output = outputStream;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void send() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(version).append(" ").append(status).append("\n");
        for (Map.Entry<String, String> header : headers.entrySet()) {
            stringBuilder.append(header.getKey()).append(": ").append(header.getValue()).append("\n");
        }
        stringBuilder.append("\n");
        byte[] bytes = stringBuilder.toString().getBytes(StandardCharsets.UTF_8);
        byte[] res = Arrays.copyOf(bytes, bytes.length + body.length);
        System.arraycopy(body, 0, res, bytes.length, body.length);
        output.write(res);

    }


}
