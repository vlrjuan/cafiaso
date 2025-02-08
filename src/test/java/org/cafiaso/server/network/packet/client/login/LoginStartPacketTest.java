package org.cafiaso.server.network.packet.client.login;

import org.cafiaso.server.network.Serializer;
import org.cafiaso.server.network.stream.input.ByteArrayInputStream;
import org.cafiaso.server.network.stream.input.InputStream;
import org.cafiaso.server.network.stream.output.ByteArrayOutputStream;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LoginStartPacketTest {

    @Test
    void read_ShouldReadPacket() throws IOException {
        String username = "Choukas";
        UUID uuid = UUID.randomUUID();

        byte[] data = serializeData(username, uuid);

        try (InputStream in = new ByteArrayInputStream(data)) {
            LoginStartPacket packet = new LoginStartPacket();
            packet.read(in);

            assertEquals(username, packet.getUsername());
            assertEquals(uuid, packet.getUuid());
        }
    }

    private byte[] serializeData(String username, UUID uuid) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            out.write(Serializer.STRING, username);
            out.write(Serializer.UUID, uuid);

            return out.toByteArray();
        }
    }
}
