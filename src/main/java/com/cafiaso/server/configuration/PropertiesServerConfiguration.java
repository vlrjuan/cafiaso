package com.cafiaso.server.configuration;

import java.util.Properties;

/**
 * An implementation of {@link ServerConfiguration} that uses a provided {@link Properties} object.
 */
public class PropertiesServerConfiguration implements ServerConfiguration {

    private final Properties properties;

    public PropertiesServerConfiguration(Properties properties) {
        this.properties = properties;
    }

    @Override
    public int getMaxPlayers() {
        return Integer.parseInt(properties.getProperty("server.max-players", String.valueOf(DEFAULT_MAX_PLAYERS)));
    }

    @Override
    public String getDescription() {
        return properties.getProperty("server.description", DEFAULT_DESCRIPTION);
    }
}
