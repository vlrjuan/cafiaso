package org.cafiaso.server.network.handlers.status;

import org.cafiaso.server.network.PacketHandler;
import org.cafiaso.server.network.connection.Connection;
import org.cafiaso.server.network.packets.client.status.PingRequestPacket;
import org.cafiaso.server.network.packets.server.status.PingResponsePacket;

import java.io.IOException;

public class PingRequestHandler implements PacketHandler<PingRequestPacket> {

    @Override
    public void handle(Connection connection, PingRequestPacket packet) throws IOException {
        long payload = packet.getPayload();

        connection.sendPacket(new PingResponsePacket(payload));
    }
}
