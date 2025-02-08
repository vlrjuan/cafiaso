package org.cafiaso.server.network.handler.login;

import org.cafiaso.server.network.connection.Connection;
import org.cafiaso.server.network.handler.PacketHandler;
import org.cafiaso.server.network.packet.client.login.LoginStartPacket;
import org.cafiaso.server.network.packet.server.login.EncryptionRequestPacket;
import org.cafiaso.server.security.SecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.cafiaso.server.network.connection.Connection.Identity;

/**
 * Handles the {@link LoginStartPacket}.
 */
public class LoginStartPacketHandler implements PacketHandler<LoginStartPacket> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginStartPacketHandler.class);

    @Override
    public void handle(Connection connection, LoginStartPacket packet) throws IOException {
        SecurityManager securityManager = connection.getServer().getSecurityManager();

        Identity identity = new Identity(packet.getUsername(), packet.getUuid());

        LOGGER.debug("Starting login process for profile {}", identity);

        connection.setIdentity(identity);

        // Generate a new verify token and send it to the client
        // It is used to verify the shared secret after the client sends the EncryptionResponsePacket
        byte[] verifyToken = securityManager.generateVerifyToken();
        connection.setVerifyToken(verifyToken);

        connection.sendPacket(
                new EncryptionRequestPacket(
                        "",
                        securityManager.getPublicKey(), // Server public key
                        verifyToken,
                        true // Authentication with Mojang is mandatory for online mode servers
                )
        );
    }
}
