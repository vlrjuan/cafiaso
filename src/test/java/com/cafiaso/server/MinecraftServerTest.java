package com.cafiaso.server;

import com.cafiaso.server.network.server.NetworkServer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MinecraftServerTest {

    @Mock
    private NetworkServer networkServer;

    @InjectMocks
    private MinecraftServer server;

    @Test
    void start_ShouldThrowIllegalStateException_WhenServerIsAlreadyRunning() {
        // Given
        String host = "localhost";
        int port = 25565;

        // When
        server.start(host, port);

        // Then
        assertThrowsExactly(IllegalStateException.class, () -> server.start(host, port));
    }

    @Test
    void start_ShouldBindNetworkServerToGivenHostAndPort() throws IOException {
        // Given
        String host = "localhost";
        int port = 25565;

        // When
        server.start(host, port);

        // Then
        verify(networkServer).bind(host, port);
    }

    @Test
    void stop_ShouldNotThrowException_WhenServerIsNotRunning() {
        assertDoesNotThrow(() -> server.stop());
    }

    @Test
    void stop_ShouldStopNetworkServer_WhenServerIsRunning() throws IOException {
        // Given
        String host = "localhost";
        int port = 25565;

        server.start(host, port);

        // When
        server.stop();

        // Then
        verify(networkServer).close();
    }
}
