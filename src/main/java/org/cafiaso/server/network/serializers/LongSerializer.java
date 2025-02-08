package org.cafiaso.server.network.serializers;

import org.cafiaso.server.network.Serializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * A {@link Serializer} for {@link Long} values.
 *
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Type:Long">Long Type</a>
 */
public class LongSerializer implements Serializer<Long> {

    @Override
    public Long deserialize(DataInputStream in) throws IOException {
        return in.readLong();
    }

    @Override
    public void serialize(DataOutputStream out, Long value) throws IOException {
        out.writeLong(value);
    }
}
