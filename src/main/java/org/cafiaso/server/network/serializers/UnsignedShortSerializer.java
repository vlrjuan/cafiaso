package org.cafiaso.server.network.serializers;

import org.cafiaso.server.network.Serializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * A {@link Serializer} for unsigned short values.
 *
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Type:Unsigned_Short">Unsigned Short Type</a>
 */
public class UnsignedShortSerializer implements Serializer<Integer> {

    @Override
    public Integer deserialize(DataInputStream in) throws IOException {
        return in.readUnsignedShort();
    }

    @Override
    public void serialize(DataOutputStream out, Integer value) throws IOException {
        out.writeShort(value);
    }
}
