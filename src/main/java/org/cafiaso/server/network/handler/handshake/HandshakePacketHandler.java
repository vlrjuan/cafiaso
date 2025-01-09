package org.cafiaso.server.network.handler.handshake;

import org.cafiaso.server.Server;
import org.cafiaso.server.network.connection.Connection;
import org.cafiaso.server.network.connection.ConnectionState;
import org.cafiaso.server.network.handler.PacketHandler;
import org.cafiaso.server.network.packet.client.handshake.HandshakePacket;

public class HandshakePacketHandler implements PacketHandler<HandshakePacket> {

    @Override
    public void handle(Connection connection, HandshakePacket packet) {
        int protocolVersion = packet.getProtocolVersion();

        if (protocolVersion != Server.PROTOCOL_VERSION) {
            return;
        }

        HandshakePacket.Intent intent = packet.getNextState();

        if (intent == HandshakePacket.Intent.STATUS) {
            connection.setState(ConnectionState.STATUS);
        } else if (intent == HandshakePacket.Intent.LOGIN) {
            connection.setState(ConnectionState.LOGIN);
        } else {
            throw new UnsupportedOperationException("Transfer is not supported");
        }
    }
}
