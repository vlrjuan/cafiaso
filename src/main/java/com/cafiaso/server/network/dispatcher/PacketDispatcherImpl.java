package com.cafiaso.server.network.dispatcher;

import com.cafiaso.server.network.connection.Connection;
import com.cafiaso.server.network.io.NetworkBuffer;
import com.cafiaso.server.network.io.NetworkByteBuffer;
import com.cafiaso.server.network.protocol.SerializablePacket;
import com.cafiaso.server.network.registry.PacketRegistry;
import com.cafiaso.server.network.data.DataTypes;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * {@link PacketDispatcher} implementation that serializes the packet body (ID and data),
 * computes the packet length, encrypts the packet if necessary, and sends it to the client.
 */
public class PacketDispatcherImpl implements PacketDispatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(PacketDispatcherImpl.class);

    private final PacketRegistry registry;

    @Inject
    public PacketDispatcherImpl(PacketRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void dispatch(Connection connection, SerializablePacket packet) throws IOException {
        int packetId = registry.getPacketId(connection.getState(), packet.resourceId())
                .orElseThrow(() -> new IOException("Unknown packet resource ID: %s".formatted(packet.resourceId())));

        NetworkBuffer bodyBuffer = new NetworkByteBuffer(); // Holds the packet body (ID and data) (unencrypted)
        bodyBuffer.write(DataTypes.VAR_INT, packetId); // Write the packet ID
        packet.serialize(bodyBuffer); // Write the packet data (serialize the packet into the buffer)

        int packetLength = bodyBuffer.getPosition();

        NetworkBuffer packetBuffer = new NetworkByteBuffer(); // Holds the packet (length and body) (unencrypted)
        packetBuffer.write(DataTypes.VAR_INT, packetLength); // Write the packet length
        packetBuffer.write(bodyBuffer); // Write the packet body

        // Prepare the buffer for sending
        packetBuffer.flip();

        // Send the packet to the client
        connection.write(packetBuffer);

        LOGGER.debug("Sent packet {} to {}", packet, connection);
    }
}
