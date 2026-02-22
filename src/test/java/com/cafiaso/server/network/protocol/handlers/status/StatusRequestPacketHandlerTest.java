package com.cafiaso.server.network.protocol.handlers.status;

import com.cafiaso.server.configuration.ServerConfiguration;
import com.cafiaso.server.icon.ServerIcon;
import com.cafiaso.server.network.connection.Connection;
import com.cafiaso.server.network.dispatcher.PacketDispatcher;
import com.cafiaso.server.network.protocol.packets.clientbound.status.StatusResponsePacket;
import com.cafiaso.server.network.protocol.packets.serverbound.status.StatusRequestPacket;
import com.cafiaso.server.player.Player;
import com.cafiaso.server.player.PlayerManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static com.cafiaso.server.MinecraftConstants.*;
import static com.cafiaso.server.configuration.ServerConfiguration.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatusRequestPacketHandlerTest {

    private static final String NAME_1 = "Choukas";
    private static final UUID ID_1 = UUID.randomUUID();

    private static final String NAME_2 = "Alex";
    private static final UUID ID_2 = UUID.randomUUID();

    @Mock
    private ServerConfiguration configuration;

    @Mock
    private ServerIcon icon;

    @Mock
    private PlayerManager playerManager;

    @Mock
    private PacketDispatcher packetDispatcher;

    @InjectMocks
    private StatusRequestPacketHandler handler;

    @Test
    void handle_OK() throws IOException {
        // Given
        when(configuration.getMaxPlayers()).thenReturn(DEFAULT_MAX_PLAYERS);
        when(configuration.getDescription()).thenReturn(DEFAULT_DESCRIPTION);

        when(icon.getData()).thenReturn(Optional.of("data:image/png;base64,AQID"));

        Player player1 = mock(Player.class);
        when(player1.getName()).thenReturn(NAME_1);
        when(player1.getId()).thenReturn(ID_1);

        Player player2 = mock(Player.class);
        when(player2.getName()).thenReturn(NAME_2);
        when(player2.getId()).thenReturn(ID_2);

        when(playerManager.getPlayers()).thenReturn(Arrays.asList(player1, player2));

        StatusRequestPacket packet = mock(StatusRequestPacket.class);
        Connection connection = mock(Connection.class);

        // When
        handler.handle(connection, packet);

        // Then
        StatusResponsePacket statusResponsePacket = new StatusResponsePacket(
                "{\"favicon\":\"data:image/png;base64,AQID\",\"players\":{\"max\":%d,\"online\":2,\"sample\":[{\"name\":\"%s\",\"id\":\"%s\"},{\"name\":\"%s\",\"id\":\"%s\"}]},\"description\":{\"text\":\"%s\"},\"version\":{\"protocol\":%d,\"name\":\"%s\"}}"
                        .formatted(DEFAULT_MAX_PLAYERS, NAME_1, ID_1, NAME_2, ID_2, DEFAULT_DESCRIPTION, PROTOCOL_VERSION, MINECRAFT_VERSION)
        );

        verify(packetDispatcher).dispatch(connection, statusResponsePacket);
    }

    @Test
    void handle_ShouldNotThrowException_WhenNoFavicon() throws IOException {
        // Given
        when(configuration.getMaxPlayers()).thenReturn(DEFAULT_MAX_PLAYERS);
        when(configuration.getDescription()).thenReturn(DEFAULT_DESCRIPTION);

        when(icon.getData()).thenReturn(Optional.empty());

        Player player1 = mock(Player.class);
        when(player1.getName()).thenReturn(NAME_1);
        when(player1.getId()).thenReturn(ID_1);

        Player player2 = mock(Player.class);
        when(player2.getName()).thenReturn(NAME_2);
        when(player2.getId()).thenReturn(ID_2);

        when(playerManager.getPlayers()).thenReturn(Arrays.asList(player1, player2));

        StatusRequestPacket packet = mock(StatusRequestPacket.class);
        Connection connection = mock(Connection.class);

        // When
        handler.handle(connection, packet);

        // Then
        StatusResponsePacket statusResponsePacket = new StatusResponsePacket(
                "{\"players\":{\"max\":%d,\"online\":2,\"sample\":[{\"name\":\"%s\",\"id\":\"%s\"},{\"name\":\"%s\",\"id\":\"%s\"}]},\"description\":{\"text\":\"%s\"},\"version\":{\"protocol\":%d,\"name\":\"%s\"}}"
                        .formatted(DEFAULT_MAX_PLAYERS, NAME_1, ID_1, NAME_2, ID_2, DEFAULT_DESCRIPTION, PROTOCOL_VERSION, MINECRAFT_VERSION)
        );

        verify(packetDispatcher).dispatch(connection, statusResponsePacket);
    }
}
