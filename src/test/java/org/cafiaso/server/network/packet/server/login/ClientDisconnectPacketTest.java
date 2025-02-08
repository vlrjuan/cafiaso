package org.cafiaso.server.network.packet.server.login;

import org.cafiaso.server.network.Serializer;
import org.cafiaso.server.network.stream.output.ByteArrayOutputStream;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.cafiaso.server.network.serializers.TextComponentSerializer.TextComponent;
import static org.junit.jupiter.api.Assertions.*;

class ClientDisconnectPacketTest {

    @Test
    void write_ShouldWritePacket() throws IOException {
        TextComponent reason = new TextComponent("reason");

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ClientDisconnectPacket packet = new ClientDisconnectPacket(reason);
            packet.write(out);

            byte[] expected = deserializeData(reason);
            byte[] actual = out.toByteArray();

            assertArrayEquals(expected, actual);
        }
    }

    private byte[] deserializeData(TextComponent reason) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            out.write(Serializer.TEXT_COMPONENT, reason);

            return out.toByteArray();
        }
    }
}
