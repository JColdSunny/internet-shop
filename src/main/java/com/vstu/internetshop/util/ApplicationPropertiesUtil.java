package com.vstu.internetshop.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ApplicationPropertiesUtil {
    private static final Properties APPLICATION_PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    private ApplicationPropertiesUtil() {
        throw new UnsupportedOperationException("This is an utility class");
    }

    public static String get(String key) {
        return APPLICATION_PROPERTIES.getProperty(key);
    }

    private static void loadProperties() {
        try (InputStream inputStream = ApplicationPropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            APPLICATION_PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load application properties", e);
        }
    }

}
