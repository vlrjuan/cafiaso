package org.cafiaso.server.network.packet.client.status;

import org.cafiaso.server.network.Serializer;
import org.cafiaso.server.network.stream.input.ByteArrayInputStream;
import org.cafiaso.server.network.stream.input.InputStream;
import org.cafiaso.server.network.stream.output.ByteArrayOutputStream;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PingRequestPacketTest {

    @Test
    void read_ShouldReadPacket() throws IOException {
        long payload = 72623859790382856L;

        byte[] data = serializeData(payload);

        try (InputStream in = new ByteArrayInputStream(data)) {
            PingRequestPacket packet = new PingRequestPacket();
            packet.read(in);

            assertEquals(payload, packet.getPayload());
        }
    }

    private byte[] serializeData(long payload) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            out.write(Serializer.LONG, payload);

            return out.toByteArray();
        }
    }
}
