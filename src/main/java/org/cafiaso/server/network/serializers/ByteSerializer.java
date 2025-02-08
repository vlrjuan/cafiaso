package org.cafiaso.server.network.serializers;

import org.cafiaso.server.network.Serializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * A {@link Serializer} for {@link Byte} values.
 *
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Data_types#Type:Byte>Byte Type</a>
 */
public class ByteSerializer implements Serializer<Byte> {

    @Override
    public Byte deserialize(DataInputStream in) throws IOException {
        return in.readByte();
    }

    @Override
    public void serialize(DataOutputStream out, Byte value) throws IOException {
        out.writeByte(value);
    }
}
