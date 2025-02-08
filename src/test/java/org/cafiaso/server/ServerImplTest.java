package org.cafiaso.server;

import org.cafiaso.server.configuration.ServerConfiguration;
import org.cafiaso.server.mojang.MojangClient;
import org.cafiaso.server.network.server.NetworkServer;
import org.cafiaso.server.player.PlayerManager;
import org.cafiaso.server.security.SecurityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServerImplTest {

    private static final String HOST = "localhost";
    private static final int PORT = 25565;

    private NetworkServer networkServer;
    private ServerConfiguration configuration;
    private SecurityManager securityManager;

    private Server server;

    @BeforeEach
    void setUp() {
        networkServer = mock(NetworkServer.class);
        configuration = mock(ServerConfiguration.class);
        securityManager = mock(SecurityManager.class);
        PlayerManager playerManager = mock(PlayerManager.class);
        MojangClient mojangClient = mock(MojangClient.class);

        server = new ServerImpl(configuration, networkServer, securityManager, playerManager, mojangClient);
    }

    @Test
    void start_ShouldStartServer() throws IOException {
        server.start(HOST, PORT);

        verify(configuration).load();
        verify(securityManager).generateKeyPair();
        verify(networkServer).bind(HOST, PORT);

        assertTrue(server.isRunning());

        assertTrue(server.stop());
    }

    @Test
    void start_ShouldStopServer_WhenSocketServerThrowsIOException() throws IOException {
        doThrow(new IOException()).when(networkServer).bind(HOST, PORT);

        server.start(HOST, PORT);

        assertFalse(server.isRunning());
    }

    @Test
    void stop_ShouldStopServer() throws IOException {
        server.start(HOST, PORT);
        assertTrue(server.isRunning());

        boolean isSuccessfully = server.stop();

        assertTrue(isSuccessfully);

        verify(networkServer).close();

        assertFalse(server.isRunning());
    }

    @Test
    void stop_ShouldDoNothing_WhenServerIsNotStarted() {
        boolean isSuccessfully = server.stop();

        assertTrue(isSuccessfully);

        verifyNoInteractions(networkServer);
    }

    @Test
    void stop_ShouldReturnFalse_WhenSocketServerThrowsIOException() throws IOException {
        server.start(HOST, PORT);
        assertTrue(server.isRunning());

        doThrow(new IOException()).when(networkServer).close();

        boolean isSuccessfully = server.stop();

        assertFalse(isSuccessfully);

        assertFalse(server.isRunning());
    }

    @Test
    void isRunning_ShouldReturnFalse_WhenServerIsNotStarted() {
        assertFalse(server.isRunning());
    }
}
