package org.cafiaso.server.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Implementation of {@link ServerConfiguration} that reads the configuration from a properties file.
 */
public class PropertiesServerConfiguration implements ServerConfiguration {

    /**
     * The name of the configuration file.
     */
    private static final String CONFIGURATION_FILE = "server.properties";

    /**
     * The properties loaded from the configuration file.
     */
    private final Properties properties;

    /**
     * The class loader to use to load the configuration file.
     */
    private final ClassLoader classLoader;

    /**
     * PropertiesServerConfiguration constructor.
     *
     * @param classLoader the class loader to use to load the configuration file
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
    public int getMaximumPlayers() {
        return Integer.parseInt(properties.getProperty("maximum-players", String.valueOf(DEFAULT_MAXIMUM_PLAYERS)));
    }

    @Override
    public String getDescription() {
        return properties.getProperty("description", DEFAULT_DESCRIPTION);
    }
}
