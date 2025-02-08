package org.cafiaso.server.network.packet.client.handshake;

import org.cafiaso.server.network.Serializer;
import org.cafiaso.server.network.packet.client.ClientPacket;
import org.cafiaso.server.network.stream.input.InputStream;

import java.io.IOException;

/**
 * Packet sent by the client to initiate the connection (ping or login).
 *
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Handshake">Handshake Packet</a>
 */
public class HandshakePacket implements ClientPacket {

    public static final int MAX_SERVER_ADDRESS_LENGTH = 255;

    private static final Serializer<String> SERVER_ADDRESS_SERIALIZER = Serializer.STRING(MAX_SERVER_ADDRESS_LENGTH);
    private static final Serializer<Intent> INTENT_SERIALIZER = Serializer.ENUM(HandshakePacket.Intent.class, Serializer.VAR_INT, HandshakePacket.Intent::getId);

    private int protocolVersion;
    private String serverAddress;
    private int serverPort;
    private Intent intent;

    @Override
    public void read(InputStream in) throws IOException {
        protocolVersion = in.read(Serializer.VAR_INT);
        serverAddress = in.read(SERVER_ADDRESS_SERIALIZER);
        serverPort = in.read(Serializer.UNSIGNED_SHORT);
        intent = in.read(INTENT_SERIALIZER);
    }

    /**
     * Gets the protocol version.
     * <p>
     * If the protocol version is different from the server's protocol version, the connection will be closed.
     *
     * @return the protocol version
     */
    public int getProtocolVersion() {
        return protocolVersion;
    }

    /**
     * Gets the server address that the client wants to connect to.
     *
     * @return the server address
     */
    public String getServerAddress() {
        return serverAddress;
    }

    /**
     * Gets the server port that the client wants to connect to.
     *
     * @return the server port
     */
    public int getServerPort() {
        return serverPort;
    }

    /**
     * Gets the next state that the client wants to switch to.
     *
     * @return the next state
     */
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

    /**
     * Represents the next state that the client wants to switch to.
     */
    public enum Intent {
        /**
         * Set by the client during the server list ping process.
         */
        STATUS(1),
        /**
         * Set by the client during the login process.
         */
        LOGIN(2),
        /**
         * Not supported by the server.
         */
        TRANSFER(3);

        private final int id;

        Intent(int id) {
            this.id = id;
        }

        /**
         * Gets the ID of the intent.
         *
         * @return the intent ID
         */
        public int getId() {
            return id;
        }
    }
}
