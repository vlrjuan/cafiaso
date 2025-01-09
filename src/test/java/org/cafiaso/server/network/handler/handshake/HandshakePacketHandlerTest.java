package org.cafiaso.server.network.handler.handshake;

import org.cafiaso.server.Server;
import org.cafiaso.server.network.connection.Connection;
import org.cafiaso.server.network.connection.ConnectionState;
import org.cafiaso.server.network.packet.client.handshake.HandshakePacket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HandshakePacketHandlerTest {

    // Handler
    private static final HandshakePacketHandler HANDLER = new HandshakePacketHandler();

    private Connection connection;
    private HandshakePacket packet;

    @BeforeEach
    void setUp() {
        connection = mock(Connection.class);

        packet = mock(HandshakePacket.class);
        when(packet.getProtocolVersion()).thenReturn(Server.PROTOCOL_VERSION);
    }

    @Test
    void handle_ShouldDoNothing_WhenProtocolVersionIsIncorrect() {
        HandshakePacket packet = mock(HandshakePacket.class);
        when(packet.getProtocolVersion()).thenReturn(0);

        HANDLER.handle(connection, packet);

        verifyNoInteractions(connection);
    }

    @Test
    void handle_ShouldSetStatusState_WhenIntentIsStatus() {
        when(packet.getNextState()).thenReturn(HandshakePacket.Intent.STATUS);

        HANDLER.handle(connection, packet);

        verify(connection).setState(ConnectionState.STATUS);
    }

    @Test
    void handle_ShouldSetLoginState_WhenIntentIsLogin() {
        when(packet.getNextState()).thenReturn(HandshakePacket.Intent.LOGIN);

        HANDLER.handle(connection, packet);

        verify(connection).setState(ConnectionState.LOGIN);
    }

    @Test
    void handle_ShouldThrowException_WhenIntentIsTransfer() {
        when(packet.getNextState()).thenReturn(HandshakePacket.Intent.TRANSFER);

        assertThrows(UnsupportedOperationException.class, () -> HANDLER.handle(connection, packet));
    }
}
