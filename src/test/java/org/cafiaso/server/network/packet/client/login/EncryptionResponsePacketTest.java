package org.cafiaso.server.network.packet.client.login;

import org.cafiaso.server.network.Serializer;
import org.cafiaso.server.network.stream.input.ByteArrayInputStream;
import org.cafiaso.server.network.stream.input.InputStream;
import org.cafiaso.server.network.stream.output.ByteArrayOutputStream;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionResponsePacketTest {

    @Test
    void read_ShouldReadPacket() throws IOException {
        byte[] sharedSecret = new byte[]{1, 2, 3, 4};
        byte[] verifyToken = new byte[]{5, 6, 7, 8};

        byte[] data = serializeData(sharedSecret, verifyToken);

        InputStream in = new ByteArrayInputStream(data);

        EncryptionResponsePacket packet = new EncryptionResponsePacket();
        packet.read(in);

        assertArrayEquals(sharedSecret, packet.getSharedSecret());
        assertArrayEquals(verifyToken, packet.getVerifyToken());
    }

    private byte[] serializeData(byte[] sharedSecret, byte[] verifyToken) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            out.write(Serializer.PREFIXED_BYTE_ARRAY, sharedSecret);
            out.write(Serializer.PREFIXED_BYTE_ARRAY, verifyToken);

            return out.toByteArray();
        }
    }
}
