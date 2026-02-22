package com.cafiaso.server.network.registry;

import com.cafiaso.server.network.connection.ConnectionState;
import com.cafiaso.server.network.protocol.PacketMappings;
import jakarta.inject.Inject;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Registry for all packets (clientbound and serverbound) handled by the server.
 * <p>
 * Packet ids often vary between different protocol versions, so this registry uses resource IDs
 * as a stable identifier for packets.
 * <p>
 * Mappings between packet IDs and resource IDs are provided by {@link PacketMappings} (typically
 * generated from the protocol definition files).
 *
 * @see PacketMappings
 */
public class PacketRegistry {

    private final PacketMappings mappings;
    private final Map<ConnectionState, Map<String, PacketBinding<Object>>> packets = new HashMap<>();

    @Inject
    public PacketRegistry(PacketMappings mappings, Set<PacketBinding<Object>> packets) {
        this.mappings = mappings;

        for (PacketBinding<Object> packet : packets) {
            this.packets.computeIfAbsent(packet.state(), _ -> new HashMap<>()).put(packet.resourceId(), packet);
        }
    }

    /**
     * Retrieves the packet binding for the given connection state and packet ID.
     *
     * @param state the connection state
     * @param id    the packet ID
     * @return an optional containing the packet binding if a packet is known by the Notchian client
     * and implemented by the server, or empty if not found
     */
    public Optional<PacketBinding<Object>> getPacket(ConnectionState state, int id) {
        String resourceId = mappings.serverbound().get(state).get(id);
        if (resourceId == null) {
            // The packet is unknown from Notchian client
            return Optional.empty();
        }

        Map<String, PacketBinding<Object>> statePackets = packets.get(state);
        if (statePackets == null) {
            // The packet is known from Notchian client but no packets are implemented for this state
            return Optional.empty();
        }

        PacketBinding<Object> registration = statePackets.get(resourceId);
        if (registration == null) {
            // The packet is known from Notchian client but not implemented yet
            return Optional.empty();
        }

        return Optional.of(registration);
    }

    /**
     * Retrieves the packet ID for the given connection state and packet resource ID.
     *
     * @param state      the connection state
     * @param resourceId the packet resource ID
     * @return an optional containing the packet ID if known by the Notchian client, or empty if not found
     */
    public Optional<Integer> getPacketId(ConnectionState state, String resourceId) {
        return Optional.ofNullable(mappings.clientbound().get(state).get(resourceId));
    }
}
