package org.cafiaso.server.network.serializers;

import org.cafiaso.server.network.Serializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * A {@link Serializer} for {@code Identifier} values.
 * <p>
 * Identifiers are a namespaced location, in the form of {@code minecraft:thing}.
 *
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Data_types#Type:Identifier">Identifier Type</a>
 */
public class IdentifierSerializer implements Serializer<String> {

    @Override
    public String deserialize(DataInputStream in) throws IOException {
        return Serializer.STRING.deserialize(in);
    }

    @Override
    public void serialize(DataOutputStream out, String value) throws IOException {
        Serializer.STRING.serialize(out, value);
    }
}
