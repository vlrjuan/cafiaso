package org.cafiaso.server.security;

import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import static org.junit.jupiter.api.Assertions.*;

class SecurityManagerImplTest {

    private final SecurityManagerImpl securityManager = new SecurityManagerImpl();

    @Test
    void generateKeyPair_ShouldGenerateNonNullKeyPair() {
        securityManager.generateKeyPair();
        assertNotNull(securityManager.getPublicKey());
    }

    @Test
    void generateVerifyToken_ShouldGenerateNonNullToken() {
        byte[] token = securityManager.generateVerifyToken();
        assertNotNull(token);
        assertEquals(4, token.length);
    }

    @Test
    void generateServerId_ShouldReturnNonNullServerId() {
        securityManager.generateKeyPair();
        SecretKey sharedSecret = new SecretKeySpec("1234567890123456".getBytes(), "AES");
        String serverId = securityManager.generateServerId("", sharedSecret);
        assertNotNull(serverId);
    }

    @Test
    void generateServerId_ShouldReturnDifferentIdsForDifferentSecrets() {
        securityManager.generateKeyPair();
        SecretKey sharedSecret1 = new SecretKeySpec("1234567890123456".getBytes(), "AES");
        SecretKey sharedSecret2 = new SecretKeySpec("6543210987654321".getBytes(), "AES");
        String serverId1 = securityManager.generateServerId("", sharedSecret1);
        String serverId2 = securityManager.generateServerId("", sharedSecret2);
        assertNotEquals(serverId1, serverId2);
    }
}
