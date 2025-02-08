package org.cafiaso.server.network.handler.handshake;

import org.cafiaso.server.Server;
import org.cafiaso.server.network.connection.Connection;
import org.cafiaso.server.network.connection.ConnectionState;
import org.cafiaso.server.network.handler.PacketHandler;
import org.cafiaso.server.network.packet.client.handshake.HandshakePacket;
import org.cafiaso.server.network.packet.client.handshake.HandshakePacket.Intent;
import org.cafiaso.server.network.server.NetworkServer;

import java.io.IOException;

/**
 * Handles the {@link HandshakePacket}.
 */
public class HandshakePacketHandler implements PacketHandler<HandshakePacket> {

    @Override
    public void handle(Connection connection, HandshakePacket packet) throws IOException {
        NetworkServer networkServer = connection.getServer().getNetworkServer();

        int protocolVersion = packet.getProtocolVersion();

        if (protocolVersion != Server.PROTOCOL_VERSION) {
            // The client is using an outdated version, close the connection
            networkServer.closeConnection(connection);

            return;
        }

        Intent intent = packet.getNextState();

        if (intent == Intent.STATUS) {
            connection.setState(ConnectionState.STATUS);
        } else if (intent == Intent.LOGIN) {
            connection.setState(ConnectionState.LOGIN);
        } else {
            // ConnectionState.TRANSFER is not supported by the server, close the connection
            networkServer.closeConnection(connection);
        }
    }
}
