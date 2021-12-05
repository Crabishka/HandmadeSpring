<<<<<<< HEAD:handmade-spring/src/main/java/ru/vsu/csf/pryadchenko/spring/logic/GetProperties.java
package ru.vsu.csf.pryadchenko.spring.logic;
=======
package ru.vsu.csf.server.logic;
>>>>>>> BROKEN:handmade-spring/src/main/java/ru/vsu/csf/server/logic/GetProperties.java

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
