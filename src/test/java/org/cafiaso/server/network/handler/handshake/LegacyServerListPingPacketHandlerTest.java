package org.cafiaso.server.network.handler.handshake;

import org.cafiaso.server.network.connection.Connection;
import org.cafiaso.server.network.packet.client.handshake.LegacyServerListPingPacket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

class LegacyServerListPingPacketHandlerTest {

    // Handler
    private static final LegacyServerListPingPacketHandler HANDLER = new LegacyServerListPingPacketHandler();

    private Connection connection;

    @BeforeEach
    void setUp() {
        connection = mock(Connection.class);
    }

    @Test
    void handle_ShouldCloseConnection() throws IOException {
        LegacyServerListPingPacket packet = mock(LegacyServerListPingPacket.class);

        HANDLER.handle(connection, packet);

        verify(connection).close();
    }
}
