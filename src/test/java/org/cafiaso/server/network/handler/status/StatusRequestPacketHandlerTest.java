package org.cafiaso.server.network.handler.status;

import org.cafiaso.server.Server;
import org.cafiaso.server.configuration.ServerConfiguration;
import org.cafiaso.server.network.connection.Connection;
import org.cafiaso.server.network.packet.client.status.StatusRequestPacket;
import org.cafiaso.server.network.packet.server.status.StatusResponsePacket;
import org.cafiaso.server.player.PlayerManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StatusRequestPacketHandlerTest {

    private Connection connection;

    @BeforeEach
    void setUp() {
        connection = mock(Connection.class);
    }

    @Test
    void handle_ShouldSendStatusResponsePacket() throws IOException {
        Server server = mock(Server.class);
        when(connection.getServer()).thenReturn(server);

        int maximumPlayers = ServerConfiguration.DEFAULT_MAX_PLAYERS;
        int onlinePlayers = 10;
        String description = ServerConfiguration.DEFAULT_DESCRIPTION;

        ServerConfiguration configuration = mock(ServerConfiguration.class);
        when(server.getConfiguration()).thenReturn(configuration);

        when(configuration.getMaxPlayers()).thenReturn(maximumPlayers);
        when(configuration.getDescription()).thenReturn(description);

        PlayerManager playerManager = mock(PlayerManager.class);
        when(server.getPlayerManager()).thenReturn(playerManager);

        when(playerManager.getOnlinePlayers()).thenReturn(onlinePlayers);

        ArgumentCaptor<StatusResponsePacket> captor = ArgumentCaptor.forClass(StatusResponsePacket.class);

        StatusRequestPacket packet = mock(StatusRequestPacket.class);

        StatusRequestPacketHandler handler = new StatusRequestPacketHandler();
        handler.handle(connection, packet);

        verify(connection).sendPacket(captor.capture());

        StatusResponsePacket capturedPacket = captor.getValue();
        assertNotNull(capturedPacket);

        String expectedJsonResponse = "{\"players\":{\"max\":%d,\"online\":%d},\"description\":{\"text\":\"%s\"},\"version\":{\"protocol\":%d,\"name\":\"%s\"}}"
                .formatted(maximumPlayers, onlinePlayers, description, Server.PROTOCOL_VERSION, Server.MINECRAFT_VERSION);

        assertEquals(expectedJsonResponse, capturedPacket.getJsonResponse());
    }
}
