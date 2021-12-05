package ru.vsu.csf.pryadchenko.server.logic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GetProperties {

    private static final InputStream is = GetProperties.class.getResourceAsStream("/config.properties");

    private static final Properties property = new Properties();

    public static String getProperty(String prop) throws IOException {
        property.load(is);
        return property.getProperty(prop);
    }
}
