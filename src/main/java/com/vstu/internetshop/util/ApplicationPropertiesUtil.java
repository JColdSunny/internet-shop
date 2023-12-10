package com.vstu.internetshop.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Утилита для работы с файлом пропертей "application.properties".
 * Класс является финальным и имеет приватный конструктор для предотвращения его инстанциации.
 */
public final class ApplicationPropertiesUtil {

    /**
     * Все проперти приложения. Загружаются один раз при инициализации класса.
     */
    private static final Properties APPLICATION_PROPERTIES = new Properties();

    // Загружаем проперти при инициализации класса.
    static {
        loadProperties();
    }

    private ApplicationPropertiesUtil() {
        throw new UnsupportedOperationException("This is an utility class");
    }

    /**
     * Получить значение по ключу из файла "application.properties".
     * @param key ключ для поиска в файле пропертей
     * @return значение по ключу или null, если такого ключа не существует
     */
    public static String get(String key) {
        return APPLICATION_PROPERTIES.getProperty(key);
    }

    /**
     * Загружает все проперти из файла "application.properties".
     * Если загрузка не удалась, выбрасывает исключение "RuntimeException".
     */
    private static void loadProperties() {
        try (InputStream inputStream = ApplicationPropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            APPLICATION_PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load application properties", e);
        }
    }

}