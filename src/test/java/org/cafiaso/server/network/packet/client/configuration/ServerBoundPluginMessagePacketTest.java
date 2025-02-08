package org.cafiaso.server.network.packet.client.configuration;

import org.cafiaso.server.network.Serializer;
import org.cafiaso.server.network.stream.input.ByteArrayInputStream;
import org.cafiaso.server.network.stream.input.InputStream;
import org.cafiaso.server.network.stream.output.ByteArrayOutputStream;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ServerBoundPluginMessagePacketTest {

    @Test
    void read_ShouldReadPacket() throws IOException {
        String channel = "minecraft:brand";
        byte[] data = {0x01, 0x02, 0x03, 0x04};

        byte[] serializedData = serializeData(channel, data);

        InputStream in = new ByteArrayInputStream(serializedData);

        ServerBoundPluginMessagePacket packet = new ServerBoundPluginMessagePacket();
        packet.read(in);

        assertEquals(channel, packet.getChannel());
        assertArrayEquals(data, packet.getData());
    }

    private byte[] serializeData(String channel, byte[] data) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            out.write(Serializer.STRING(16), channel);
            out.write(Serializer.BYTE_ARRAY, data);

            return out.toByteArray();
        }
    }
}
