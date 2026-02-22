package com.cafiaso.server.network.data.types;

import com.cafiaso.server.network.data.DataType;
import com.cafiaso.server.network.io.NetworkBuffer;

import java.io.IOException;
import java.util.function.Function;

/**
 * A {@link DataType} for {@link Enum} values.
 * <p>
 * Example usage for a {@code VarInt} enum:
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
 *   DataType<TestEnum> type = new EnumDataType<>(TestEnum.class, DataTypes.VAR_INT, TestEnum::getValue);
 * }</pre>
 *
 * @param <E> the enum type
 * @param <T> the type of the data
 * @param <D> the DataType for the data
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Type:Enum">Enum Type</a>
 */
public class EnumDataType<E extends Enum<E>, T, D extends DataType<T>> implements DataType<E> {

    private final Class<E> enumClass;
    private final D type;
    private final Function<E, T> mapper;

    public EnumDataType(Class<E> enumClass, D type, Function<E, T> mapper) {
        this.enumClass = enumClass;
        this.type = type;
        this.mapper = mapper;
    }

    @Override
    public Class<E> getType() {
        return enumClass;
    }

    @Override
    public E read(NetworkBuffer in) throws IOException {
        E[] constants = enumClass.getEnumConstants();
        T value = in.read(type);

        for (E constant : constants) {
            if (mapper.apply(constant).equals(value)) {
                // Check if the value matches the enum constant
                // If it does, return the constant
                return constant;
            }
        }

        throw new IOException("Invalid enum value");
    }

    @Override
    public void write(E value, NetworkBuffer out) throws IOException {
        out.write(type, mapper.apply(value));
    }
}
