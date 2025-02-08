package org.cafiaso.server.network.packet.server.status;

import org.cafiaso.server.Server;
import org.cafiaso.server.configuration.ServerConfiguration;
import org.cafiaso.server.network.Serializer;
import org.cafiaso.server.network.stream.output.ByteArrayOutputStream;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class StatusResponsePacketTest {

    @Test
    void test_ShouldWritePacket() throws IOException {
        String jsonResponse = "{\"players\":{\"max\":%d,\"online\":%d},\"description\":{\"text\":\"%s\"},\"version\":{\"protocol\":%d,\"name\":\"%s\"}}"
                .formatted(ServerConfiguration.DEFAULT_MAX_PLAYERS, 5, ServerConfiguration.DEFAULT_DESCRIPTION, Server.PROTOCOL_VERSION, Server.MINECRAFT_VERSION);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            StatusResponsePacket packet = new StatusResponsePacket(jsonResponse);
            packet.write(out);

            byte[] expected = deserializeData(jsonResponse);
            byte[] actual = out.toByteArray();

            assertArrayEquals(expected, actual);
        }
    }

    private byte[] deserializeData(String jsonResponse) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            out.write(Serializer.STRING, jsonResponse);

            return out.toByteArray();
        }
    }
}
