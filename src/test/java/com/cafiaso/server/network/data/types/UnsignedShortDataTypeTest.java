package com.cafiaso.server.network.data.types;

import com.cafiaso.server.network.io.NetworkBuffer;
import com.cafiaso.server.network.io.NetworkByteBuffer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnsignedShortDataTypeTest {

    private static final byte[] SERIALIZED_VALUE = {0x63, (byte) 0xdd};
    private static final int VALUE = 25565;

    private static final UnsignedShortDataType INSTANCE = new UnsignedShortDataType();

    @Test
    void read_OK() {
        // Given
        NetworkBuffer in = new NetworkByteBuffer(SERIALIZED_VALUE);

        // When
        int value = INSTANCE.read(in);

        // Then
        assertEquals(VALUE, value);
    }

    @Test
    void write_OK() {
        // Given
        NetworkBuffer out = new NetworkByteBuffer();

        // When
        INSTANCE.write(VALUE, out);

        // Then
        assertArrayEquals(SERIALIZED_VALUE, out.toByteArray());
    }

    @Test
    void write_ShouldThrowException_WhenValueIsNegative() {
        // Given
        NetworkBuffer out = new NetworkByteBuffer();

        int value = -1;

        // When/Then
        IllegalArgumentException exception = assertThrowsExactly(IllegalArgumentException.class, () -> INSTANCE.write(value, out));
        assertEquals("Value out of range for unsigned short: -1", exception.getMessage());
    }

    @Test
    void write_ShouldThrowException_WhenValueIsGreaterThanMax() {
        // Given
        NetworkBuffer out = new NetworkByteBuffer();

        int value = 65536;

        // When/Then
        IllegalArgumentException exception = assertThrowsExactly(IllegalArgumentException.class, () -> INSTANCE.write(value, out));
        assertEquals("Value out of range for unsigned short: 65536", exception.getMessage());
    }
}
