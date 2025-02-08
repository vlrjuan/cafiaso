package org.cafiaso.server.network.serializers;

import org.cafiaso.server.utils.IntegerUtils;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static org.cafiaso.server.network.serializers.StringSerializer.MAX_LENGTH;
import static org.junit.jupiter.api.Assertions.*;

class StringSerializerTest {

    private static final byte[] SERIALIZED_VALUE = {0x0D, 'H', 'e', 'l', 'l', 'o', ',', ' ', 'W', 'o', 'r', 'l', 'd', '!'};
    private static final String VALUE = "Hello, World!";

    private static final StringSerializer SERIALIZER = new StringSerializer();

    @Test
    void deserialize_ShouldDeserializeString() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(SERIALIZED_VALUE);
        DataInputStream dataIn = new DataInputStream(in);

        String value = SERIALIZER.deserialize(dataIn);

        assertEquals(VALUE, value);
    }

    @Test
    void deserialize_ShouldThrowException_WhenStringIsTooLongUsingDefaultMaxLength() {
        byte[] value = IntegerUtils.toByteArray(MAX_LENGTH + 1);

        ByteArrayInputStream in = new ByteArrayInputStream(value);
        DataInputStream dataIn = new DataInputStream(in);

        assertThrowsExactly(IOException.class, () -> SERIALIZER.deserialize(dataIn),
                "String is too long. Waiting for maximum %d characters, received %d."
                        .formatted(MAX_LENGTH, MAX_LENGTH + 1)
        );
    }

    @Test
    void deserialize_ShouldThrowException_WhenStringIsTooLongUsingGivenMaxLength() {
        int maxLength = 100;
        StringSerializer serializer = new StringSerializer(maxLength);

        byte[] value = IntegerUtils.toByteArray(maxLength + 1);

        ByteArrayInputStream in = new ByteArrayInputStream(value);
        DataInputStream dataIn = new DataInputStream(in);

        assertThrowsExactly(IOException.class, () -> serializer.deserialize(dataIn),
                "String is too long. Waiting for maximum %d characters, received %d."
                        .formatted(maxLength, maxLength + 1)
        );
    }

    @Test
    void serialize_ShouldSerializeString() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);

        SERIALIZER.serialize(dataOut, VALUE);

        byte[] bytes = out.toByteArray();

        assertEquals(SERIALIZED_VALUE.length, bytes.length);
        assertArrayEquals(SERIALIZED_VALUE, bytes);
    }
}
