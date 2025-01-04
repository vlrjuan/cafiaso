package org.cafiaso.server.configuration;

import org.cafiaso.server.Main;

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
     * PropertiesServerConfiguration constructor.
     */
    public PropertiesServerConfiguration() {
        properties = new Properties();
    }

    @Override
    public void load() {
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream(CONFIGURATION_FILE)) {
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
        return Integer.parseInt(properties.getProperty("maximum-players", "20"));
    }

    @Override
    public String getDescription() {
        return properties.getProperty("description", "A Minecraft Server");
    }
}
