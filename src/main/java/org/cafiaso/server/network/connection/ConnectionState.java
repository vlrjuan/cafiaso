package org.cafiaso.server.network.connection;

import org.cafiaso.server.network.handlers.handshake.HandshakePacketHandler;
import org.cafiaso.server.network.ClientPacket;
import org.cafiaso.server.network.PacketHandler;
import org.cafiaso.server.network.handlers.handshake.LegacyServerListPingPacketHandler;
import org.cafiaso.server.network.handlers.status.PingRequestHandler;
import org.cafiaso.server.network.handlers.status.StatusRequestHandler;
import org.cafiaso.server.network.packets.client.handshake.HandshakePacket;
import org.cafiaso.server.network.packets.client.handshake.LegacyServerListPingPacket;
import org.cafiaso.server.network.packets.client.status.PingRequestPacket;
import org.cafiaso.server.network.packets.client.status.StatusRequestPacket;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Represents a connection state.
 * <p>
 * Each connection state has a set of {@link ClientPacket} that can be sent by the client at that state.
 */
public enum ConnectionState {

    HANDSHAKE(
            new Packet<>(0x00, HandshakePacket.class, new HandshakePacketHandler()),
            new Packet<>(0xFE, LegacyServerListPingPacket.class, new LegacyServerListPingPacketHandler())
    ),
    STATUS(
            new Packet<>(0x00, StatusRequestPacket.class, new StatusRequestHandler()),
            new Packet<>(0x01, PingRequestPacket.class, new PingRequestHandler())
    ),
    LOGIN(),
    CONFIGURATION(),
    PLAY();

    private final Map<Integer, Packet<?>> packets;

    /**
     * ConnectionState constructor.
     *
     * @param packetEntries the packet entries
     */
    ConnectionState(Packet<?>... packetEntries) {
        packets = Arrays.stream(packetEntries)
                .collect(Collectors.toMap(Packet::id, Function.identity()));
    }

    /**
     * Gets the packet with the specified ID and its handler for this connection state, if it is registered.
     * <p>
     * The packet is automatically instantiated using its default constructor.
     *
     * @param id  the packet ID
     * @param <P> the packet type
     * @return the packet entry, or {@link Optional#empty()} if the packet is not registered
     */
    @SuppressWarnings("unchecked")
    public <P extends ClientPacket> Optional<PacketEntry<P>> getPacketById(int id) {
        Packet<P> packet = (Packet<P>) packets.get(id);

        if (packet == null) {
            return Optional.empty();
        }

        try {
            P instance = packet.clazz.getConstructor().newInstance();
            PacketHandler<P> handler = packet.handler();

            return Optional.of(new PacketEntry<>(instance, handler));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException("Failed to create packet instance", e);
        }
    }

    /**
     * Represents a pair of a packet and its handler.
     *
     * @param <P> the packet type
     */
    public record PacketEntry<P extends ClientPacket>(P packet, PacketHandler<P> handler) {

    }

    /**
     * Represents a packet class with its ID and handler.
     *
     * @param <P> the packet type
     */
    private record Packet<P extends ClientPacket>(int id, Class<P> clazz, PacketHandler<P> handler) {

    }
}
