package org.cafiaso.server.network.packet.server.login;

import org.cafiaso.server.mojang.PlayerProfile.Property;
import org.cafiaso.server.network.Serializer;
import org.cafiaso.server.network.packet.server.ServerPacket;
import org.cafiaso.server.network.stream.output.OutputStream;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

/**
 * Packet sent by the server to acknowledge the client's login. Marks the end of the login process.
 *
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Login_Success">Login Success Packet</a>
 */
public class LoginSuccessPacket extends ServerPacket {

    private final UUID uuid;
    private final String username;
    private final Property[] properties;

    /**
     * LoginSuccessPacket constructor.
     *
     * @param uuid       the UUID of the client
     * @param username   the username of the client
     * @param properties an array of properties (not used yet)
     */
    public LoginSuccessPacket(UUID uuid, String username, Property[] properties) {
        super(0x02);

        this.uuid = uuid;
        this.username = username;
        this.properties = properties;
    }

    @Override
    public void write(OutputStream out) throws IOException {
        out.write(Serializer.UUID, this.uuid);
        out.write(Serializer.STRING, this.username);
        out.write(Serializer.VAR_INT, 0); // TODO: Implement properties serialization
    }

    /**
     * Returns the UUID of the client.
     *
     * @return the UUID
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Returns the username of the client.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns an array of the client properties.
     *
     * @return the client properties
     */
    public Property[] getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return "LoginSuccessPacket{" +
                "uuid=" + uuid +
                ", username='" + username + '\'' +
                ", properties=" + Arrays.toString(properties) +
                '}';
    }
}
