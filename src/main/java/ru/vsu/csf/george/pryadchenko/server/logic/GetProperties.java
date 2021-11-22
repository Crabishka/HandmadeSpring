package ru.vsu.csf.george.pryadchenko.server.logic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class GetProperties {

    private static FileInputStream fis;

    static {
        try {
            fis = new FileInputStream("src/main/resources/config.properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static final Properties property = new Properties();


    public static String getProperty(String prop) throws IOException {

            property.load(fis);

            String res = property.getProperty(prop);
            return res;

    }
}
