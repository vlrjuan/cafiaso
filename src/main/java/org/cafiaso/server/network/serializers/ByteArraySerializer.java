package org.cafiaso.server.network.serializers;

import org.cafiaso.server.network.Serializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * A {@link Serializer} for byte arrays.
 * <p>
 * A byte array is a sequence of 0 or more bytes. The length of the byte array is not explicitly encoded
 * and must be inferred from the context in which the byte array is used.
 *
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Data_types#Type:Byte_Array">Byte Array Type</a>
 */
public class ByteArraySerializer implements Serializer<byte[]> {

    private final int size;

    /**
     * ByteArraySerializer constructor.
     *
     * @param size The size of the byte array.
     */
    public ByteArraySerializer(int size) {
        this.size = size;
    }

    public ByteArraySerializer() {
        this(-1);
    }

    @Override
    public byte[] deserialize(DataInputStream in) throws IOException {
        byte[] value = new byte[size == -1 ? in.available() : size];
        in.readFully(value);

        return value;
    }

    @Override
    public void serialize(DataOutputStream out, byte[] value) throws IOException {
        out.write(value);
    }
}
