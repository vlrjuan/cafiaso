package org.cafiaso.server.network.server;

import org.cafiaso.server.network.connection.Connection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NetworkServerTest {

    private AbstractNetworkServer server;
    private Connection connection;

    @BeforeEach
    void setUp() throws Exception {
        server = new CafiasoNetworkServer();

        connection = mock(Connection.class);
        when(connection.isOpen()).thenReturn(true);
        // When connection reads a packet, next call to isOpen() will return false
        doAnswer(invocation -> {
            when(connection.isOpen()).thenReturn(false);

            return null;
        }).when(connection).readPackets();
    }

    @Test
    void close_ShouldCloseAllConnections() throws IOException {
        server.acceptConnection(connection);

        assertEquals(1, server.getConnections().size());

        server.close();

        verify(connection).close();

        assertTrue(server.getConnections().isEmpty());
    }

    @Test
    void close_ShouldCloseAllConnections_WhenOneConnectionThrownException() throws IOException {
        Connection connection2 = mock(Connection.class);

        doThrow(new IOException()).when(connection2).close();

        server.acceptConnection(connection);
        server.acceptConnection(connection2);

        assertEquals(2, server.getConnections().size());

        assertDoesNotThrow(server::close);

        verify(connection).close();
        verify(connection2).close();

        assertTrue(server.getConnections().isEmpty());
    }

    @Test
    void acceptConnection_ShouldStartReadingPacket() throws IOException, InterruptedException {
        server.acceptConnection(connection);

        assertEquals(connection, server.getConnections().getFirst());

        Thread.sleep(100); // Delay to ensure thread execution

        // Should be 2 calls, but sometimes it's only 1 because of the
        // execution's speed
        verify(connection, atLeast(1)).isOpen();
        verify(connection).readPackets();
    }

    @Test
    void closeConnection_ShouldRemoveConnection() throws IOException {
        server.acceptConnection(connection);

        assertEquals(1, server.getConnections().size());

        server.closeConnection(connection);

        verify(connection).close();
        assertTrue(server.getConnections().isEmpty());
    }

    // We need this class to initialize AbstractNetworkServer connections map
    private static class CafiasoNetworkServer extends AbstractNetworkServer {

        @Override
        public void bind(String host, int port) {
            // Do nothing
        }
    }
}
