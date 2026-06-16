package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class TestConfig {

    private static final Properties properties = new Properties();

    static {
        loadProperties();
    }

    private TestConfig() {
        // Utility class
    }

    private static void loadProperties() {
        try (InputStream input = TestConfig.class
                .getClassLoader()
                .getResourceAsStream("test.properties")) {

            if (input == null) {
                throw new IllegalStateException(
                        "Could not find test.properties in src/test/resources"
                );
            }

            properties.load(input);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Could not load test.properties",
                    e
            );
        }
    }

    public static String get(String key) {
        String value = properties.getProperty(key);

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Missing value in test.properties for key: " + key
            );
        }

        return value.trim();
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }
}