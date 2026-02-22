package com.cafiaso.server.network.protocol.packets.serverbound.status;

import com.cafiaso.server.network.connection.ConnectionState;
import com.cafiaso.server.network.io.NetworkBuffer;
import com.cafiaso.server.network.protocol.Packet;
import com.cafiaso.server.network.protocol.PacketDeserializer;

/**
 * Sent by the client to request the server status (MOTD, player count, etc.).
 */
@Packet(state = ConnectionState.STATUS, resourceId = "status_request", deserializer = StatusRequestPacket.Deserializer.class)
public record StatusRequestPacket() {

    public static class Deserializer implements PacketDeserializer<StatusRequestPacket> {

        @Override
        public StatusRequestPacket deserialize(NetworkBuffer in) {
            return new StatusRequestPacket();
        }
    }
}
