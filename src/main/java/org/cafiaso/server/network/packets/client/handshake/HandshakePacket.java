package org.cafiaso.server.network.packets.client.handshake;

import org.cafiaso.server.network.DataType;
import org.cafiaso.server.network.ClientPacket;
import org.cafiaso.server.network.types.EnumDataType;

import java.io.DataInputStream;
import java.io.IOException;

public class HandshakePacket implements ClientPacket {

    private static final DataType<Intent> INTENT_DATA_TYPE = new EnumDataType<>(DataType.VAR_INT, Intent::fromId, Intent::getId);

    private int protocolVersion;
    private String serverAddress;
    private int serverPort;
    private Intent intent;

    @Override
    public void read(DataInputStream in) throws IOException {
        protocolVersion = DataType.VAR_INT.read(in);
        serverAddress = DataType.STRING.read(in);
        if (serverAddress.length() > 255) {
            throw new IOException("Server address is too long");
        }
        serverPort = DataType.UNSIGNED_SHORT.read(in);
        intent = INTENT_DATA_TYPE.read(in);
    }

    public int getProtocolVersion() {
        return protocolVersion;
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

        public static Intent fromId(int id) {
            for (Intent intent : values()) {
                if (intent.id == id) {
                    return intent;
                }
            }

            throw new IllegalArgumentException("Unknown intent id: " + id);
        }

        public int getId() {
            return id;
        }
    }
}
