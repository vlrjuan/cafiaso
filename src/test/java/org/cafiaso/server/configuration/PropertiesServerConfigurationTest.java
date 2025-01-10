package org.cafiaso.server.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PropertiesServerConfigurationTest {

    private ClassLoader classLoader;
    private PropertiesServerConfiguration configuration;

    @BeforeEach
    void setUp() {
        classLoader = mock(ClassLoader.class);
        configuration = new PropertiesServerConfiguration(classLoader);
    }

    @Test
    void test_ShouldReturnCorrectValues() {
        String config = """
                max-players=50
                description=A Cafiaso Server
                """;
        InputStream mockInputStream = new ByteArrayInputStream(config.getBytes());

        when(classLoader.getResourceAsStream("server.properties")).thenReturn(mockInputStream);

        configuration.load();

        assertEquals(50, configuration.getMaxPlayers());
        assertEquals("A Cafiaso Server", configuration.getDescription());
    }

    @Test
    void load_ShouldThrowException_WhenFileNotFound() {
        when(classLoader.getResourceAsStream("server.properties")).thenReturn(null);

        assertThrowsExactly(RuntimeException.class, configuration::load, "File not found");
    }

    @Test
    void test_ShouldReturnDefaultValue_WhenPropertyIsMissing() {
        String config = """
                max-players=50
                """;
        InputStream mockInputStream = new ByteArrayInputStream(config.getBytes());

        when(classLoader.getResourceAsStream("server.properties")).thenReturn(mockInputStream);

        configuration.load();

        assertEquals(50, configuration.getMaxPlayers());
        assertEquals(ServerConfiguration.DEFAULT_DESCRIPTION, configuration.getDescription());
    }

    @Test
    void test_ShouldReturnDefaultValues_WhenFileIsEmpty() {
        String config = "";
        InputStream mockInputStream = new ByteArrayInputStream(config.getBytes());

        when(classLoader.getResourceAsStream("server.properties")).thenReturn(mockInputStream);

        configuration.load();

        assertEquals(ServerConfiguration.DEFAULT_MAX_PLAYERS, configuration.getMaxPlayers());
        assertEquals(ServerConfiguration.DEFAULT_DESCRIPTION, configuration.getDescription());
    }

    @Test
    void test_ShouldReturnDefaultValues_WhenConfigurationIsNotLoaded() {
        assertEquals(ServerConfiguration.DEFAULT_MAX_PLAYERS, configuration.getMaxPlayers());
        assertEquals(ServerConfiguration.DEFAULT_DESCRIPTION, configuration.getDescription());
    }
}
