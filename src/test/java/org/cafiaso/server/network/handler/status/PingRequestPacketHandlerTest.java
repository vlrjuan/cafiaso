package org.cafiaso.server.network.handler.status;

import org.cafiaso.server.network.connection.Connection;
import org.cafiaso.server.network.packet.client.status.PingRequestPacket;
import org.cafiaso.server.network.packet.server.status.PingResponsePacket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PingRequestPacketHandlerTest {

    // Data
    private static final long PAYLOAD = 123L;

    // Handler
    private static final PingRequestPacketHandler HANDLER = new PingRequestPacketHandler();

    private Connection connection;

    @BeforeEach
    void setUp() {
        connection = mock(Connection.class);
    }

    @Test
    void handle_ShouldSendPingResponsePacket() throws IOException {
        ArgumentCaptor<PingResponsePacket> captor = ArgumentCaptor.forClass(PingResponsePacket.class);

        PingRequestPacket packet = mock(PingRequestPacket.class);
        when(packet.getPayload()).thenReturn(PAYLOAD);

        HANDLER.handle(connection, packet);

        verify(connection).sendPacket(captor.capture());

        PingResponsePacket capturedPacket = captor.getValue();
        assertNotNull(capturedPacket);

        assertEquals(PAYLOAD, capturedPacket.payload());
    }
}
