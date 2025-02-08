package org.cafiaso.server.network.packet.server.status;

import org.cafiaso.server.network.Serializer;
import org.cafiaso.server.network.packet.server.ServerPacket;
import org.cafiaso.server.network.stream.output.OutputStream;

import java.io.IOException;

/**
 * Packet sent by the server in response to a {@link org.cafiaso.server.network.packet.client.status.PingRequestPacket}.
 *
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Pong_Response_(status)">Pong Response Packet</a>
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Server_List_Ping#Pong_Response">Pong response</a>
 */
public class PongResponsePacket extends ServerPacket {

    private final long payload;

    /**
     * PingResponsePacket constructor.
     *
     * @param payload the payload of the client's request
     */
    public PongResponsePacket(long payload) {
        super(0x01);

        this.payload = payload;
    }

    @Override
    public void write(OutputStream out) throws IOException {
        out.write(Serializer.LONG, this.payload);
    }

    /**
     * Gets the payload of the packet.
     * <p>
     * It is the same value as the payload of the client's request.
     *
     * @return the payload
     */
    public long getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "PingResponsePacket{" +
                "payload=" + payload +
                '}';
    }
}
