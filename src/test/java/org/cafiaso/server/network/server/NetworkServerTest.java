package org.cafiaso.server.network.server;

import org.cafiaso.server.network.connection.Connection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetAddress;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NetworkServerTest {

    private AbstractNetworkServer server;
    private Connection connection;
    private InetAddress address;

    @BeforeEach
    void setUp() throws Exception {
        server = new CafiasoNetworkServer();

        connection = mock(Connection.class);
        when(connection.isOpen()).thenReturn(true);
        when(connection.readPacket()).then(invocation -> {
            when(connection.isOpen()).thenReturn(false); // Close the connection after reading a packet

            return true;
        });

        address = InetAddress.getByName("127.0.0.1");
        when(connection.getAddress()).thenReturn(address);
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

        InetAddress address2 = InetAddress.getByName("127.0.0.2");
        when(connection2.getAddress()).thenReturn(address2);

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

        assertEquals(connection, server.getConnections().get(address));

        Thread.sleep(100); // Delay to ensure thread execution

        // Should be 2 calls, but sometimes it's only 1 because of the
        // execution's speed
        verify(connection, atLeast(1)).isOpen();
        verify(connection).readPacket();
    }

    @Test
    void acceptConnection_ShouldReplaceExistingConnection_WhenNewConnectionWithSameIpIsCreated() {
        Connection connection2 = mock(Connection.class);
        when(connection2.getAddress()).thenReturn(address);

        server.acceptConnection(connection);
        server.acceptConnection(connection2);

        assertEquals(1, server.getConnections().size());
        assertEquals(connection2, server.getConnections().get(address));
    }

    // We need this class to initialize AbstractNetworkServer connections map
    private static class CafiasoNetworkServer extends AbstractNetworkServer {

        @Override
        public void bind(String host, int port) {
            // Do nothing
        }
    }
}
