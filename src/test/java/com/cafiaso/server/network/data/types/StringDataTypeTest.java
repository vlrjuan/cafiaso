package com.cafiaso.server.network.data.types;

import com.cafiaso.server.network.io.NetworkBuffer;
import com.cafiaso.server.network.io.NetworkByteBuffer;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class StringDataTypeTest {

    private static final byte[] SERIALIZED_VALUE = {0x0d, 'H', 'e', 'l', 'l', 'o', ',', ' ', 'W', 'o', 'r', 'l', 'd', '!'};
    private static final String VALUE = "Hello, World!";

    private static final StringDataType INSTANCE = new StringDataType();

    @Test
    void read_OK() throws IOException {
        // Given
        NetworkBuffer in = new NetworkByteBuffer(SERIALIZED_VALUE);

        // When
        String value = in.read(INSTANCE);

        // Then
        assertEquals(VALUE, value);
    }

    @Test
    void read_ShouldThrowException_WhenStringIsTooLong() {
        // Given
        byte[] value1 = {(byte) 0xa0, (byte) 0x8d, 0x06}; // 100 000
        NetworkBuffer in1 = new NetworkByteBuffer(value1);

        // When/Then
        IOException exception = assertThrowsExactly(IOException.class, () -> INSTANCE.read(in1));
        assertEquals(
                "String is too long. Waiting for maximum 98304 characters, received 100000.",
                exception.getMessage()
        );

        // Given
        int maxLength = 100;
        StringDataType dataType = new StringDataType(maxLength);

        byte[] value2 = {(byte) 0x96, 0x01}; // 150
        NetworkBuffer in2 = new NetworkByteBuffer(value2);

        // When/Then
        IOException exception2 = assertThrowsExactly(IOException.class, () -> dataType.read(in2));
        assertEquals(
                "String is too long. Waiting for maximum 100 characters, received 150.",
                exception2.getMessage()
        );
    }

    @Test
    void write_OK() throws IOException {
        // Given
        NetworkBuffer out = new NetworkByteBuffer();

        // When
        INSTANCE.write(VALUE, out);

        // Then
        assertArrayEquals(SERIALIZED_VALUE, out.toByteArray());
    }
}
