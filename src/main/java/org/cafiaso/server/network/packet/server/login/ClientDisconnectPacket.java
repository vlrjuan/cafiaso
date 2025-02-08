package org.cafiaso.server.network.packet.server.login;

import org.cafiaso.server.network.Serializer;
import org.cafiaso.server.network.packet.server.ServerPacket;
import org.cafiaso.server.network.serializers.TextComponentSerializer.TextComponent;
import org.cafiaso.server.network.stream.output.OutputStream;

import java.io.IOException;

/**
 * Packet sent by the server to disconnect the client.
 *
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Disconnect_(login)">Disconnect Packet</a>
 */
public class ClientDisconnectPacket extends ServerPacket {

    private final TextComponent reason;

    /**
     * ClientDisconnectPacket constructor.
     *
     * @param reason the reason for the disconnection
     */
    public ClientDisconnectPacket(TextComponent reason) {
        super(0x00);

        this.reason = reason;
    }

    @Override
    public void write(OutputStream out) throws IOException {
        out.write(Serializer.TEXT_COMPONENT, reason);
    }

    @Override
    public String toString() {
        return "ClientDisconnectPacket{" +
                "reason='" + reason + '\'' +
                '}';
    }
}
