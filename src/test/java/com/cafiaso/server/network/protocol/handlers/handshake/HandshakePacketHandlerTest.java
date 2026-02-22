package com.cafiaso.server.network.protocol.handlers.handshake;

import com.cafiaso.server.network.connection.Connection;
import com.cafiaso.server.network.connection.ConnectionRegistry;
import com.cafiaso.server.network.connection.ConnectionState;
import com.cafiaso.server.network.protocol.packets.serverbound.handshake.HandshakePacket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HandshakePacketHandlerTest {

    @InjectMocks
    private HandshakePacketHandler handler;

    @Mock
    private ConnectionRegistry connectionRegistry;

    @Test
    void handle_ShouldSetConnectionStateToStatus_ForStatusIntent() {
        // Given
        HandshakePacket packet = mock(HandshakePacket.class);
        when(packet.intention()).thenReturn(HandshakePacket.Intention.STATUS);

        Connection connection = mock(Connection.class);

        // When
        handler.handle(connection, packet);

        // Then
        verify(connection).setState(ConnectionState.STATUS);
    }

    @Test
    void handle_ShouldSetConnectionStateToLogin_ForLoginIntent() {
        // Given
        HandshakePacket packet = mock(HandshakePacket.class);
        when(packet.intention()).thenReturn(HandshakePacket.Intention.LOGIN);

        Connection connection = mock(Connection.class);

        // When
        handler.handle(connection, packet);

        // Then
        verify(connection).setState(ConnectionState.LOGIN);
    }

    @Test
    void handle_ShouldCloseConnection_ForTransferIntent() {
        // Given
        HandshakePacket packet = mock(HandshakePacket.class);
        when(packet.intention()).thenReturn(HandshakePacket.Intention.TRANSFER);

        Connection connection = mock(Connection.class);

        // When
        handler.handle(connection, packet);

        // Then
        verify(connectionRegistry).closeConnection(connection);
    }
}
