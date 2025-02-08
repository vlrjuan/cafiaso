package org.cafiaso.server.network.packet.server.login;

import org.cafiaso.server.network.Serializer;
import org.cafiaso.server.network.stream.output.ByteArrayOutputStream;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionRequestPacketTest {

    @Test
    void test_ShouldWritePacket() throws IOException {
        String serverId = "";
        byte[] publicKey = new byte[]{1, 2, 3, 4, 5};
        byte[] verifyToken = new byte[]{6, 7, 8, 9, 10};
        boolean shouldAuthenticate = true;

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            EncryptionRequestPacket packet = new EncryptionRequestPacket(serverId, publicKey, verifyToken, shouldAuthenticate);
            packet.write(out);

            byte[] expected = deserializeData(serverId, publicKey, verifyToken, shouldAuthenticate);
            byte[] actual = out.toByteArray();

            assertArrayEquals(expected, actual);
        }
    }

    private byte[] deserializeData(String serverId, byte[] publicKey, byte[] verifyToken, boolean shouldAuthenticate) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            out.write(Serializer.STRING, serverId);
            out.write(Serializer.PREFIXED_BYTE_ARRAY, publicKey);
            out.write(Serializer.PREFIXED_BYTE_ARRAY, verifyToken);
            out.write(Serializer.BOOLEAN, shouldAuthenticate);

            return out.toByteArray();
        }
    }
}
