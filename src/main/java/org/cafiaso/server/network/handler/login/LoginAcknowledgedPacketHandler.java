package org.cafiaso.server.network.handler.login;

import org.cafiaso.server.network.connection.Connection;
import org.cafiaso.server.network.connection.ConnectionState;
import org.cafiaso.server.network.handler.PacketHandler;
import org.cafiaso.server.network.packet.client.login.LoginAcknowledgedPacket;
import org.cafiaso.server.network.server.NetworkServer;

import java.io.IOException;

/**
 * Handles the {@link LoginAcknowledgedPacket}.
 */
public class LoginAcknowledgedPacketHandler implements PacketHandler<LoginAcknowledgedPacket> {

    @Override
    public void handle(Connection connection, LoginAcknowledgedPacket packet) throws IOException {
        // The client has acknowledged the login success, we can now move to the configuration state
        connection.setState(ConnectionState.CONFIGURATION);

        // TODO: Remove
        NetworkServer networkServer = connection.getServer().getNetworkServer();
        networkServer.closeConnection(connection);
    }
}
