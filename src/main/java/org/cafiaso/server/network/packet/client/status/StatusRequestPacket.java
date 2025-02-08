package org.cafiaso.server.network.packet.client.status;

import org.cafiaso.server.network.packet.client.ClientPacket;
import org.cafiaso.server.network.stream.input.InputStream;

import java.io.IOException;

/**
 * Packet sent by the client to request the server status (MOTD, player count, etc.).
 *
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Status_Request">Status Request Packet</a>
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Server_List_Ping#Status_Request">Status Request</a>
 */
public class StatusRequestPacket implements ClientPacket {

    @Override
    public void read(InputStream in) throws IOException {
        // No data to read
    }

    @Override
    public String toString() {
        return "StatusRequestPacket{}";
    }
}
