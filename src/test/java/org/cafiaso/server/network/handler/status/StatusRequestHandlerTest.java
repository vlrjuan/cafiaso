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

class StatusRequestHandlerTest {

    // Data
    private static final int MAXIMUM_PLAYERS = ServerConfiguration.DEFAULT_MAXIMUM_PLAYERS;
    private static final int ONLINE_PLAYERS = 0;
    private static final String DESCRIPTION = ServerConfiguration.DEFAULT_DESCRIPTION;

    // Handler
    private static final StatusRequestHandler HANDLER = new StatusRequestHandler();

    private Connection connection;

    @BeforeEach
    void setUp() {
        connection = mock(Connection.class);

        Server server = mock(Server.class);
        when(connection.getServer()).thenReturn(server);

        ServerConfiguration configuration = mock(ServerConfiguration.class);
        when(server.getConfiguration()).thenReturn(configuration);

        when(configuration.getMaximumPlayers()).thenReturn(MAXIMUM_PLAYERS);
        when(configuration.getDescription()).thenReturn(DESCRIPTION);

        PlayerManager playerManager = mock(PlayerManager.class);
        when(server.getPlayerManager()).thenReturn(playerManager);

        when(playerManager.getOnlinePlayers()).thenReturn(ONLINE_PLAYERS);
    }

    @Test
    void handle_ShouldSendStatusResponsePacket() throws IOException {
        ArgumentCaptor<StatusResponsePacket> captor = ArgumentCaptor.forClass(StatusResponsePacket.class);

        StatusRequestPacket packet = mock(StatusRequestPacket.class);

        HANDLER.handle(connection, packet);

        verify(connection).sendPacket(captor.capture());

        StatusResponsePacket capturedPacket = captor.getValue();

        assertNotNull(capturedPacket);

        String expectedJsonResponse = "{\"players\":{\"max\":%d,\"online\":%d},\"description\":{\"text\":\"%s\"},\"version\":{\"protocol\":%d,\"name\":\"%s\"}}"
                .formatted(MAXIMUM_PLAYERS, ONLINE_PLAYERS, DESCRIPTION, Server.PROTOCOL_VERSION, Server.VERSION_NAME);

        assertEquals(expectedJsonResponse, capturedPacket.jsonResponse());
    }
}
