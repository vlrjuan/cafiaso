package org.cafiaso.server.utils;

import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionUtilsTest {

    @Test
    void testEncryptAndDecrypt() throws Exception {
        // Generate a secret key for testing
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128); // AES-128
        SecretKey secretKey = keyGenerator.generateKey();

        // Test data
        String originalData = "This is a test string";
        byte[] originalBytes = originalData.getBytes(StandardCharsets.UTF_8);

        // Encrypt the data
        byte[] encryptedBytes = EncryptionUtils.encrypt(secretKey, originalBytes);

        // Decrypt the data
        byte[] decryptedBytes = EncryptionUtils.decrypt(secretKey, encryptedBytes);

        // Validate that decrypted bytes match original bytes
        assertArrayEquals(originalBytes, decryptedBytes, "Decrypted data should match the original data");

        // Validate that encryption doesn't produce the same bytes as input
        assertFalse(Arrays.equals(originalBytes, encryptedBytes), "Encrypted data should not match the original data");
    }
}
