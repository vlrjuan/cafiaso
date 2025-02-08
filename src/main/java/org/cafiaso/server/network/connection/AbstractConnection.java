package org.cafiaso.server.network.connection;

import org.cafiaso.server.Server;
import org.cafiaso.server.network.Serializer;
import org.cafiaso.server.network.connection.exceptions.UnknownPacketException;
import org.cafiaso.server.network.handler.PacketHandler;
import org.cafiaso.server.network.packet.client.ClientPacket;
import org.cafiaso.server.network.packet.server.ServerPacket;
import org.cafiaso.server.network.stream.input.ByteArrayInputStream;
import org.cafiaso.server.network.stream.input.InputStream;
import org.cafiaso.server.network.stream.output.ByteArrayOutputStream;
import org.cafiaso.server.utils.EncryptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;

/**
 * Partial implementation of {@link Connection} that provides read and write methods for packets.
 */
public abstract class AbstractConnection implements Connection {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractConnection.class);

    private final Server server;

    private final InetAddress address;

    protected final DataInputStream in;
    protected final DataOutputStream out;

    private ConnectionState state = ConnectionState.HANDSHAKE;

    private Identity identity;

    private byte[] verifyToken;

    private SecretKey sharedSecret;

    /**
     * AbstractConnection constructor.
     *
     * @param server  the server instance
     * @param address the client address
     * @param in      the input stream to read from
     * @param out     the output stream to write to
     */
    public AbstractConnection(Server server, InetAddress address, DataInputStream in, DataOutputStream out) {
        this.server = server;
        this.address = address;
        this.in = in;
        this.out = out;
    }

    @Override
    public void readPackets() throws IOException {
        // Read a reasonable chunk of data for decryption
        byte[] encryptedBuffer = new byte[1024];
        int readBytes = this.in.read(encryptedBuffer);

        if (readBytes <= 0) {
            // No data was read from the input stream
            return;
        }

        // Resize the buffer to match the actual read bytes
        byte[] encryptedData = Arrays.copyOf(encryptedBuffer, readBytes);

        // Decrypt the raw packet data if encryption is enabled
        byte[] decryptedData = isEncryptionEnabled()
                ? EncryptionUtils.decrypt(sharedSecret, encryptedData)
                : encryptedData;

        // Wrap the decrypted data in a ByteArrayInputStream for further parsing
        try (InputStream in = new ByteArrayInputStream(decryptedData)) {
            // Read all packets from the decrypted data until the input stream is empty
            while (!in.isEmpty()) {
                // Read the packet length
                int packetLength = in.read(Serializer.VAR_INT);

                // Read the packet ID (0xFE is a special case and stands for the LegacyServerListPing packet)
                int packetId = packetLength == 0xFE ? 0xFE : in.read(Serializer.VAR_INT);

                // Retrieve the packet entry for the packet ID
                ConnectionState.PacketEntry<ClientPacket> packetEntry = state.getPacketById(packetId);

                if (packetEntry == null) {
                    throw new UnknownPacketException(packetId, state);
                }

                ClientPacket packet = packetEntry.packet();
                PacketHandler<ClientPacket> handler = packetEntry.handler();

                // Read the packet data
                byte[] packetData = in.read(Serializer.BYTE_ARRAY(packetLength - 1));

                try (InputStream packetIn = new ByteArrayInputStream(packetData)) {
                    // Read the packet body
                    packet.read(packetIn);
                }

                LOGGER.debug("Received packet {} from {}", packet, this);

                // Handle the packet
                handler.handle(this, packet);
            }
        }
    }

    @Override
    public void sendPacket(ServerPacket packet) throws IOException {
        try (ByteArrayOutputStream packetBuffer = new ByteArrayOutputStream(); // Buffer to determine the packet length
             ByteArrayOutputStream out = new ByteArrayOutputStream()) { // Buffer to write the raw packet data
            // Write the packet ID
            packetBuffer.write(Serializer.VAR_INT, packet.getId());

            // Write the packet body
            packet.write(packetBuffer);

            // Get the raw packet data (excluding the packet length)
            byte[] packetBufferByteArray = packetBuffer.toByteArray();

            // Write the packet length (ID + body) to the temporary buffer
            out.write(Serializer.VAR_INT, packetBufferByteArray.length);

            // Write the packet ID and body to the temporary buffer
            out.write(Serializer.BYTE_ARRAY, packetBufferByteArray);

            byte[] packetData = out.toByteArray();

            // Encrypt the raw packet data if encryption is enabled
            byte[] encryptedPacketData = isEncryptionEnabled()
                    ? EncryptionUtils.encrypt(sharedSecret, packetData)
                    : packetData;

            // Write the encrypted packet data to the output stream
            this.out.write(encryptedPacketData);

            LOGGER.debug("Sent packet {} to {}", packet, this);
        }
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
    public void setState(ConnectionState state) {
        this.state = state;
    }

    @Override
    public Identity getIdentity() {
        return identity;
    }

    @Override
    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    @Override
    public byte[] getVerifyToken() {
        return verifyToken;
    }

    @Override
    public void setVerifyToken(byte[] verifyToken) {
        this.verifyToken = verifyToken;
    }

    @Override
    public void setSharedSecret(SecretKey sharedSecret) {
        this.sharedSecret = sharedSecret;
    }

    @Override
    public String toString() {
        return address.toString();
    }

    /**
     * Checks whether encryption is enabled for this connection.
     * <p>
     * Encryption is enabled if a shared secret has been set (i.e. during the EncryptionResponse packet handling).
     *
     * @return {@code true} if encryption is enabled, {@code false} otherwise
     */
    private boolean isEncryptionEnabled() {
        return sharedSecret != null;
    }
}
