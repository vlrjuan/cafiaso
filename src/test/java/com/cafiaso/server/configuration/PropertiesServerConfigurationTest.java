package com.cafiaso.server.configuration;

import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class PropertiesServerConfigurationTest {

    @Test
    void getMaxPlayers_ShouldReturnDefaultMaxPlayers_WhenPropertyIsNotSet() {
        // Given
        PropertiesServerConfiguration config = new PropertiesServerConfiguration(new Properties());

        // When/Then
        assertEquals(ServerConfiguration.DEFAULT_MAX_PLAYERS, config.getMaxPlayers());
    }

    @Test
    void getMaxPlayers_ShouldReturnConfiguredMaxPlayers_WhenPropertyIsSet() {
        // Given
        Properties properties = new Properties();
        properties.setProperty("server.max-players", "50");

        PropertiesServerConfiguration config = new PropertiesServerConfiguration(properties);

        // When/Then
        assertEquals(50, config.getMaxPlayers());
    }

    @Test
    void getDescription_ShouldReturnDefaultDescription_WhenPropertyIsNotSet() {
        // Given
        PropertiesServerConfiguration config = new PropertiesServerConfiguration(new Properties());

        // When/Then
        assertEquals(ServerConfiguration.DEFAULT_DESCRIPTION, config.getDescription());
    }

    @Test
    void getDescription_ShouldReturnConfiguredDescription_WhenPropertyIsSet() {
        // Given
        Properties properties = new Properties();
        properties.setProperty("server.description", "Welcome to the server!");

        PropertiesServerConfiguration config = new PropertiesServerConfiguration(properties);

        // When/Then
        assertEquals("Welcome to the server!", config.getDescription());
    }
}
