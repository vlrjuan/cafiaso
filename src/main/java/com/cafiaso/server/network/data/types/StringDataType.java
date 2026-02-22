package com.cafiaso.server.network.data.types;

import com.cafiaso.server.network.data.DataType;
import com.cafiaso.server.network.data.DataTypes;
import com.cafiaso.server.network.io.NetworkBuffer;

import java.io.IOException;

/**
 * A {@link DataType} for {@link String} values.
 * <p>
 * Strings are values encoded as a length-prefixed byte array. The length of the string is encoded as a
 * {@code VarInt}.
 *
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Type:String">String Type</a>
 */
public class StringDataType implements DataType<String> {

    public static final int MAX_LENGTH = 3 * 32767 + 3; // 3 * number of characters in a string + 3 (VarInt maximum size)

    private final int maxLength;

    public StringDataType(int maxLength) {
        this.maxLength = maxLength;
    }

    public StringDataType() {
        this(MAX_LENGTH);
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Override
    public String read(NetworkBuffer in) throws IOException {
        // Read the length of the string
        int length = in.read(DataTypes.VAR_INT);

        if (length > maxLength) {
            throw new IOException("String is too long. Waiting for maximum %d characters, received %d.".formatted(maxLength, length));
        }

        // Read the content itself
        return new String(in.readBytes(length));
    }

    @Override
    public void write(String value, NetworkBuffer out) throws IOException {
        byte[] bytes = value.getBytes();

        // Write the length of the string
        out.write(DataTypes.VAR_INT, bytes.length);

        // Write the content itself
        out.writeBytes(bytes);
    }
}
