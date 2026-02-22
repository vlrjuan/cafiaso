package com.cafiaso.server.network.processor;

import com.cafiaso.server.network.connection.Connection;
import com.cafiaso.server.network.io.NetworkBuffer;
import com.cafiaso.server.network.io.NetworkByteBuffer;
import com.cafiaso.server.network.protocol.PacketHandler;
import com.cafiaso.server.network.protocol.PacketDeserializer;
import com.cafiaso.server.network.registry.PacketBinding;
import com.cafiaso.server.network.registry.PacketRegistry;
import com.cafiaso.server.network.data.DataTypes;
import com.google.inject.assistedinject.Assisted;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.util.Optional;

/**
 * A {@link PacketProcessor} implementation that reads incoming data from the connection,
 * deserializes packets, and dispatches them to their respective handlers.
 * <p>
 * This class handles incomplete packets by buffering data until a full packet is available.
 * It also logs unknown packets and any remaining bytes after packet deserialization.
 * <p>
 * A {@code PacketProcessorImpl} instance is tied to a specific {@link Connection}.
 */
public class PacketProcessorImpl implements PacketProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(PacketProcessorImpl.class);

    public static final int LEGACY_PACKET_ID = 0xFE;

    private final PacketRegistry packetRegistry;

    private final Connection connection;
    private final NetworkBuffer buffer = new NetworkByteBuffer();

    @Inject
    public PacketProcessorImpl(PacketRegistry packetRegistry, @Assisted Connection connection) {
        this.packetRegistry = packetRegistry;
        this.connection = connection;
    }

    @Override
    public void process() throws IOException {
        if (!connection.isOpen()) {
            throw new IllegalStateException("Connection is closed");
        }

        int bytesRead = connection.read(buffer);

        if (bytesRead == -1) {
            // No new data read, nothing to process
            return;
        }

        // Switch to reading mode
        buffer.flip();

        while (buffer.hasRemainingBytes()) {
            buffer.mark();

            int packetLength;

            try {
                packetLength = buffer.read(DataTypes.VAR_INT);
            } catch (IOException | BufferUnderflowException e) {
                LOGGER.debug("Failed to read packet length", e);

                buffer.reset();
                buffer.compact();

                return;
            }

            if (packetLength == LEGACY_PACKET_ID) {
                // Legacy packet
                LOGGER.debug("Received legacy server list ping from {}", connection);

                return;
            }

            int remainingBytes = buffer.getRemainingBytes();
            if (remainingBytes < packetLength) {
                LOGGER.debug("Incomplete packet: expected {} bytes, but only {} available", packetLength, remainingBytes);

                buffer.reset();
                buffer.compact();

                return;
            }

            NetworkBuffer packetBodyBuffer = buffer.copy(packetLength);

            int packetId = packetBodyBuffer.read(DataTypes.VAR_INT);

            Optional<PacketBinding<Object>> optionalPacket = packetRegistry.getPacket(connection.getState(), packetId);

            if (optionalPacket.isEmpty()) {
                LOGGER.warn("Unknown packet id {} in state {} from {}", Integer.toHexString(packetId), connection.getState(), connection);
            } else {
                PacketBinding<Object> packet = optionalPacket.get();

                PacketDeserializer<Object> deserializer = packet.getDeserializer();
                Object packetInstance = deserializer.deserialize(packetBodyBuffer);

                LOGGER.debug("Received packet {} from {}", packetInstance, connection);

                remainingBytes = packetBodyBuffer.getRemainingBytes();
                if (remainingBytes > 0) {
                    LOGGER.warn("{} bytes remaining after reading packet {}. They will be lost", remainingBytes, packetInstance);
                }

                PacketHandler<Object> handler = packet.getHandler();
                handler.handle(connection, packetInstance);
            }
        }

        buffer.clear();
    }
}
