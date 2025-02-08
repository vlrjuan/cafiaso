package org.cafiaso.server.network.packet.server.configuration;

import org.cafiaso.server.network.Serializer;
import org.cafiaso.server.network.packet.server.ServerPacket;
import org.cafiaso.server.network.stream.output.OutputStream;

import java.io.IOException;

/**
 * Packet sent by the server to send data in a channel.
 *
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Clientbound_Plugin_Message_(configuration)>Clientbound Plugin Message (configuration)</a>
 */
public class ClientBoundPluginMessagePacket extends ServerPacket {

    private final String channel;
    private final byte[] data;

    /**
     * ClientBoundPluginMessagePacket constructor.
     *
     * @param channel the channel to send the data to
     * @param data    the data to send
     */
    public ClientBoundPluginMessagePacket(String channel, byte[] data) {
        super(0x01);

        this.channel = channel;
        this.data = data;
    }

    @Override
    public void write(OutputStream out) throws IOException {
        out.write(Serializer.STRING, channel);
        out.write(Serializer.BYTE_ARRAY, data);
    }

    /**
     * Gets the channel to send the data to.
     *
     * @return the channel
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Gets the data to send.
     *
     * @return the data
     */
    public byte[] getData() {
        return data;
    }
}
