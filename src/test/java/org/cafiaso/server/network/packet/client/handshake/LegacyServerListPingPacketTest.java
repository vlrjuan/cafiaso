package org.cafiaso.server.network.packet.client.handshake;

import org.cafiaso.server.network.Serializer;
import org.cafiaso.server.network.stream.input.ByteArrayInputStream;
import org.cafiaso.server.network.stream.input.InputStream;
import org.cafiaso.server.network.stream.output.ByteArrayOutputStream;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class LegacyServerListPingPacketTest {

    @Test
    void read_ShouldReadPacket() throws IOException {
        int payload = 1;

        byte[] data = serializeData(payload);

        try (InputStream in = new ByteArrayInputStream(data)) {
            LegacyServerListPingPacket packet = new LegacyServerListPingPacket();
            packet.read(in);

            assertEquals(payload, packet.getPayload());
        }
    }

    private byte[] serializeData(int payload) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            out.write(Serializer.VAR_INT, payload);

            return out.toByteArray();
        }
    }
}
