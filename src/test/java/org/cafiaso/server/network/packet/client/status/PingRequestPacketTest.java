package org.cafiaso.server.network.packet.client.status;

import org.cafiaso.server.network.DataType;
import org.cafiaso.server.network.buffers.InputBuffer;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PingRequestPacketTest {

    // Data
    private static final long PAYLOAD = 72623859790382856L;

    // Data types
    private static final DataType<Long> LONG_DATA_TYPE = DataType.LONG;

    @Test
    void read_ShouldReadPacket() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);

        LONG_DATA_TYPE.write(dataOut, PAYLOAD);

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        DataInputStream dataIn = new DataInputStream(in);

        try (InputBuffer buffer = new InputBuffer(dataIn)) {
            PingRequestPacket packet = new PingRequestPacket();
            packet.read(buffer);

            assertEquals(PAYLOAD, packet.getPayload());
        }
    }
}
