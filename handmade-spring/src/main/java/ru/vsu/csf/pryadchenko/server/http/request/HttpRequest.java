package ru.vsu.csf.pryadchenko.server.http.request;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private RequestType requestType;
    private String path;
    private final Map<String, String> params = new HashMap<>();
    private final Map<String, String> headers = new HashMap<>();


    public void setBody(byte[] body) {
        this.body = body;
    }

    private byte[] body;


    public HttpRequest(String s) {
        try {
            parseInput(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getBody() {
        return body;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getParam(String key) {
        return params.get(key);  // return null if there's not such key
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    private void parseInput(String s) throws IOException {

        String[] lines = s.split("\n");
        String[] requestLine = lines[0].split(" ");

        switch (requestLine[0]) {
            case "GET":
                requestType = RequestType.GET;
                break;
            case "POST":
                requestType = RequestType.POST;
                break;
            default:
                requestType = RequestType.GET;
                break;
        }

        String[] link = requestLine[1].split("\\?");
        this.path = link[0];
        if (link.length > 1) {
            String[] params = link[1].split("&");
            for (String pair : params) {
                String[] splintedPair = pair.split("=");
                this.params.put(splintedPair[0], splintedPair[1]);
            }
        }

        int i = 1;
        while (lines.length > i && !lines[i].equals("") && !lines[i].equals("\r")) {
            String[] header = lines[i].split(":");
            headers.put(header[0], header[1]);
            i++;
        }


        StringBuilder bodyBuilder = new StringBuilder();
        while (lines.length > i) {
            bodyBuilder.append(lines[i]).append("\n");
            i++;
        }

        this.body = bodyBuilder.toString().getBytes(StandardCharsets.UTF_8);


    }


}
