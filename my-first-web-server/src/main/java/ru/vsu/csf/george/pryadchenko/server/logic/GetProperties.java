package ru.vsu.csf.george.pryadchenko.server.logic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GetProperties {

    private static final InputStream is;

    static {
        is = GetProperties.class.getResourceAsStream("/config.properties");
    }

    private static final Properties property = new Properties();


    public static String getProperty(String prop) throws IOException {

            property.load(is);

            String res = property.getProperty(prop);
            return res;

    }
}
