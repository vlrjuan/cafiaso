package org.cafiaso.server.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * {@link ServerConfiguration} implementation that reads the configuration from a properties file.
 */
public class PropertiesServerConfiguration implements ServerConfiguration {

    private static final String CONFIGURATION_FILE = "server.properties";

    private final Properties properties;

    private final ClassLoader classLoader;

    /**
     * PropertiesServerConfiguration constructor.
     *
     * @param classLoader the class loader to use to load the configuration file (mainly for testing purposes)
     */
    public PropertiesServerConfiguration(ClassLoader classLoader) {
        this.properties = new Properties();
        this.classLoader = classLoader;
    }

    @Override
    public void load() {
        try (InputStream input = classLoader.getResourceAsStream(CONFIGURATION_FILE)) {
            if (input == null) {
                throw new IOException("File not found");
            }

            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load server configuration", e);
        }
    }

    @Override
    public int getMaxPlayers() {
        return Integer.parseInt(properties.getProperty("max-players", String.valueOf(DEFAULT_MAX_PLAYERS)));
    }

    @Override
    public String getDescription() {
        return properties.getProperty("description", DEFAULT_DESCRIPTION);
    }
}
