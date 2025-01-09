package org.cafiaso.server.network.packet.server;

import org.cafiaso.server.Server;
import org.cafiaso.server.configuration.ServerConfiguration;
import org.cafiaso.server.network.DataType;
import org.cafiaso.server.network.buffers.OutputBuffer;
import org.cafiaso.server.network.packet.server.status.StatusResponsePacket;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class StatusResponsePacketTest {

    // Data
    private static final String JSON_RESPONSE = "{\"players\":{\"max\":%d,\"online\":%d},\"description\":{\"text\":\"%s\"},\"version\":{\"protocol\":%d,\"name\":\"%s\"}}"
            .formatted(ServerConfiguration.DEFAULT_MAXIMUM_PLAYERS, 0, ServerConfiguration.DEFAULT_DESCRIPTION, Server.PROTOCOL_VERSION, Server.VERSION_NAME);

    // Data types
    private static final DataType<String> STRING_DATA_TYPE = DataType.STRING;

    @Test
    void test_ShouldWritePacket() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);

        try (OutputBuffer buffer = new OutputBuffer(dataOut)) {
            StatusResponsePacket packet = new StatusResponsePacket(JSON_RESPONSE);
            packet.write(buffer);
        }

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        DataInputStream dataIn = new DataInputStream(in);

        String jsonResponse = STRING_DATA_TYPE.read(dataIn);
        assertEquals(JSON_RESPONSE, jsonResponse);
    }
}
