package Server.Http.Request;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    BufferedReader in;

    private RequestType requestType;
    private String path;
    private Map<String, String> params = new HashMap<>();
    private Map<String, String> headers = new HashMap<>();


    public HttpRequest(BufferedReader in) {
        this.in = in;
        try {
            parseInput(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void parseInput(BufferedReader in) throws IOException {

        /*
        Path - check
        RequestType - check
        params - check
        headers - check
         */

        StringBuilder stringBuilder = new StringBuilder();
        while (in.ready()) {
            stringBuilder.append(in.readLine()).append("\n");
        }
        String s = stringBuilder.toString();
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
        while (lines.length > i && !lines[i].equals("")) {
            String[] header = lines[i].split(":");
            headers.put(header[0], header[1]);
            i++;
        }


    }


}
