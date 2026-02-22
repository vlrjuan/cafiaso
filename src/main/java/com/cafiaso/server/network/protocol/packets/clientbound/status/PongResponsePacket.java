package com.cafiaso.server.network.protocol.packets.clientbound.status;

import com.cafiaso.server.network.io.NetworkBuffer;
import com.cafiaso.server.network.protocol.SerializablePacket;
import com.cafiaso.server.network.data.DataTypes;

import java.io.IOException;

/**
 * Sent by the server in response to a {@link com.cafiaso.server.network.protocol.packets.serverbound.status.PingRequestStatusPacket}.
 *
 * @param payload the payload sent by the client in the ping request
 */
public record PongResponsePacket(long payload) implements SerializablePacket {

    @Override
    public String resourceId() {
        return "pong_response";
    }

    @Override
    public void serialize(NetworkBuffer out) throws IOException {
        out.write(DataTypes.LONG, payload);
    }
}
