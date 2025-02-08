package org.cafiaso.server.utils;

/**
 * Utility class for integer operations.
 */
public class IntegerUtils {

    private static final int SEGMENT_BITS = 0x7F;
    private static final int CONTINUE_BIT = 0x80;

    /**
     * Returns the number of bytes required to serialize the given integer.
     * <p>
     * Mainly used for validating the correctness of the serialization and for testing purposes.
     *
     * @param value the integer
     * @return the number of bytes required to serialize the integer
     */
    public static int getSerializedLength(int value) {
        int length = 0;

        // While there are more than 7 bits left in the number, count how many bytes it will take
        do {
            value >>>= 7; // Right shift by 7 bits (ignoring the sign bit)
            length++;
        } while (value != 0);

        return length;
    }

    /**
     * Converts the given value to a byte array.
     * <p>
     * Mainly used for testing purposes (e.g. create a too long string).
     *
     * @param value the value
     * @return the byte array
     */
    public static byte[] toByteArray(Integer value) {
        byte[] bytes = new byte[getSerializedLength(value)];

        int index = 0;

        while (true) {
            if ((value & ~SEGMENT_BITS) == 0) {
                bytes[index] = value.byteValue();

                return bytes;
            }

            bytes[index] = (byte) ((value & SEGMENT_BITS) | CONTINUE_BIT);

            value >>>= 7;
            index++;
        }
    }
}
