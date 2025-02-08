package org.cafiaso.server.network.packet.server.status;

import org.cafiaso.server.network.Serializer;
import org.cafiaso.server.network.stream.output.ByteArrayOutputStream;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PongResponsePacketTest {

    @Test
    void write_ShouldWritePacket() throws IOException {
        long payload = 123L;

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PongResponsePacket packet = new PongResponsePacket(payload);
            packet.write(out);

            byte[] expected = deserializeData(payload);
            byte[] actual = out.toByteArray();

            assertArrayEquals(expected, actual);
        }
    }

    private byte[] deserializeData(long payload) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            out.write(Serializer.LONG, payload);

            return out.toByteArray();
        }
    }
}
