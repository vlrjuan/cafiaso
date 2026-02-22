package com.cafiaso.server.network.protocol.handlers.status;

import com.cafiaso.server.network.connection.Connection;
import com.cafiaso.server.network.connection.ConnectionRegistry;
import com.cafiaso.server.network.dispatcher.PacketDispatcher;
import com.cafiaso.server.network.protocol.packets.clientbound.status.PongResponsePacket;
import com.cafiaso.server.network.protocol.packets.serverbound.status.PingRequestStatusPacket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PingRequestStatusPacketHandlerTest {

    @InjectMocks
    private PingRequestStatusPacketHandler handler;

    @Mock
    private PacketDispatcher packetDispatcher;

    @Mock
    private ConnectionRegistry connectionRegistry;

    @Test
    void handle_ShouldRespondWithPongAndCloseConnection() throws IOException {
        // Given
        PingRequestStatusPacket packet = mock(PingRequestStatusPacket.class);
        when(packet.payload()).thenReturn(123L);

        Connection connection = mock(Connection.class);

        // When
        handler.handle(connection, packet);

        // Then
        verify(connectionRegistry).closeConnection(connection);

        PongResponsePacket pongResponsePacket = new PongResponsePacket(123L);
        verify(packetDispatcher).dispatch(connection, pongResponsePacket);
    }
}
