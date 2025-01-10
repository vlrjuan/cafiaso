package org.cafiaso.server.network.packet.server;

import org.cafiaso.server.network.DataType;
import org.cafiaso.server.network.buffers.OutputBuffer;
import org.cafiaso.server.network.packet.server.status.PingResponsePacket;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PingResponsePacketTest {

    // Data
    private static final long PAYLOAD = 123L;

    // Data types
    private static final DataType<Long> LONG_DATA_TYPE = DataType.LONG;

    @Test
    void write_ShouldWritePacket() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);

        try (OutputBuffer buffer = new OutputBuffer(dataOut)) {
            PingResponsePacket packet = new PingResponsePacket(PAYLOAD);
            packet.write(buffer);
        }

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        DataInputStream dataIn = new DataInputStream(in);

        long payload = LONG_DATA_TYPE.read(dataIn);
        assertEquals(PAYLOAD, payload);
    }

    @Test
    void getLength_ShouldReturnCorrectLength() throws IOException {
        PingResponsePacket packet = new PingResponsePacket(PAYLOAD);

        assertEquals(0x01, packet.getId());
        assertEquals(9, packet.getLength());
    }
}
