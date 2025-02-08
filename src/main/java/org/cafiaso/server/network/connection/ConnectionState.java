package org.cafiaso.server.network.connection;

import org.cafiaso.server.network.handler.PacketHandler;
import org.cafiaso.server.network.handler.configuration.ClientInformationPacketHandler;
import org.cafiaso.server.network.handler.configuration.ServerBoundPluginMessagePacketHandler;
import org.cafiaso.server.network.handler.handshake.HandshakePacketHandler;
import org.cafiaso.server.network.handler.handshake.LegacyServerListPingPacketHandler;
import org.cafiaso.server.network.handler.login.EncryptionResponsePacketHandler;
import org.cafiaso.server.network.handler.login.LoginAcknowledgedPacketHandler;
import org.cafiaso.server.network.handler.login.LoginStartPacketHandler;
import org.cafiaso.server.network.handler.status.PingRequestPacketHandler;
import org.cafiaso.server.network.handler.status.StatusRequestPacketHandler;
import org.cafiaso.server.network.packet.client.ClientPacket;
import org.cafiaso.server.network.packet.client.configuration.ClientInformationPacket;
import org.cafiaso.server.network.packet.client.configuration.ServerBoundPluginMessagePacket;
import org.cafiaso.server.network.packet.client.handshake.HandshakePacket;
import org.cafiaso.server.network.packet.client.handshake.LegacyServerListPingPacket;
import org.cafiaso.server.network.packet.client.login.EncryptionResponsePacket;
import org.cafiaso.server.network.packet.client.login.LoginAcknowledgedPacket;
import org.cafiaso.server.network.packet.client.login.LoginStartPacket;
import org.cafiaso.server.network.packet.client.status.PingRequestPacket;
import org.cafiaso.server.network.packet.client.status.StatusRequestPacket;

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

    /**
     * Used for the initial handshake between the client and the server.
     *
     * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Handshaking">Handshaking</a>
     */
    HANDSHAKE(
            new Entry<>(0x00, HandshakePacket.class, new HandshakePacketHandler()),
            new Entry<>(0xFE, LegacyServerListPingPacket.class, new LegacyServerListPingPacketHandler())
    ),
    /**
     * Used for the initial status request and ping.
     *
     * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Status">Status</a>
     */
    STATUS(
            new Entry<>(0x00, StatusRequestPacket.class, new StatusRequestPacketHandler()),
            new Entry<>(0x01, PingRequestPacket.class, new PingRequestPacketHandler())
    ),
    /**
     * Used for the login process.
     *
     * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Login">Login</a>
     */
    LOGIN(
            new Entry<>(0x00, LoginStartPacket.class, new LoginStartPacketHandler()),
            new Entry<>(0x01, EncryptionResponsePacket.class, new EncryptionResponsePacketHandler()),
            new Entry<>(0x03, LoginAcknowledgedPacket.class, new LoginAcknowledgedPacketHandler())
    ),
    /**
     * Used for the configuration process.
     *
     * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Configuration">Configuration</a>
     */
    CONFIGURATION(
            new Entry<>(0x00, ClientInformationPacket.class, new ClientInformationPacketHandler()),
            new Entry<>(0x02, ServerBoundPluginMessagePacket.class, new ServerBoundPluginMessagePacketHandler())
    ),
    /**
     * Used for the main game interaction.
     *
     * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Play">Play</a>
     */
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
     * Gets the packet with the specified ID and its handler for this connection state.
     * <p>
     * A new instance of the packet is automatically created each time this method is called
     * using the packet's default constructor.
     *
     * @param <P> the packet type
     * @param id  the packet ID
     * @return the packet entry, or {@code null} if the packet is not registered for this connection state
     * @throws RuntimeException if the packet instance could not be created
     */
    @SuppressWarnings("unchecked")
    public <P extends ClientPacket> PacketEntry<P> getPacketById(int id) {
        Entry<P> entry = (Entry<P>) entries.get(id);

        if (entry == null) {
            // The packet is not registered for this connection state
            return null;
        }

        try {
            P instance = entry.clazz.getConstructor().newInstance();
            PacketHandler<P> handler = entry.handler();

            return new PacketEntry<>(instance, handler);
        } catch (Exception e) {
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
