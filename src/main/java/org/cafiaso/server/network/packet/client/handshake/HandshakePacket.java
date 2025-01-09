package org.cafiaso.server.network.packet.client.handshake;

import org.cafiaso.server.network.DataType;
import org.cafiaso.server.network.buffers.InputBuffer;
import org.cafiaso.server.network.packet.client.ClientPacket;

import java.io.IOException;

public class HandshakePacket implements ClientPacket {

    public static final int MAX_SERVER_ADDRESS_LENGTH = 255;

    private static final DataType<Intent> INTENT_DATA_TYPE = DataType.ENUM(HandshakePacket.Intent.class, DataType.VAR_INT, HandshakePacket.Intent::getId);

    private int protocolVersion;
    private String serverAddress;
    private int serverPort;
    private Intent intent;

    @Override
    public void read(InputBuffer buffer) throws IOException {
        protocolVersion = buffer.read(DataType.VAR_INT);
        serverAddress = buffer.read(DataType.STRING);
        if (serverAddress.length() > MAX_SERVER_ADDRESS_LENGTH) {
            throw new IOException("Server address is too long");
        }
        serverPort = buffer.read(DataType.UNSIGNED_SHORT);
        intent = buffer.read(INTENT_DATA_TYPE);
    }

    public int getProtocolVersion() {
        return protocolVersion;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public int getServerPort() {
        return serverPort;
    }

    public Intent getNextState() {
        return intent;
    }

    @Override
    public String toString() {
        return "HandshakePacket{" +
                "protocolVersion=" + protocolVersion +
                ", serverAddress='" + serverAddress + '\'' +
                ", serverPort=" + serverPort +
                ", intent=" + intent +
                '}';
    }

    public enum Intent {
        STATUS(1),
        LOGIN(2),
        TRANSFER(3);

        private final int id;

        Intent(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}
