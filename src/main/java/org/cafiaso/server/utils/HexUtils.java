package org.cafiaso.server.utils;

/**
 * Utility class for hex operations.
 */
public class HexUtils {

    /**
     * Converts an integer to a hexadecimal string.
     * <p>
     * Example:
     * <pre>
     *     int n = 255;
     *     String hex = IntegerUtils.toHexString(n);
     *     System.out.println(hex); // Output: 0xff
     * </pre>
     *
     * @param n the integer to convert
     * @return the hexadecimal string
     */
    public static String toHexString(int n) {
        return "0x" + Integer.toHexString(n);
    }
}
