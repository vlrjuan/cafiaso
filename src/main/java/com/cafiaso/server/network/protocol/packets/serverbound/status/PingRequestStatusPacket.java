package com.cafiaso.server.network.protocol.packets.serverbound.status;

import com.cafiaso.server.network.connection.ConnectionState;
import com.cafiaso.server.network.io.NetworkBuffer;
import com.cafiaso.server.network.protocol.Packet;
import com.cafiaso.server.network.protocol.PacketDeserializer;
import com.cafiaso.server.network.data.DataTypes;

import java.io.IOException;

/**
 * Sent by the client to request a pong response from the server.
 *
 * @param payload the payload sent by the client to be echoed back by the server
 */
@Packet(state = ConnectionState.STATUS, resourceId = "ping_request", deserializer = PingRequestStatusPacket.Deserializer.class)
public record PingRequestStatusPacket(long payload) {

    public static class Deserializer implements PacketDeserializer<PingRequestStatusPacket> {

        @Override
        public PingRequestStatusPacket deserialize(NetworkBuffer in) throws IOException {
            long payload = in.read(DataTypes.LONG);

            return new PingRequestStatusPacket(payload);
        }
    }
}
