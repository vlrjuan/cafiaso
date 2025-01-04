package org.cafiaso.server.network.handlers.handshake;

import org.cafiaso.server.network.PacketHandler;
import org.cafiaso.server.network.connection.Connection;
import org.cafiaso.server.network.packets.client.handshake.LegacyServerListPingPacket;

import java.io.IOException;

public class LegacyServerListPingPacketHandler implements PacketHandler<LegacyServerListPingPacket> {

    @Override
    public void handle(Connection connection, LegacyServerListPingPacket packet) throws IOException {
        connection.close();
    }
}
