package org.cafiaso.server.network.packet.client.handshake;

import org.cafiaso.server.Server;
import org.cafiaso.server.network.Serializer;
import org.cafiaso.server.network.stream.input.ByteArrayInputStream;
import org.cafiaso.server.network.stream.input.InputStream;
import org.cafiaso.server.network.stream.output.ByteArrayOutputStream;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.cafiaso.server.network.packet.client.handshake.HandshakePacket.MAX_SERVER_ADDRESS_LENGTH;
import static org.junit.jupiter.api.Assertions.*;

class HandshakePacketTest {

    private static final Serializer<HandshakePacket.Intent> INTENT_SERIALIZER = Serializer.ENUM(HandshakePacket.Intent.class, Serializer.VAR_INT, HandshakePacket.Intent::getId);

    @Test
    void read_ShouldReadPacket() throws IOException {
        int protocolVersion = Server.PROTOCOL_VERSION;
        String serverAddress = "mc.cafiaso.org";
        int serverPort = 25565;
        HandshakePacket.Intent nextState = HandshakePacket.Intent.STATUS;

        byte[] data = serializeData(protocolVersion, serverAddress, serverPort, nextState);

        try (InputStream in = new ByteArrayInputStream(data)) {
            HandshakePacket packet = new HandshakePacket();
            packet.read(in);

            assertEquals(protocolVersion, packet.getProtocolVersion());
            assertEquals(serverAddress, packet.getServerAddress());
            assertEquals(serverPort, packet.getServerPort());
            assertEquals(nextState, packet.getNextState());
        }
    }

    @Test
    void read_ShouldThrowException_WhenAddressIsTooLong() throws IOException {
        int protocolVersion = Server.PROTOCOL_VERSION;
        String serverAddress = "0".repeat(MAX_SERVER_ADDRESS_LENGTH + 1);
        int serverPort = 25565;
        HandshakePacket.Intent nextState = HandshakePacket.Intent.STATUS;

        byte[] data = serializeData(protocolVersion, serverAddress, serverPort, nextState);

        try (InputStream in = new ByteArrayInputStream(data)) {
            HandshakePacket packet = new HandshakePacket();

            assertThrowsExactly(IOException.class, () -> packet.read(in));
        }
    }

    private byte[] serializeData(int protocolVersion, String serverAddress, int serverPort, HandshakePacket.Intent nextState) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            out.write(Serializer.VAR_INT, protocolVersion);
            out.write(Serializer.STRING, serverAddress);
            out.write(Serializer.UNSIGNED_SHORT, serverPort);
            out.write(INTENT_SERIALIZER, nextState);

            return out.toByteArray();
        }
    }
}
