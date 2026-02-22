package com.cafiaso.server.network.data.types;

import com.cafiaso.server.network.io.NetworkBuffer;
import com.cafiaso.server.network.io.NetworkByteBuffer;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class VarIntDataTypeTest {

    private static final byte[] SERIALIZED_VALUE = {(byte) 0xdd, (byte) 0xc7, 0x01};
    private static final int VALUE = 25565;

    private static final VarIntDataType INSTANCE = new VarIntDataType();

    @Test
    void read_OK() throws IOException {
        // Given
        NetworkBuffer in = new NetworkByteBuffer(SERIALIZED_VALUE);

        // When
        int value = in.read(INSTANCE);

        // Then
        assertEquals(VALUE, value);
    }

    @Test
    void read_ThrowIOException_WhenVarIntIsTooLong() {
        // Given
        byte[] serializedValue = {(byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, 0x01};

        // When/Then
        IOException exception = assertThrowsExactly(IOException.class, () -> INSTANCE.read(new NetworkByteBuffer(serializedValue)));
        assertEquals("VarInt is too big", exception.getMessage());
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
}
