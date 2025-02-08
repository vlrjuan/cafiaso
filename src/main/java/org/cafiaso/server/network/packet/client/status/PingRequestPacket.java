package org.cafiaso.server.network.packet.client.status;

import org.cafiaso.server.network.Serializer;
import org.cafiaso.server.network.packet.client.ClientPacket;
import org.cafiaso.server.network.stream.input.InputStream;

import java.io.IOException;

/**
 * Packet sent by the client to request a pong response from the server.
 *
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Ping_Request_(status)">Ping Request Packet</a>
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Server_List_Ping#Ping_Request">Ping Request</a>
 */
public class PingRequestPacket implements ClientPacket {

    private long payload;

    @Override
    public void read(InputStream in) throws IOException {
        payload = in.read(Serializer.LONG);
    }

    /**
     * Gets the payload of the packet.
     * <p>
     * It may be any value, Notchian clients use the time of the request.
     * <p>
     * The server will respond with the same payload.
     *
     * @return the payload
     */
    public long getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "PingRequestPacket{" +
                "payload=" + payload +
                '}';
    }
}
