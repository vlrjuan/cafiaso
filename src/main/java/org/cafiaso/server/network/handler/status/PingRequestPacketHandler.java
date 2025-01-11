package org.cafiaso.server.network.handler.status;

import org.cafiaso.server.network.connection.Connection;
import org.cafiaso.server.network.handler.PacketHandler;
import org.cafiaso.server.network.packet.client.status.PingRequestPacket;
import org.cafiaso.server.network.packet.server.status.PingResponsePacket;

import java.io.IOException;

public class PingRequestPacketHandler implements PacketHandler<PingRequestPacket> {

    @Override
    public void handle(Connection connection, PingRequestPacket packet) throws IOException {
        long payload = packet.getPayload();

        connection.sendPacket(new PingResponsePacket(payload));

        connection.close();
    }
}
