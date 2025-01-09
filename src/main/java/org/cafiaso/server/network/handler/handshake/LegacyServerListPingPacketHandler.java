package org.cafiaso.server.network.handler.handshake;

import org.cafiaso.server.network.connection.Connection;
import org.cafiaso.server.network.handler.PacketHandler;
import org.cafiaso.server.network.packet.client.handshake.LegacyServerListPingPacket;

import java.io.IOException;

public class LegacyServerListPingPacketHandler implements PacketHandler<LegacyServerListPingPacket> {

    @Override
    public void handle(Connection connection, LegacyServerListPingPacket packet) throws IOException {
        connection.close();
    }
}
