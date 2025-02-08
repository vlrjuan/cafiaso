package org.cafiaso.server.network.handler.login;

import org.cafiaso.server.Server;
import org.cafiaso.server.network.connection.Connection;
import org.cafiaso.server.network.connection.Connection.Identity;
import org.cafiaso.server.network.packet.client.login.LoginStartPacket;
import org.cafiaso.server.network.packet.server.login.EncryptionRequestPacket;
import org.cafiaso.server.security.SecurityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginStartPacketHandlerTest {

    private Connection connection;

    @BeforeEach
    void setUp() {
        connection = mock(Connection.class);
    }

    @Test
    void handle_ShouldSetConnectionIdentityAndGenerateVerifyTokenAndSendEncryptionRequestPacket() throws IOException {
        byte[] publicKey = new byte[]{1, 2, 3, 4, 5};
        byte[] verifyToken = new byte[]{6, 7, 8, 9, 10};

        Server server = mock(Server.class);

        SecurityManager securityManager = mock(SecurityManager.class);
        when(securityManager.getPublicKey()).thenReturn(publicKey);
        when(securityManager.generateVerifyToken()).thenReturn(verifyToken);

        when(server.getSecurityManager()).thenReturn(securityManager);

        when(connection.getServer()).thenReturn(server);

        String username = "Choukas";
        UUID uuid = UUID.randomUUID();

        LoginStartPacket packet = mock(LoginStartPacket.class);
        when(packet.getUsername()).thenReturn(username);
        when(packet.getUuid()).thenReturn(uuid);

        LoginStartPacketHandler handler = new LoginStartPacketHandler();
        handler.handle(connection, packet);

        ArgumentCaptor<Identity> identityCaptor = ArgumentCaptor.forClass(Identity.class);

        verify(connection).setIdentity(identityCaptor.capture());

        Identity identity = identityCaptor.getValue();
        assertNotNull(identity);
        assertEquals(username, identity.username());
        assertEquals(uuid, identity.uuid());

        ArgumentCaptor<EncryptionRequestPacket> packetCaptor = ArgumentCaptor.forClass(EncryptionRequestPacket.class);

        verify(connection).sendPacket(packetCaptor.capture());

        EncryptionRequestPacket capturedPacket = packetCaptor.getValue();

        assertNotNull(capturedPacket);
        assertEquals("", capturedPacket.getServerId());
        assertArrayEquals(publicKey, capturedPacket.getPublicKey());
        assertArrayEquals(verifyToken, capturedPacket.getVerifyToken());
        assertTrue(capturedPacket.shouldAuthenticate());
    }
}
