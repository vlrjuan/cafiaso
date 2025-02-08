package org.cafiaso.server.network.serializers;

import org.cafiaso.server.network.Serializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * A {@link Serializer} for prefixed byte array values.
 * <p>
 * Prefixed byte arrays are a length-prefixed byte array. The length of the byte array is encoded as a
 * {@code VarInt}.
 *
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Data_types#Type:Prefixed_Array">Prefixed Array Type</a>
 */
public class PrefixedByteArraySerializer implements Serializer<byte[]> {

    @Override
    public byte[] deserialize(DataInputStream in) throws IOException {
        int length = Serializer.VAR_INT.deserialize(in);

        byte[] value = new byte[length];
        in.readFully(value);

        return value;
    }

    @Override
    public void serialize(DataOutputStream out, byte[] value) throws IOException {
        Serializer.VAR_INT.serialize(out, value.length);
        out.write(value);
    }
}
