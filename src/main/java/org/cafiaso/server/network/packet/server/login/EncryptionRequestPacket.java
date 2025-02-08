package org.cafiaso.server.network.packet.server.login;

import org.cafiaso.server.network.Serializer;
import org.cafiaso.server.network.packet.server.ServerPacket;
import org.cafiaso.server.network.stream.output.OutputStream;

import java.io.IOException;
import java.util.Arrays;

/**
 * Packet sent by the server to request encryption from the client.
 *
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Encryption_Request">Encryption Request Packet</a>
 */
public class EncryptionRequestPacket extends ServerPacket {

    private final String serverId;
    private final byte[] publicKey;
    private final byte[] verifyToken;
    private final boolean shouldAuthenticate;

    /**
     * EncryptionRequestPacket constructor.
     *
     * @param serverId           the server ID (always empty)
     * @param publicKey          the server public key
     * @param verifyToken        the verify token
     * @param shouldAuthenticate whether the client should authenticate through Mojang servers (always true, mandatory for online mode servers)
     */
    public EncryptionRequestPacket(String serverId, byte[] publicKey, byte[] verifyToken, boolean shouldAuthenticate) {
        super(0x01);

        this.serverId = serverId;
        this.publicKey = publicKey;
        this.verifyToken = verifyToken;
        this.shouldAuthenticate = shouldAuthenticate;
    }

    @Override
    public void write(OutputStream out) throws IOException {
        out.write(Serializer.STRING, this.serverId);
        out.write(Serializer.PREFIXED_BYTE_ARRAY, this.publicKey);
        out.write(Serializer.PREFIXED_BYTE_ARRAY, this.verifyToken);
        out.write(Serializer.BOOLEAN, this.shouldAuthenticate);
    }

    /**
     * Gets the server id
     *
     * @return the server id
     */
    public String getServerId() {
        return serverId;
    }

    /**
     * Gets the server public key.
     *
     * @return the public key
     */
    public byte[] getPublicKey() {
        return publicKey;
    }

    /**
     * Gets the verify token.
     *
     * @return the verify token
     */
    public byte[] getVerifyToken() {
        return verifyToken;
    }

    /**
     * Checks whether the client should authenticate through Mojang servers.
     *
     * @return {@code true} if the client should authenticate, {@code false} otherwise
     */
    public boolean shouldAuthenticate() {
        return shouldAuthenticate;
    }

    @Override
    public String toString() {
        return "EncryptionRequestPacket{" +
                "serverId='" + serverId + '\'' +
                ", publicKey=" + Arrays.toString(publicKey) +
                ", verifyToken=" + Arrays.toString(verifyToken) +
                ", shouldAuthenticate=" + shouldAuthenticate +
                '}';
    }
}
