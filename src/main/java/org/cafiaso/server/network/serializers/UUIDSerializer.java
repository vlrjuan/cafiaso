package org.cafiaso.server.network.serializers;

import org.cafiaso.server.network.Serializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * A {@link Serializer} for {@link UUID} values.
 *
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Data_types#Type:UUID>UUID Type</a>
 */
public class UUIDSerializer implements Serializer<UUID> {

    @Override
    public UUID deserialize(DataInputStream in) throws IOException {
        long mostSignificantBits = Serializer.LONG.deserialize(in);
        long leastSignificantBits = Serializer.LONG.deserialize(in);

        return new UUID(mostSignificantBits, leastSignificantBits);
    }

    @Override
    public void serialize(DataOutputStream out, UUID value) throws IOException {
        Serializer.LONG.serialize(out, value.getMostSignificantBits());
        Serializer.LONG.serialize(out, value.getLeastSignificantBits());
    }
}
