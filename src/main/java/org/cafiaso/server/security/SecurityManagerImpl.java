package org.cafiaso.server.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Default {@link SecurityManager} implementation.
 */
public class SecurityManagerImpl implements SecurityManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityManagerImpl.class);

    private static final int KEY_SIZE = 1024;

    private KeyPair keyPair;

    @Override
    public void generateKeyPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(KEY_SIZE);

            keyPair = generator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("An error occurred while generating key pair", e);
        }
    }

    @Override
    public byte[] generateVerifyToken() {
        byte[] token = new byte[4];
        ThreadLocalRandom.current().nextBytes(token);

        return token;
    }

    @Override
    public byte[] getPublicKey() {
        return keyPair.getPublic().getEncoded();
    }

    @Override
    public boolean isVerifyTokenValid(byte[] sentVerifyToken, byte[] storedVerifyToken) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());

            return Arrays.equals(storedVerifyToken, cipher.doFinal(sentVerifyToken));
        } catch (Exception e) {
            LOGGER.error("An error occurred while decrypting verify token", e);

            return false;
        }
    }

    @Override
    public SecretKey decryptSharedSecret(byte[] encryptedSharedSecret) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());

            byte[] decryptedSharedSecret = cipher.doFinal(encryptedSharedSecret);

            return new SecretKeySpec(decryptedSharedSecret, "AES");
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while decrypting shared secret", e);
        }
    }

    @Override
    public String generateServerId(String baseServerId, SecretKey sharedSecret) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");

            digest.update(baseServerId.getBytes()); // Empty string for now
            digest.update(sharedSecret.getEncoded()); // Encoded shared secret (symmetric AES key)
            digest.update(getPublicKey()); // Encoded public key

            // Digest the data
            byte[] digestData = digest.digest();

            return new BigInteger(digestData).toString(16);
        } catch (NoSuchAlgorithmException e) {
            // This should never happen, SHA-1 is a standard algorithm
            throw new RuntimeException("An error occurred while digesting server uuid", e);
        }
    }
}
