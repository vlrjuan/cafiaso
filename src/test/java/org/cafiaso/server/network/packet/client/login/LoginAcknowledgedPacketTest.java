package org.cafiaso.server.network.packet.client.login;

import org.cafiaso.server.network.stream.input.ByteArrayInputStream;
import org.cafiaso.server.network.stream.input.InputStream;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class LoginAcknowledgedPacketTest {

    @Test
    void read_ShouldReadPacket() throws IOException {
        try (InputStream in = ByteArrayInputStream.empty()) {
            LoginAcknowledgedPacket packet = new LoginAcknowledgedPacket();

            assertDoesNotThrow(() -> packet.read(in));
        }
    }
}
