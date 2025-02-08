package org.cafiaso.server.network;

import org.cafiaso.server.network.serializers.BooleanSerializer;
import org.cafiaso.server.network.serializers.ByteArraySerializer;
import org.cafiaso.server.network.serializers.ByteSerializer;
import org.cafiaso.server.network.serializers.EnumSerializer;
import org.cafiaso.server.network.serializers.IdentifierSerializer;
import org.cafiaso.server.network.serializers.LongSerializer;
import org.cafiaso.server.network.serializers.PrefixedByteArraySerializer;
import org.cafiaso.server.network.serializers.PrefixedOptionalSerializer;
import org.cafiaso.server.network.serializers.StringSerializer;
import org.cafiaso.server.network.serializers.TextComponentSerializer;
import org.cafiaso.server.network.serializers.TextComponentSerializer.TextComponent;
import org.cafiaso.server.network.serializers.UUIDSerializer;
import org.cafiaso.server.network.serializers.UnsignedByteSerializer;
import org.cafiaso.server.network.serializers.UnsignedShortSerializer;
import org.cafiaso.server.network.serializers.VarIntSerializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

/**
 * Represents a serializer for a specific data type.
 *
 * @param <T> the type of the data
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Data_types">Protocol documentation</a>
 */
public interface Serializer<T> {

    Serializer<Boolean> BOOLEAN = new BooleanSerializer();
    Serializer<Byte> BYTE = new ByteSerializer();
    Serializer<Byte> UNSIGNED_BYTE = new UnsignedByteSerializer();
    Serializer<Integer> UNSIGNED_SHORT = new UnsignedShortSerializer();
    Serializer<Long> LONG = new LongSerializer();
    Serializer<String> STRING = new StringSerializer();
    Serializer<TextComponent> TEXT_COMPONENT = new TextComponentSerializer();
    Serializer<String> IDENTIFIER = new IdentifierSerializer();
    Serializer<Integer> VAR_INT = new VarIntSerializer();
    Serializer<UUID> UUID = new UUIDSerializer();
    Serializer<byte[]> BYTE_ARRAY = new ByteArraySerializer();

    // Prefixed arrays
    Serializer<byte[]> PREFIXED_BYTE_ARRAY = new PrefixedByteArraySerializer();

    /**
     * Creates a new {@link StringSerializer} instance.
     *
     * @param maxLength the maximum length of the string. If the string is longer, reading will throw an exception.
     * @return the {@link StringSerializer} instance
     */
    static Serializer<String> STRING(int maxLength) {
        return new StringSerializer(maxLength);
    }

    /**
     * Creates a new {@link PrefixedOptionalSerializer} instance.
     *
     * @param serializer the serializer used to serialize the optional value
     * @param <T>        the type of the data
     * @param <S>        the type of the serializer
     * @return the {@link PrefixedOptionalSerializer} instance
     */
    static <T, S extends Serializer<T>> Serializer<Optional<T>> OPTIONAL(S serializer) {
        return new PrefixedOptionalSerializer<>(serializer);
    }

    /**
     * Creates a new {@link EnumSerializer} instance.
     *
     * @param enumClass  the enum class
     * @param serializer the serializer used to serialize the enum
     * @param getter     the getter function that maps the enum to the type of the data
     * @param <E>        the type of the enum
     * @param <T>        the type of the data
     * @param <S>        the type of the serializer
     * @return the {@link EnumSerializer} instance
     */
    static <E extends Enum<E>, T, S extends Serializer<T>> Serializer<E> ENUM(Class<E> enumClass, S serializer, Function<E, T> getter) {
        return new EnumSerializer<>(enumClass, serializer, getter);
    }

    static Serializer<byte[]> BYTE_ARRAY(int size) {
        return new ByteArraySerializer(size);
    }

    /**
     * Deserializes a value from the input stream.
     *
     * @param in the input stream to read from
     * @return the deserialized value
     * @throws IOException if an I/O error occurs while reading the data
     */
    T deserialize(DataInputStream in) throws IOException;

    /**
     * Serializes the given value to the output stream.
     *
     * @param out   the output stream to write to
     * @param value the value to serialize
     * @throws IOException if an I/O error occurs while writing the data
     */
    void serialize(DataOutputStream out, T value) throws IOException;
}
