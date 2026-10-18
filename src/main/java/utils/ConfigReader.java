package utils;

import java.io.InputStream;
import java.util.Properties;

/**
 * Reads config.properties. A -Dkey=value passed on the command line
 * (e.g. Jenkins: mvn test -Dheadless=true) overrides the file value.
 */
public class ConfigReader {

    private static final Properties props = new Properties();

    static {
        try (InputStream is = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (is == null) {
                throw new RuntimeException("config.properties not found on classpath");
            }
            props.load(is);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String get(String key) {
        String sys = System.getProperty(key);
        return (sys != null && !sys.isBlank()) ? sys : props.getProperty(key);
    }
}
