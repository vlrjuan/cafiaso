package org.cafiaso.server.network.packet.client.handshake;

import org.cafiaso.server.network.Serializer;
import org.cafiaso.server.network.packet.client.ClientPacket;
import org.cafiaso.server.network.stream.input.InputStream;

import java.io.IOException;

/**
 * Packet sent by legacy clients to ping the server.
 * <p>
 * This packet is intentionally not fully implemented.
 *
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Legacy_Server_List_Ping">Legacy Server List Ping Packet</a>
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Server_List_Ping#1.6">Legacy Server List Ping (1.6 and older)</a>
 */
public class LegacyServerListPingPacket implements ClientPacket {

    private int payload;

    @Override
    public void read(InputStream in) throws IOException {
        payload = in.read(Serializer.VAR_INT);
    }

    /**
     * Gets the payload of the packet.
     * <p>
     * The server will not try to parse the payload and will just kick the client.
     *
     * @return the payload
     */
    public int getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "LegacyServerListPingPacket{" +
                "payload=" + payload +
                '}';
    }
}
