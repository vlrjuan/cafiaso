package org.cafiaso.server.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.IOException;

/**
 * Utility class for encrypting and decrypting data using AES encryption.
 */
public class EncryptionUtils {

    /**
     * Decrypts the given data using the given secret key.
     *
     * @param secretKey the secret key to use for decryption
     * @param data      the data to decrypt
     * @return the decrypted data
     * @throws IOException if an error occurs while decrypting the data
     */
    public static byte[] decrypt(SecretKey secretKey, byte[] data) throws IOException {
        return operate(secretKey, data, Cipher.DECRYPT_MODE);
    }

    /**
     * Encrypts the given packet data using the given secret key.
     *
     * @param secretKey the secret key to use for encryption
     * @param data      the data to encrypt
     * @return the encrypted data
     * @throws IOException if an error occurs while encrypting the data
     */
    public static byte[] encrypt(SecretKey secretKey, byte[] data) throws IOException {
        return operate(secretKey, data, Cipher.ENCRYPT_MODE);
    }

    /**
     * Encrypts or decrypts the given data using the given secret key.
     *
     * @param secretKey the secret key to use for encryption/decryption
     * @param data      the data to encrypt/decrypt
     * @param mode      the mode to use (either {@link Cipher#ENCRYPT_MODE} or {@link Cipher#DECRYPT_MODE})
     * @return the encrypted/decrypted data
     * @throws IOException if an error occurs while encrypting/decrypting the data
     */
    private static byte[] operate(SecretKey secretKey, byte[] data, int mode) throws IOException {
        try {
            Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
            cipher.init(mode, secretKey, new IvParameterSpec(secretKey.getEncoded()));

            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new IOException("An error occurred while encrypting packet", e);
        }
    }
}
