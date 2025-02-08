package org.cafiaso.server.network.handler.login;

import org.cafiaso.server.Server;
import org.cafiaso.server.network.connection.Connection;
import org.cafiaso.server.network.connection.ConnectionState;
import org.cafiaso.server.network.packet.client.login.LoginAcknowledgedPacket;
import org.cafiaso.server.network.server.NetworkServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

class LoginAcknowledgedPacketHandlerTest {

    private Connection connection;

    @BeforeEach
    void setUp() {
        NetworkServer networkServer = mock(NetworkServer.class);

        Server server = mock(Server.class);
        when(server.getNetworkServer()).thenReturn(networkServer);

        connection = mock(Connection.class);
        when(connection.getServer()).thenReturn(server);
    }

    @Test
    void handle_ShouldSetConnectionStateToConfiguration() throws IOException {
        LoginAcknowledgedPacket packet = new LoginAcknowledgedPacket();

        LoginAcknowledgedPacketHandler handler = new LoginAcknowledgedPacketHandler();
        handler.handle(connection, packet);

        verify(connection).setState(ConnectionState.CONFIGURATION);
    }
}
