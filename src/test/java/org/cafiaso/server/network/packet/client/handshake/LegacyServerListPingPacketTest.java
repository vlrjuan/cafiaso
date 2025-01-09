package org.cafiaso.server.network.packet.client.handshake;

import org.cafiaso.server.network.DataType;
import org.cafiaso.server.network.buffers.InputBuffer;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class LegacyServerListPingPacketTest {

    // Data
    private static final int PAYLOAD = 1;

    // Data types
    private static final DataType<Integer> VAR_INT_DATA_TYPE = DataType.VAR_INT;

    @Test
    void read_ShouldReadPacket() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);

        VAR_INT_DATA_TYPE.write(dataOut, PAYLOAD);

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        DataInputStream dataIn = new DataInputStream(in);

        try (InputBuffer buffer = new InputBuffer(dataIn)) {
            LegacyServerListPingPacket packet = new LegacyServerListPingPacket();
            packet.read(buffer);

            assertEquals(PAYLOAD, packet.getPayload());
        }
    }
}
