package org.cafiaso.server.network.connection;

import org.cafiaso.server.Server;
import org.cafiaso.server.network.DataType;
import org.cafiaso.server.network.buffers.InputBuffer;
import org.cafiaso.server.network.buffers.OutputBuffer;
import org.cafiaso.server.network.handler.PacketHandler;
import org.cafiaso.server.network.packet.client.ClientPacket;
import org.cafiaso.server.network.packet.server.ServerPacket;
import org.cafiaso.server.utils.IntegerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Implementation of {@link Connection} that provides common functionality for connections
 * and uses a {@link InputBuffer} and {@link OutputBuffer} to read and write packets.
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
     * The buffer to read from.
     */
    protected final InputBuffer inputBuffer;

    /**
     * The buffer to write to.
     */
    protected final OutputBuffer outputBuffer;

    /**
     * The current connection state.
     */
    private ConnectionState state;

    /**
     * AbstractConnection constructor.
     *
     * @param server       the server instance
     * @param address      the client address
     * @param inputBuffer  the buffer to read from
     * @param outputBuffer the buffer to write to
     */
    public AbstractConnection(Server server, InetAddress address, InputBuffer inputBuffer, OutputBuffer outputBuffer) {
        this.server = server;
        this.address = address;
        this.inputBuffer = inputBuffer;
        this.outputBuffer = outputBuffer;
        this.state = ConnectionState.HANDSHAKE;
    }

    @Override
    public boolean readPacket() throws IOException {
        if (inputBuffer.isEmpty()) {
            return false;
        }

        int packetLength = inputBuffer.read(DataType.VAR_INT);
        int packetId = packetLength == 0xFE ? 0xFE : inputBuffer.read(DataType.VAR_INT);

        ConnectionState.PacketEntry<ClientPacket> packetEntry = state.getPacketById(packetId);

        if (packetEntry == null) {
            LOGGER.warn("Received unknown packet with id {} from {}", IntegerUtils.toHexString(packetId), this);

            return false;
        }

        ClientPacket packet = packetEntry.packet();
        PacketHandler<ClientPacket> packetHandler = packetEntry.handler();

        // Read the packet data
        packet.read(inputBuffer);

        LOGGER.debug("Received packet {} from {}", packet, this);

        // Handle the packet
        packetHandler.handle(this, packet);

        return true;
    }

    @Override
    public void sendPacket(ServerPacket packet) throws IOException {
        outputBuffer.write(DataType.VAR_INT, packet.getLength());
        outputBuffer.write(DataType.VAR_INT, packet.getId());
        packet.write(outputBuffer);

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

        inputBuffer.close();
        outputBuffer.close();
    }

    @Override
    public String toString() {
        return address.toString();
    }
}
