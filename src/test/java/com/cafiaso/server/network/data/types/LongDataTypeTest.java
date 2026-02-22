package com.cafiaso.server.network.data.types;

import com.cafiaso.server.network.io.NetworkBuffer;
import com.cafiaso.server.network.io.NetworkByteBuffer;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class LongDataTypeTest {

    private static final byte[] SERIALIZED_VALUE = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
    private static final Long VALUE = 72623859790382856L;

    private static final LongDataType INSTANCE = new LongDataType();

    @Test
    void read_OK() throws IOException {
        // Given
        NetworkBuffer in = new NetworkByteBuffer(SERIALIZED_VALUE);

        // When
        Long value = in.read(INSTANCE);

        // Then
        assertEquals(VALUE, value);
    }

    @Test
    void write_OK() throws IOException {
        // Given
        NetworkBuffer out = new NetworkByteBuffer();

        // When
        out.write(INSTANCE, VALUE);

        // Then
        assertArrayEquals(SERIALIZED_VALUE, out.toByteArray());
    }
}
