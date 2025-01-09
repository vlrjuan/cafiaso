package org.cafiaso.server.network.connection;

import org.cafiaso.server.network.handler.PacketHandler;
import org.cafiaso.server.network.handler.handshake.HandshakePacketHandler;
import org.cafiaso.server.network.handler.handshake.LegacyServerListPingPacketHandler;
import org.cafiaso.server.network.handler.status.PingRequestHandler;
import org.cafiaso.server.network.handler.status.StatusRequestHandler;
import org.cafiaso.server.network.packet.client.ClientPacket;
import org.cafiaso.server.network.packet.client.handshake.HandshakePacket;
import org.cafiaso.server.network.packet.client.handshake.LegacyServerListPingPacket;
import org.cafiaso.server.network.packet.client.status.PingRequestPacket;
import org.cafiaso.server.network.packet.client.status.StatusRequestPacket;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Represents a connection state.
 * <p>
 * Each connection state has a set of {@link ClientPacket} that can be sent by the client at that state.
 */
public enum ConnectionState {

    HANDSHAKE(
            new Entry<>(0x00, HandshakePacket.class, new HandshakePacketHandler()),
            new Entry<>(0xFE, LegacyServerListPingPacket.class, new LegacyServerListPingPacketHandler())
    ),
    STATUS(
            new Entry<>(0x00, StatusRequestPacket.class, new StatusRequestHandler()),
            new Entry<>(0x01, PingRequestPacket.class, new PingRequestHandler())
    ),
    LOGIN(),
    CONFIGURATION(),
    PLAY();

    private final Map<Integer, Entry<?>> entries;

    /**
     * ConnectionState constructor.
     *
     * @param entries the packet entries
     */
    ConnectionState(Entry<?>... entries) {
        this.entries = Arrays.stream(entries)
                .collect(Collectors.toMap(Entry::id, Function.identity()));
    }

    /**
     * Gets the packet with the specified ID and its handler for this connection state, if it is registered.
     * <p>
     * The packet is automatically instantiated using its default constructor.
     *
     * @param <P> the packet type
     * @param id  the packet ID
     * @return the packet entry, or null if the packet is not registered
     */
    @SuppressWarnings("unchecked")
    public <P extends ClientPacket> PacketEntry<P> getPacketById(int id) {
        Entry<P> entry = (Entry<P>) entries.get(id);

        if (entry == null) {
            return null;
        }

        try {
            P instance = entry.clazz.getConstructor().newInstance();
            PacketHandler<P> handler = entry.handler();

            return new PacketEntry<>(instance, handler);
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
     * Maps a packet ID to its class and handler.
     *
     * @param <P> the packet type
     */
    private record Entry<P extends ClientPacket>(int id, Class<P> clazz, PacketHandler<P> handler) {

    }
}
