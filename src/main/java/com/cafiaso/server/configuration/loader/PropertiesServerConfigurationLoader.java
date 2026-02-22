package com.cafiaso.server.configuration.loader;

import com.cafiaso.server.Main;
import com.cafiaso.server.configuration.ServerConfiguration;
import com.cafiaso.server.configuration.PropertiesServerConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * A {@link ServerConfigurationLoader} implementation that loads configuration from a properties file
 * located in the classpath.
 */
public class PropertiesServerConfigurationLoader implements ServerConfigurationLoader {

    private final String path;

    public PropertiesServerConfigurationLoader(String path) {
        this.path = path;
    }

    @Override
    public ServerConfiguration load() throws IOException {
        try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: %s".formatted(path));
            }

            Properties properties = new Properties();
            properties.load(inputStream);

            return new PropertiesServerConfiguration(properties);
        }
    }
}
