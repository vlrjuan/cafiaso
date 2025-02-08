package org.cafiaso.server.network.handler.login;

import org.cafiaso.server.Server;
import org.cafiaso.server.mojang.MojangClient;
import org.cafiaso.server.mojang.PlayerProfile;
import org.cafiaso.server.network.connection.Connection;
import org.cafiaso.server.network.packet.client.login.EncryptionResponsePacket;
import org.cafiaso.server.network.packet.server.login.LoginSuccessPacket;
import org.cafiaso.server.network.server.NetworkServer;
import org.cafiaso.server.security.SecurityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EncryptionResponsePacketHandlerTest {

    private Connection connection;

    @BeforeEach
    void setUp() {
        connection = mock(Connection.class);
    }

    @Test
    void handle_ShouldSetSharedSecretAndVerifyClientLoginSession() throws IOException {
        byte[] encryptedSharedSecret = new byte[]{1, 2, 3, 4, 5};
        byte[] clientVerifyToken = new byte[]{6, 7, 8, 9, 10};
        byte[] serverVerifyToken = new byte[]{11, 12, 13, 14, 15};

        Server server = mock(Server.class);

        SecurityManager securityManager = mock(SecurityManager.class);
        when(securityManager.isVerifyTokenValid(clientVerifyToken, serverVerifyToken)).thenReturn(true);

        SecretKey decryptedSharedSecret = mock(SecretKey.class);
        when(securityManager.decryptSharedSecret(encryptedSharedSecret)).thenReturn(decryptedSharedSecret);

        String baseServerId = "0000";
        when(securityManager.generateServerId("", decryptedSharedSecret)).thenReturn(baseServerId);

        when(server.getSecurityManager()).thenReturn(securityManager);

        PlayerProfile playerProfile = mock(PlayerProfile.class);
        when(playerProfile.getId()).thenReturn(UUID.randomUUID());
        when(playerProfile.name()).thenReturn("Choukas");
        when(playerProfile.properties()).thenReturn(new PlayerProfile.Property[]{});

        MojangClient mojangClient = mock(MojangClient.class);
        when(server.getMojangClient()).thenReturn(mojangClient);

        Connection.Identity identity = mock(Connection.Identity.class);
        when(identity.username()).thenReturn("Choukas");

        when(connection.getIdentity()).thenReturn(identity);

        InetAddress address = mock(InetAddress.class);
        when(address.getHostAddress()).thenReturn("127.0.0.1");

        when(connection.getAddress()).thenReturn(address);

        when(mojangClient.verifyClientLoginSession("Choukas", baseServerId, "127.0.0.1")).thenReturn(playerProfile);

        when(connection.getServer()).thenReturn(server);
        when(connection.getVerifyToken()).thenReturn(serverVerifyToken);

        EncryptionResponsePacket packet = mock(EncryptionResponsePacket.class);
        when(packet.getSharedSecret()).thenReturn(encryptedSharedSecret);
        when(packet.getVerifyToken()).thenReturn(clientVerifyToken);

        EncryptionResponsePacketHandler handler = new EncryptionResponsePacketHandler();
        handler.handle(connection, packet);

        verify(connection).setSharedSecret(decryptedSharedSecret);

        ArgumentCaptor<LoginSuccessPacket> loginSuccessPacketCaptor = ArgumentCaptor.forClass(LoginSuccessPacket.class);
        verify(connection).sendPacket(loginSuccessPacketCaptor.capture());

        LoginSuccessPacket loginSuccessPacket = loginSuccessPacketCaptor.getValue();
        assertNotNull(loginSuccessPacket);
        assertEquals(playerProfile.getId(), loginSuccessPacket.getUuid());
        assertEquals(playerProfile.name(), loginSuccessPacket.getUsername());
        assertArrayEquals(playerProfile.properties(), loginSuccessPacket.getProperties());
    }

    @Test
    void handle_ShouldCloseConnection_WhenVerifyTokenIsInvalid() throws IOException {
        byte[] sharedSecret = new byte[]{1, 2, 3, 4, 5};
        byte[] clientVerifyToken = new byte[]{6, 7, 8, 9, 10};
        byte[] serverVerifyToken = new byte[]{11, 12, 13, 14, 15};

        Server server = mock(Server.class);

        NetworkServer networkServer = mock(NetworkServer.class);
        when(server.getNetworkServer()).thenReturn(networkServer);

        SecurityManager securityManager = mock(SecurityManager.class);
        when(securityManager.isVerifyTokenValid(clientVerifyToken, serverVerifyToken)).thenReturn(false);

        when(server.getSecurityManager()).thenReturn(securityManager);

        when(connection.getServer()).thenReturn(server);

        EncryptionResponsePacket packet = mock(EncryptionResponsePacket.class);
        when(packet.getSharedSecret()).thenReturn(sharedSecret);
        when(packet.getVerifyToken()).thenReturn(clientVerifyToken);

        EncryptionResponsePacketHandler handler = new EncryptionResponsePacketHandler();
        handler.handle(connection, packet);

        verify(networkServer).closeConnection(connection);

        verify(connection, never()).sendPacket(any());
    }
}
