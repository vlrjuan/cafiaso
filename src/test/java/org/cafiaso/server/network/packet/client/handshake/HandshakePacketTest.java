package org.cafiaso.server.network.packet.client.handshake;

import org.cafiaso.server.Server;
import org.cafiaso.server.network.DataType;
import org.cafiaso.server.network.buffers.InputBuffer;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static org.cafiaso.server.network.packet.client.handshake.HandshakePacket.MAX_SERVER_ADDRESS_LENGTH;
import static org.junit.jupiter.api.Assertions.*;

class HandshakePacketTest {

    // Data
    private static final int PROTOCOL_VERSION = Server.PROTOCOL_VERSION;
    private static final String SERVER_ADDRESS = "mc.cafiaso.org";
    private static final int SERVER_PORT = 25565;
    private static final HandshakePacket.Intent NEXT_STATE = HandshakePacket.Intent.STATUS;

    // Data types
    private static final DataType<Integer> VAR_INT_DATA_TYPE = DataType.VAR_INT;
    private static final DataType<String> STRING_DATA_TYPE = DataType.STRING;
    private static final DataType<Integer> UNSIGNED_SHORT_DATA_TYPE = DataType.UNSIGNED_SHORT;
    private static final DataType<HandshakePacket.Intent> INTENT_DATA_TYPE = DataType.ENUM(HandshakePacket.Intent.class, VAR_INT_DATA_TYPE, HandshakePacket.Intent::getId);

    @Test
    void read_ShouldReadPacket() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);

        VAR_INT_DATA_TYPE.write(dataOut, PROTOCOL_VERSION);
        STRING_DATA_TYPE.write(dataOut, SERVER_ADDRESS);
        UNSIGNED_SHORT_DATA_TYPE.write(dataOut, SERVER_PORT);
        INTENT_DATA_TYPE.write(dataOut, NEXT_STATE);

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        DataInputStream dataIn = new DataInputStream(in);

        try (InputBuffer buffer = new InputBuffer(dataIn)) {
            HandshakePacket packet = new HandshakePacket();
            packet.read(buffer);

            assertEquals(PROTOCOL_VERSION, packet.getProtocolVersion());
            assertEquals(SERVER_ADDRESS, packet.getServerAddress());
            assertEquals(SERVER_PORT, packet.getServerPort());
            assertEquals(NEXT_STATE, packet.getNextState());
        }
    }

    @Test
    void read_ShouldRaiseException_WhenAddressIsTooLong() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);

        VAR_INT_DATA_TYPE.write(dataOut, PROTOCOL_VERSION);
        STRING_DATA_TYPE.write(dataOut, "0".repeat(MAX_SERVER_ADDRESS_LENGTH + 1));
        UNSIGNED_SHORT_DATA_TYPE.write(dataOut, SERVER_PORT);
        INTENT_DATA_TYPE.write(dataOut, NEXT_STATE);

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        DataInputStream dataIn = new DataInputStream(in);

        try (InputBuffer buffer = new InputBuffer(dataIn)) {
            assertThrowsExactly(IOException.class, () -> new HandshakePacket().read(buffer),
                    "String is too long. Waiting for maximum %d characters, received %d."
                            .formatted(MAX_SERVER_ADDRESS_LENGTH, MAX_SERVER_ADDRESS_LENGTH + 1)
            );
        }
    }
}
