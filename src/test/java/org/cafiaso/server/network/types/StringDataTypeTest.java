package org.cafiaso.server.network.types;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static org.cafiaso.server.network.types.StringDataType.MAX_LENGTH;
import static org.junit.jupiter.api.Assertions.*;

class StringDataTypeTest {

    private static final String VALUE = "Hello, World!";
    private static final byte[] DATA = {0x0D, 'H', 'e', 'l', 'l', 'o', ',', ' ', 'W', 'o', 'r', 'l', 'd', '!'};

    private static final StringDataType DATA_TYPE = new StringDataType();

    @Test
    void read_ShouldReadString() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(DATA);
        DataInputStream dataIn = new DataInputStream(in);

        String value = DATA_TYPE.read(dataIn);

        assertEquals(VALUE, value);
    }

    @Test
    void read_ShouldThrowException_WhenStringIsTooLongUsingDefaultMaxLength() {
        ByteArrayInputStream in = new ByteArrayInputStream(VarIntDataType.toByteArray(MAX_LENGTH + 1));
        DataInputStream dataIn = new DataInputStream(in);

        assertThrowsExactly(IOException.class, () -> DATA_TYPE.read(dataIn),
                "String is too long. Waiting for maximum %d characters, received %d."
                        .formatted(MAX_LENGTH, MAX_LENGTH + 1)
        );
    }

    @Test
    void read_ShouldThrowException_WhenStringIsTooLongUsingGivenMaxLength() {
        int maxLength = 100;

        StringDataType dataType = new StringDataType(maxLength);

        ByteArrayInputStream in = new ByteArrayInputStream(VarIntDataType.toByteArray(maxLength + 1));
        DataInputStream dataIn = new DataInputStream(in);

        assertThrowsExactly(IOException.class, () -> dataType.read(dataIn),
                "String is too long. Waiting for maximum %d characters, received %d."
                        .formatted(maxLength, maxLength + 1)
        );
    }

    @Test
    void write_ShouldWriteString() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);

        DATA_TYPE.write(dataOut, VALUE);

        byte[] bytes = out.toByteArray();

        assertEquals(DATA.length, bytes.length);
        assertArrayEquals(DATA, bytes);
    }
}
