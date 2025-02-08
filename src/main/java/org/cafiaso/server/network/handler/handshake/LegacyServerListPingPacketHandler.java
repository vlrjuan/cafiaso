package org.cafiaso.server.network.handler.handshake;

import org.cafiaso.server.network.connection.Connection;
import org.cafiaso.server.network.handler.PacketHandler;
import org.cafiaso.server.network.packet.client.handshake.LegacyServerListPingPacket;
import org.cafiaso.server.network.server.NetworkServer;

import java.io.IOException;

/**
 * Handles the {@link LegacyServerListPingPacket} packet.
 */
public class LegacyServerListPingPacketHandler implements PacketHandler<LegacyServerListPingPacket> {

    @Override
    public void handle(Connection connection, LegacyServerListPingPacket packet) throws IOException {
        // Legacy server list pings are not supported by the server
        NetworkServer networkServer = connection.getServer().getNetworkServer();
        networkServer.closeConnection(connection);
    }
}
