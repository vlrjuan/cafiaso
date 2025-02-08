package org.cafiaso.server.network.handler.status;

import org.cafiaso.server.network.connection.Connection;
import org.cafiaso.server.network.handler.PacketHandler;
import org.cafiaso.server.network.packet.client.status.PingRequestPacket;
import org.cafiaso.server.network.packet.server.status.PongResponsePacket;
import org.cafiaso.server.network.server.NetworkServer;

import java.io.IOException;

/**
 * Handles the {@link PingRequestPacket}.
 */
public class PingRequestPacketHandler implements PacketHandler<PingRequestPacket> {

    @Override
    public void handle(Connection connection, PingRequestPacket packet) throws IOException {
        NetworkServer networkServer = connection.getServer().getNetworkServer();

        long payload = packet.getPayload();

        // Send a response back to the client using the same payload
        connection.sendPacket(new PongResponsePacket(payload));

        networkServer.closeConnection(connection); // Close the connection after sending the response
    }
}
