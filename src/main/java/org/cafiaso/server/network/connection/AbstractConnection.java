package org.cafiaso.server.network.connection;

import org.cafiaso.server.Server;
import org.cafiaso.server.network.ClientPacket;
import org.cafiaso.server.network.PacketHandler;
import org.cafiaso.server.network.ServerPacket;
import org.cafiaso.server.network.DataType;
import org.cafiaso.server.utils.IntegerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Optional;

/**
 * Implementation of {@link Connection} that provides common functionality for connections
 * and that writes and reads packets from a {@link DataInputStream} and {@link DataOutputStream}.
 */
public abstract class AbstractConnection implements Connection {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractConnection.class);

    /**
     * The server instance.
     */
    private final Server server;

    /**
     * The address of the connection.
     */
    private final InetAddress address;

    /**
     * The stream to read data from.
     */
    private final DataInputStream in;

    /**
     * The stream to write data to.
     */
    private final DataOutputStream out;

    /**
     * The current connection state.
     */
    private ConnectionState state;

    /**
     * AbstractConnection constructor.
     *
     * @param server the server instance
     * @param in the stream to read data from
     * @param out the stream to write data to
     */
    public AbstractConnection(Server server, InetAddress address, DataInputStream in, DataOutputStream out) {
        this.server = server;
        this.address = address;

        this.in = in;
        this.out = out;

        this.state = ConnectionState.HANDSHAKE;
    }

    @Override
    public void readPacket() throws IOException {
        if (in.available() == 0) {
            return;
        }

        int packetLength = DataType.VAR_INT.read(in);
        int packetId = packetLength == 0xFE ? 0xFE : DataType.VAR_INT.read(in);

        Optional<ConnectionState.PacketEntry<ClientPacket>> optionalPacketEntry = state.getPacketById(packetId);

        if (optionalPacketEntry.isPresent()) {
            ConnectionState.PacketEntry<ClientPacket> packetEntry = optionalPacketEntry.get();
            ClientPacket packet = packetEntry.packet();
            PacketHandler<ClientPacket> packetHandler = packetEntry.handler();

            // Read the packet data
            packet.read(in);

            LOGGER.debug("Received packet {} from {}", packet, this);

            // Handle the packet
            packetHandler.handle(this, packet);
        } else {
            LOGGER.warn("Received unknown packet with id {} from {}", IntegerUtils.toHexString(packetId), this);
        }
    }

    @Override
    public void sendPacket(ServerPacket packet) throws IOException {
        DataType.VAR_INT.write(out, packet.getLength());
        DataType.VAR_INT.write(out, packet.getId());
        packet.write(out);

        LOGGER.debug("Sent packet {} to {}", packet, this);
    }

    @Override
    public void setState(ConnectionState state) {
        this.state = state;
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public InetAddress getAddress() {
        return address;
    }

    @Override
    public void close() throws IOException {
        LOGGER.info("Closing connection {}", this);

        in.close();
        out.close();
    }

    @Override
    public String toString() {
        return getAddress().toString();
    }
}
