package com.cafiaso.server.network.protocol.packets.clientbound.status;

import com.cafiaso.server.network.io.NetworkBuffer;
import com.cafiaso.server.network.protocol.SerializablePacket;
import com.cafiaso.server.network.data.DataTypes;

import java.io.IOException;

/**
 * Sent by the server in response to a {@link com.cafiaso.server.network.protocol.packets.serverbound.status.StatusRequestPacket}.
 * <p>
 * Example JSON response:
 * <pre>
 * {
 *   "version": {
 *     "id": "1.21.11",
 *     "protocol": 774
 *   },
 *   "players": {
 *     "max": 20,
 *     "online": 5,
 *     "sample": [
 *       {
 *         "id": "Choukas",
 *         "uuid": "123e4567-e89b-12d3-a456-426614174000"
 *       }
 *     ]
 *   },
 *   "description": {
 *     "text": "A Minecraft Server"
 *   },
 *   "favicon": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUA"
 * }
 * </pre>
 *
 * @param jsonResponse a JSON object containing server information
 */
public record StatusResponsePacket(String jsonResponse) implements SerializablePacket {

    @Override
    public String resourceId() {
        return "status_response";
    }

    @Override
    public void serialize(NetworkBuffer out) throws IOException {
        out.write(DataTypes.STRING, jsonResponse);
    }
}
