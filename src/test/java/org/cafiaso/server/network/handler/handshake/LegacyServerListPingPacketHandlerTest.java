package org.cafiaso.server.network.handler.handshake;

import org.cafiaso.server.Server;
import org.cafiaso.server.network.connection.Connection;
import org.cafiaso.server.network.packet.client.handshake.LegacyServerListPingPacket;
import org.cafiaso.server.network.server.NetworkServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

class LegacyServerListPingPacketHandlerTest {

    private NetworkServer networkServer;
    private Connection connection;

    @BeforeEach
    void setUp() {
        networkServer = mock(NetworkServer.class);

        Server server = mock(Server.class);
        when(server.getNetworkServer()).thenReturn(networkServer);

        connection = mock(Connection.class);
        when(connection.getServer()).thenReturn(server);
    }

    @Test
    void handle_ShouldCloseConnection() throws IOException {
        LegacyServerListPingPacket packet = mock(LegacyServerListPingPacket.class);

        LegacyServerListPingPacketHandler handler = new LegacyServerListPingPacketHandler();
        handler.handle(connection, packet);

        verify(networkServer).closeConnection(connection);
    }
}
