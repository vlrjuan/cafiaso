package org.cafiaso.server.network.packet.client.login;

import org.cafiaso.server.network.packet.client.ClientPacket;
import org.cafiaso.server.network.stream.input.InputStream;

import java.io.IOException;

/**
 * Packet sent by the client to acknowledge the login process.
 *
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Login_Acknowledged">Login Acknowledged Packet</a>
 */
public class LoginAcknowledgedPacket implements ClientPacket {

    @Override
    public void read(InputStream in) throws IOException {
        // No data to read
    }

    @Override
    public String toString() {
        return "LoginAcknowledgedPacket{}";
    }
}
