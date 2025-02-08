package org.cafiaso.server.network.serializers;

import org.cafiaso.server.network.Serializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Optional;

/**
 * A {@link Serializer} for prefixed optional values.
 * <p>
 * Prefixed optional values are a boolean indicating if the value is present, followed by the value itself.
 *
 * @param <T> The type of the value.
 * @param <D> The type of the {@link Serializer} for the value.
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Data_types#Type:Prefixed_Optional">Prefixed Optional</a>
 */
public class PrefixedOptionalSerializer<T, D extends Serializer<T>> implements Serializer<Optional<T>> {

    private final D dataType;

    public PrefixedOptionalSerializer(D dataType) {
        this.dataType = dataType;
    }

    @Override
    public Optional<T> deserialize(DataInputStream in) throws IOException {
        boolean present = Serializer.BOOLEAN.deserialize(in);

        if (!present) {
            return Optional.empty();
        }

        return Optional.of(dataType.deserialize(in));
    }

    @Override
    public void serialize(DataOutputStream out, Optional<T> value) throws IOException {
        Serializer.BOOLEAN.serialize(out, value.isPresent());

        if (value.isPresent()) {
            dataType.serialize(out, value.get());
        }
    }
}
