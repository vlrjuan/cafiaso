package com.cafiaso.server.configuration.loader;

import com.cafiaso.server.configuration.ServerConfiguration;

import java.io.IOException;

/**
 * A loader for {@link ServerConfiguration} instances.
 *
 * @see PropertiesServerConfigurationLoader
 */
public interface ServerConfigurationLoader {

    /**
     * Loads a {@link ServerConfiguration}.
     *
     * @return the loaded configuration
     * @throws IOException if an I/O error occurs
     */
    ServerConfiguration load() throws IOException;
}
