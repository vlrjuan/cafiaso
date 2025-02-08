package org.cafiaso.server.network.packet.server.login;

import org.cafiaso.server.mojang.PlayerProfile.Property;
import org.cafiaso.server.network.Serializer;
import org.cafiaso.server.network.stream.output.ByteArrayOutputStream;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LoginSuccessPacketTest {

    @Test
    void test_ShouldWritePacket() throws IOException {
        UUID uuid = UUID.randomUUID();
        String username = "username";
        Property[] properties = new Property[0];

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            LoginSuccessPacket packet = new LoginSuccessPacket(uuid, username, properties);
            packet.write(out);

            byte[] expected = deserializeData(uuid, username, properties);
            byte[] actual = out.toByteArray();

            assertArrayEquals(expected, actual);
        }
    }

    private byte[] deserializeData(UUID uuid, String username, Property[] properties) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            out.write(Serializer.UUID, uuid);
            out.write(Serializer.STRING, username);
            out.write(Serializer.VAR_INT, properties.length);

            return out.toByteArray();
        }
    }
}
