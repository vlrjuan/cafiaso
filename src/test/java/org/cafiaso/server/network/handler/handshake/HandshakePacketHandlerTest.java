package org.cafiaso.server.network.handler.handshake;

import org.cafiaso.server.Server;
import org.cafiaso.server.network.connection.Connection;
import org.cafiaso.server.network.connection.ConnectionState;
import org.cafiaso.server.network.packet.client.handshake.HandshakePacket;
import org.cafiaso.server.network.server.NetworkServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

class HandshakePacketHandlerTest {

    // Handler
    private static final HandshakePacketHandler HANDLER = new HandshakePacketHandler();

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
    void handle_ShouldCloseConnection_WhenClientVersionIsOutdated() throws IOException {
        HandshakePacket packet = mock(HandshakePacket.class);
        when(packet.getProtocolVersion()).thenReturn(0);

        HandshakePacketHandler handler = new HandshakePacketHandler();
        handler.handle(connection, packet);

        verify(networkServer).closeConnection(connection);
    }

    @Test
    void handle_ShouldSetConnectionStateToStatus_WhenIntentIsStatus() throws IOException {
        HandshakePacket packet = mock(HandshakePacket.class);
        when(packet.getProtocolVersion()).thenReturn(Server.PROTOCOL_VERSION);
        when(packet.getNextState()).thenReturn(HandshakePacket.Intent.STATUS);

        HANDLER.handle(connection, packet);

        verify(connection).setState(ConnectionState.STATUS);
    }

    @Test
    void handle_ShouldSetConnectionStateToLogin_WhenIntentIsLogin() throws IOException {
        HandshakePacket packet = mock(HandshakePacket.class);
        when(packet.getProtocolVersion()).thenReturn(Server.PROTOCOL_VERSION);
        when(packet.getNextState()).thenReturn(HandshakePacket.Intent.LOGIN);

        HANDLER.handle(connection, packet);

        verify(connection).setState(ConnectionState.LOGIN);
    }

    @Test
    void handle_ShouldCloseConnection_WhenIntentIsTransfer() throws IOException {
        HandshakePacket packet = mock(HandshakePacket.class);
        when(packet.getProtocolVersion()).thenReturn(Server.PROTOCOL_VERSION);
        when(packet.getNextState()).thenReturn(HandshakePacket.Intent.TRANSFER);

        HANDLER.handle(connection, packet);

        verify(networkServer).closeConnection(connection);
    }
}
