package com.Petal;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final Properties PROPERTIES = new Properties();

    static {
        // Default to 'qa' if env is not specified
        String env = System.getProperty("env", "qa"); // Provide a default value
        String fileName = env + ".properties";

        try (InputStream inputStream = ConfigLoader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new FileNotFoundException("Properties file not found: " + fileName);
            }
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file: " + fileName, e);
        }
    }

    public static String getPetalsASEURL() {
        String url = PROPERTIES.getProperty("petal_baseUrl");
        if (url == null) {
            throw new IllegalStateException("petal_baseUrl not found in properties file.");
        }
        return url;
    }

    public static String getVitraiBASEURL() {
        String url = PROPERTIES.getProperty("vitrai_baseUrl");
        if (url == null) {
            throw new IllegalStateException("vitrai_baseUrl not found in properties file.");
        }
        return url;
    }
}