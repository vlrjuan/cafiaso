package org.cafiaso.server.network.packet.server.status;

import org.cafiaso.server.network.Serializer;
import org.cafiaso.server.network.packet.server.ServerPacket;
import org.cafiaso.server.network.stream.output.OutputStream;

import java.io.IOException;

/**
 * Packet sent by the server in response to a {@link org.cafiaso.server.network.packet.client.status.StatusRequestPacket}.
 *
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Status_Response">Status Response Packet</a>
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Server_List_Ping#Status_Response">Status Response</a>
 */
public class StatusResponsePacket extends ServerPacket {

    private final String jsonResponse;

    /**
     * StatusResponsePacket constructor.
     *
     * @param jsonResponse the server status as a JSON object
     */
    public StatusResponsePacket(String jsonResponse) {
        super(0x00);

        this.jsonResponse = jsonResponse;
    }

    @Override
    public void write(OutputStream out) throws IOException {
        out.write(Serializer.STRING, this.jsonResponse);
    }

    /**
     * Gets the server status.
     * <p>
     * It is a JSON object containing server information.
     * <p>
     * Example:
     * <pre>
     * {
     *   "version": {
     *     "name": "1.21.4",
     *     "protocol": 769,
     *   },
     *   "players": {
     *     "max": 20,
     *     "online": 5,
     *   },
     *   "description": {
     *     "text": "A Minecraft Server",
     *   },
     *   "favicon": "data:image/png;base64,..."
     * }
     *
     * @return the JSON response
     */
    public String getJsonResponse() {
        return jsonResponse;
    }

    @Override
    public String toString() {
        return "StatusResponsePacket{" +
                "jsonResponse='" + jsonResponse + '\'' +
                '}';
    }
}
