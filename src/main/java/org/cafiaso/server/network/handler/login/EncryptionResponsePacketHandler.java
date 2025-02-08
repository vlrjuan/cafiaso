package org.cafiaso.server.network.handler.login;

import org.cafiaso.server.Server;
import org.cafiaso.server.mojang.PlayerProfile;
import org.cafiaso.server.network.connection.Connection;
import org.cafiaso.server.network.handler.PacketHandler;
import org.cafiaso.server.network.packet.client.login.EncryptionResponsePacket;
import org.cafiaso.server.network.packet.server.login.LoginSuccessPacket;
import org.cafiaso.server.network.server.NetworkServer;

import javax.crypto.SecretKey;
import java.io.IOException;

/**
 * Handles the {@link EncryptionResponsePacket}.
 */
public class EncryptionResponsePacketHandler implements PacketHandler<EncryptionResponsePacket> {

    @Override
    public void handle(Connection connection, EncryptionResponsePacket packet) throws IOException {
        Server server = connection.getServer();
        NetworkServer networkServer = server.getNetworkServer();

        if (!server.getSecurityManager().isVerifyTokenValid(packet.getVerifyToken(), connection.getVerifyToken())) {
            // The verify token sent by the client does not match the one we sent, disconnect the client
            networkServer.closeConnection(connection);

            return;
        }

        SecretKey sharedSecret = server.getSecurityManager().decryptSharedSecret(packet.getSharedSecret());

        // Set the shared secret in the connection
        // From now on, all packets will be encrypted using this shared secret
        connection.setSharedSecret(sharedSecret);

        // Generate the server ID used to verify the client login session
        String digestedServerId = server.getSecurityManager().generateServerId("", sharedSecret);

        PlayerProfile playerProfile = server.getMojangClient().verifyClientLoginSession(
                connection.getIdentity().username(),
                digestedServerId,
                connection.getAddress().getHostAddress()
        );

        connection.sendPacket(
                new LoginSuccessPacket(
                        playerProfile.getId(),
                        playerProfile.name(),
                        playerProfile.properties()
                )
        );
    }
}
