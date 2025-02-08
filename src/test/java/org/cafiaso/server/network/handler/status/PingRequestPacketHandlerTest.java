package org.cafiaso.server.network.handler.status;

import org.cafiaso.server.Server;
import org.cafiaso.server.network.connection.Connection;
import org.cafiaso.server.network.packet.client.status.PingRequestPacket;
import org.cafiaso.server.network.packet.server.status.PongResponsePacket;
import org.cafiaso.server.network.server.NetworkServer;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PingRequestPacketHandlerTest {

    @Test
    void handle_ShouldSendPingResponsePacketAndCloseConnection() throws IOException {
        NetworkServer networkServer = mock(NetworkServer.class);

        Server server = mock(Server.class);
        when(server.getNetworkServer()).thenReturn(networkServer);

        Connection connection = mock(Connection.class);
        when(connection.getServer()).thenReturn(server);

        long payload = 123L;

        PingRequestPacket packet = mock(PingRequestPacket.class);
        when(packet.getPayload()).thenReturn(payload);

        PingRequestPacketHandler handler = new PingRequestPacketHandler();
        handler.handle(connection, packet);

        verify(networkServer).closeConnection(connection);

        ArgumentCaptor<PongResponsePacket> pongResponsePacketCaptor = ArgumentCaptor.forClass(PongResponsePacket.class);

        verify(connection).sendPacket(pongResponsePacketCaptor.capture());

        PongResponsePacket capturedPacket = pongResponsePacketCaptor.getValue();
        assertNotNull(capturedPacket);
        assertEquals(payload, capturedPacket.getPayload());
    }
}
