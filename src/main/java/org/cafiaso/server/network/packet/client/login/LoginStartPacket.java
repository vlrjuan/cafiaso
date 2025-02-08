package org.cafiaso.server.network.packet.client.login;

import org.cafiaso.server.network.Serializer;
import org.cafiaso.server.network.packet.client.ClientPacket;
import org.cafiaso.server.network.stream.input.InputStream;

import java.io.IOException;
import java.util.UUID;

/**
 * Packet sent by the client to start the login process (when he clicks the "Login" button).
 *
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Login_Start">Login Start Packet</a>
 */
public class LoginStartPacket implements ClientPacket {

    private static final int MAX_USERNAME_LENGTH = 16;

    private static final Serializer<String> USERNAME_SERIALIZER = Serializer.STRING(MAX_USERNAME_LENGTH);

    private String username;
    private UUID uuid;

    @Override
    public void read(InputStream in) throws IOException {
        username = in.read(USERNAME_SERIALIZER);
        uuid = in.read(Serializer.UUID);
    }

    /**
     * Gets the username of the client.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the UUID of the client.
     *
     * @return the UUID
     */
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return "LoginStartPacket{" +
                "username='" + username + '\'' +
                ", uuid=" + uuid +
                '}';
    }
}
