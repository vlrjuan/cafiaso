package org.cafiaso.server.security;

import javax.crypto.SecretKey;

/**
 * Provides various encryption and decryption operations.
 * <p>
 * This class is responsible for generating the server's key pair, verifying the client's verify token,
 * decrypting the shared secret, and generating the server ID.
 *
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol_Encryption">Protocol Encryption</a>
 */
public interface SecurityManager {

    /**
     * Generates a new RSA key pair for the server (public and private key).
     * <p>
     * Must be called during the server initialization, before any other method.
     */
    void generateKeyPair();

    /**
     * Generates a new verify token used to verify the shared secret.
     *
     * @return the verify token
     */
    byte[] generateVerifyToken();

    /**
     * Returns the server's public key.
     *
     * @return the public key
     */
    byte[] getPublicKey();

    /**
     * Checks if the verify token sent by the client is valid.
     *
     * @param sentVerifyToken   the verify token sent by the client
     * @param storedVerifyToken the verify token stored by the server
     * @return {@code true} if the verify token is valid, {@code false} otherwise or if an error occurred
     */
    boolean isVerifyTokenValid(byte[] sentVerifyToken, byte[] storedVerifyToken);

    /**
     * Decrypts the shared secret sent by the client.
     *
     * @param encryptedSharedSecret the shared secret, encrypted using the server's public key
     * @return the decrypted shared secret
     * @throws RuntimeException if an error occurred while decrypting the shared secret
     */
    SecretKey decryptSharedSecret(byte[] encryptedSharedSecret);

    /**
     * Generates the server ID using the base server ID, the shared secret, and the server's public key.
     * <p>
     * The server ID is used to verify the client login session.
     *
     * @param baseServerId the base server ID (empty string)
     * @param sharedSecret the shared secret (decrypted in {@link #decryptSharedSecret(byte[])})
     * @return the generated server ID
     * @throws RuntimeException if an error occurred while generating the server ID
     * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol_Encryption#Authentication">Pseudo code implementation</a>
     * @see <a href="https://minecraft.wiki/w/Mojang_API#Server">Java implementation</a>
     */
    String generateServerId(String baseServerId, SecretKey sharedSecret);
}
