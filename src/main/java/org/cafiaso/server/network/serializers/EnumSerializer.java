package org.cafiaso.server.network.serializers;

import org.cafiaso.server.network.Serializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.function.Function;

/**
 * A {@link Serializer} for {@link Enum} values.
 * <p>
 * Example usage for a VarInt enum:
 * <pre>{@code
 *   public enum TestEnum {
 *     A(0x00),
 *     B(0x01),
 *     C(0x02);
 *
 *     private final int value;
 *
 *     TestEnum(int value) {
 *       this.value = value;
 *     }
 *
 *     public int getValue() {
 *       return value;
 *     }
 *   }
 *
 *   TypeSerializer<TestEnum> serializer = new EnumTypeSerializer<>(TestEnum.class, DataType.VAR_INT, TestEnum::getValue);
 * }</pre>
 *
 * @param <E> the enum type
 * @param <T> the data type
 * @param <S> the type serializer
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Type:Enum">Enum Type</a>
 */
public class EnumSerializer<E extends Enum<E>, T, S extends Serializer<T>> implements Serializer<E> {

    private final Class<E> enumClass;
    private final S serializer;
    private final Function<E, T> getter;

    /**
     * EnumTypeSerializer constructor.
     *
     * @param enumClass  the enum class
     * @param serializer the serializer for the data
     * @param getter     the getter function that maps the enum to the type of the data
     */
    public EnumSerializer(Class<E> enumClass, S serializer, Function<E, T> getter) {
        this.enumClass = enumClass;
        this.serializer = serializer;
        this.getter = getter;
    }

    @Override
    public E deserialize(DataInputStream in) throws IOException {
        E[] constants = enumClass.getEnumConstants();
        T value = serializer.deserialize(in);

        for (E constant : constants) {
            if (getter.apply(constant).equals(value)) {
                return constant;
            }
        }

        throw new IOException("Invalid enum value");
    }

    @Override
    public void serialize(DataOutputStream out, E value) throws IOException {
        serializer.serialize(out, getter.apply(value));
    }
}
