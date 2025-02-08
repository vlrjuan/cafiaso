package org.cafiaso.server.network.serializers;

import org.cafiaso.server.network.Serializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * A {@link Serializer} for {@link Boolean} values.
 *
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Type:Boolean">Boolean Type</a>
 */
public class BooleanSerializer implements Serializer<Boolean> {

    @Override
    public Boolean deserialize(DataInputStream in) throws IOException {
        byte value = in.readByte();

        if (value == 0x00) {
            return false;
        } else if (value == 0x01) {
            return true;
        } else {
            throw new IOException("Invalid value");
        }
    }

    @Override
    public void serialize(DataOutputStream out, Boolean value) throws IOException {
        out.writeByte(value ? 0x01 : 0x00);
    }
}
