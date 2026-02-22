package com.cafiaso.server.configuration.loader;

import com.cafiaso.server.configuration.ServerConfiguration;
import com.cafiaso.server.configuration.PropertiesServerConfiguration;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PropertiesServerServerConfigurationLoaderTest {

    @Test
    void loadConfiguration_ShouldThrowIOException_WhenFileDoesNotExist() {
        // Given
        PropertiesServerConfigurationLoader loader = new PropertiesServerConfigurationLoader("unexisting/path/server.properties");

        // When/Then
        assertThrowsExactly(IOException.class, loader::load);
    }

    @Test
    void loadConfiguration_ShouldLoadConfiguration_WhenFileExists() throws IOException {
        // Given
        PropertiesServerConfigurationLoader loader = new PropertiesServerConfigurationLoader("server.properties");

        // When
        ServerConfiguration configuration = loader.load();

        // Then
        assertInstanceOf(PropertiesServerConfiguration.class, configuration);

        assertEquals(50, configuration.getMaxPlayers());
        assertEquals("Welcome to the server!", configuration.getDescription());
    }
}
