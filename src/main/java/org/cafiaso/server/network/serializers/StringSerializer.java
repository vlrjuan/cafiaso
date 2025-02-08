package org.cafiaso.server.network.serializers;

import org.cafiaso.server.network.Serializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * A {@link Serializer} for {@link String} values.
 * <p>
 * Strings are values encoded as a length-prefixed byte array. The length of the string is encoded as a
 * {@code VarInt}.
 *
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Type:String">String Type</a>
 */
public class StringSerializer implements Serializer<String> {

    /**
     * Maximum length of a string.
     * <p>
     * <code>MAX_LENGTH = 3 * number of characters in a string + 3 (VarInt maximum size)</code>
     */
    public static final int MAX_LENGTH = 3 * 32767 + 3;

    private final int maxLength;

    /**
     * StringTypeSerializer constructor.
     *
     * @param maxLength the maximum length of a string
     */
    public StringSerializer(int maxLength) {
        this.maxLength = maxLength;
    }

    /**
     * StringTypeSerializer constructor.
     */
    public StringSerializer() {
        this(MAX_LENGTH);
    }

    @Override
    public String deserialize(DataInputStream in) throws IOException {
        // Read the length of the string
        int length = Serializer.VAR_INT.deserialize(in);

        if (length > maxLength) {
            throw new IOException("String is too long. Waiting for maximum %d characters, received %d.".formatted(maxLength, length));
        }

        // Read the content itself
        byte[] value = new byte[length];
        in.readFully(value);

        return new String(value); // Default charset is UTF-8
    }

    @Override
    public void serialize(DataOutputStream out, String value) throws IOException {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);

        // Write the length of the string
        Serializer.VAR_INT.serialize(out, bytes.length);

        // Write the content itself
        out.write(bytes);
    }
}
