package Server.Http.Response;


import Server.ServerLogic.GetProperties;

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
    public OutputStream getOutput() {
        return output;
    }

    private OutputStream output;

    private String version;

    {
        try {
            version = GetProperties.getProperty("protocol_version");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String status = "200 OK";
    private Map<String, String> headers = new HashMap<>();
    private byte[] body;

    public String getHeader(Object key) {
        return headers.get(key);
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

    public void send() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(version).append(" ").append(status).append("\n");
        for (Map.Entry<String, String> header : headers.entrySet()) {
            stringBuilder.append(header.getKey()).append(":").append(header.getValue());
        }
        stringBuilder.append("\n");
        byte[] bytes = stringBuilder.toString().getBytes(StandardCharsets.UTF_8);
        byte[] res = Arrays.copyOf(bytes,bytes.length + body.length);
        System.arraycopy(body,0,res,bytes.length,body.length);
        try {
            output.write(res);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
