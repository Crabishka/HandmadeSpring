package ru.vsu.csf.pryadchenko.server.logic;

import java.io.*;
import java.util.Base64;

/**
 * this class hasn't any state
 * You use it to create HTTP Response using HTTP Request
 */
public class ServerService {

   public static String getExtension(String path) {
        int index = path.indexOf("\\?");
        String parsedPath = path;
        String extension = "";
        if (index > 0) {
            parsedPath = path.substring(0, index);
        }

        int i = parsedPath.lastIndexOf('.');
        if (i > 0) {
            extension = parsedPath.substring(i + 1);
        }
        return extension;
    }

    public static String readTextFromFile(File file) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        fileReader.close();
        bufferedReader.close();
        return stringBuilder.toString();
    }

    public static String readAllBytes(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bytes = new byte[fileInputStream.available()];
        fileInputStream.read(bytes, 0, bytes.length);
        return  Base64.getEncoder().encodeToString(bytes);
    }

}
