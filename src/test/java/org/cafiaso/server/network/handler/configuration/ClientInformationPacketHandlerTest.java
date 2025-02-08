package org.cafiaso.server.network.handler.configuration;

import org.cafiaso.server.network.connection.Connection;
import org.cafiaso.server.network.packet.client.configuration.ClientInformationPacket;
import org.cafiaso.server.network.packet.server.configuration.ClientBoundPluginMessagePacket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientInformationPacketHandlerTest {

    private static final ClientInformationPacketHandler HANDLER = new ClientInformationPacketHandler();

    private Connection connection;

    @BeforeEach
    void setUp() {
        connection = mock(Connection.class);
    }

    @Test
    void handle_ShouldSendClientBoundPluginMessagePacket() throws IOException {
        ArgumentCaptor<ClientBoundPluginMessagePacket> captor = ArgumentCaptor.forClass(ClientBoundPluginMessagePacket.class);

        ClientInformationPacket packet = new ClientInformationPacket();
        HANDLER.handle(connection, packet);

        verify(connection).sendPacket(captor.capture());

        ClientBoundPluginMessagePacket sentPacket = captor.getValue();

        assertEquals("minecraft:brand", sentPacket.getChannel());
        assertArrayEquals("cafiaso".getBytes(), sentPacket.getData());
    }
}
