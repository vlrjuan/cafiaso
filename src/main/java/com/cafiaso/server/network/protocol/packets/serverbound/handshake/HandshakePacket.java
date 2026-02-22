package com.cafiaso.server.network.protocol.packets.serverbound.handshake;

import com.cafiaso.server.network.connection.ConnectionState;
import com.cafiaso.server.network.io.NetworkBuffer;
import com.cafiaso.server.network.protocol.Packet;
import com.cafiaso.server.network.protocol.PacketDeserializer;
import com.cafiaso.server.network.data.DataTypes;

import java.io.IOException;
import java.util.function.Supplier;

/**
 * Sent by the client to initiate the connection (ping or login).
 *
 * @param protocolVersion the version that the client plans to connect to the server
 * @param serverAddress   the host id that was used to connect
 * @param serverPort      the port that was used to connect
 * @param intention       the next state that the client wants to switch to
 */
@Packet(state = ConnectionState.HANDSHAKE, resourceId = "intention", deserializer = HandshakePacket.Deserializer.class)
public record HandshakePacket(int protocolVersion, String serverAddress, int serverPort, Intention intention) {

    /**
     * Represents the next state that the client wants to transition to after the handshake.
     */
    public enum Intention implements Supplier<Integer> {

        /**
         * Switch to {@link ConnectionState#STATUS}
         */
        STATUS(1),
        /**
         * Switch to {@link ConnectionState#LOGIN}
         */
        LOGIN(2),
        /**
         * Not supported by the server.
         */
        TRANSFER(3);

        private final int id;

        Intention(int id) {
            this.id = id;
        }

        @Override
        public Integer get() {
            return id;
        }
    }

    public static class Deserializer implements PacketDeserializer<HandshakePacket> {

        @Override
        public HandshakePacket deserialize(NetworkBuffer in) throws IOException {
            int protocolVersion = in.read(DataTypes.VAR_INT);
            String serverAddress = in.read(DataTypes.STRING(255));
            int serverPort = in.read(DataTypes.UNSIGNED_SHORT);
            Intention intention = in.read(DataTypes.ENUM(Intention.class, DataTypes.VAR_INT));

            return new HandshakePacket(protocolVersion, serverAddress, serverPort, intention);
        }
    }
}
