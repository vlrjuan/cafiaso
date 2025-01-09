package org.cafiaso.server.network.packet.client.status;

import org.cafiaso.server.network.buffers.InputBuffer;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class StatusRequestPacketTest {

    @Test
    void read_ShouldReadPacket() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        DataInputStream dataIn = new DataInputStream(in);

        try (InputBuffer buffer = new InputBuffer(dataIn)) {
            assertDoesNotThrow(() -> {
                StatusRequestPacket packet = new StatusRequestPacket();
                packet.read(buffer);
            });
        }
    }
}
