package org.cafiaso.server.network.packet.client.configuration;

import org.cafiaso.server.network.Serializer;
import org.cafiaso.server.network.packet.client.ClientPacket;
import org.cafiaso.server.network.stream.input.InputStream;

import java.io.IOException;
import java.util.Arrays;

/**
 * Packet sent by the client to send data in a channel.
 *
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Serverbound_Plugin_Message_(configuration)>Serverbound Plugin Message (configuration)</a>
 */
public class ServerBoundPluginMessagePacket implements ClientPacket {

    private String channel;
    private byte[] data;

    @Override
    public void read(InputStream in) throws IOException {
        channel = in.read(Serializer.IDENTIFIER);
        data = in.read(Serializer.BYTE_ARRAY);
    }

    /**
     * Get the channel to send data to.
     *
     * @return the channel
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Get the data to send.
     *
     * @return the message data
     */
    public byte[] getData() {
        return data;
    }

    @Override
    public String toString() {
        return "ServerBoundPluginMessagePacket{" +
                "channel='" + channel + '\'' +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
