package com.cafiaso.server.network.data;

import com.cafiaso.server.network.data.types.*;

import java.util.function.Supplier;

/**
 * A collection of common {@link DataType} instances.
 * <p>
 * This class provides static instances of various data types used in the protocol.
 * It also provides utility methods for creating custom data types.
 */
public class DataTypes {

    public static final DataType<Integer> UNSIGNED_SHORT = new UnsignedShortDataType();
    public static final DataType<Long> LONG = new LongDataType();
    public static final DataType<String> STRING = new StringDataType();
    public static final DataType<Integer> VAR_INT = new VarIntDataType();

    /**
     * Creates a new {@link StringDataType} with the specified maximum length.
     *
     * @param maxLength the maximum length of the string
     * @return a new {@link StringDataType}
     */
    public static DataType<String> STRING(int maxLength) {
        return new StringDataType(maxLength);
    }

    /**
     * Creates a new {@link EnumDataType} for the given enum class and data type.
     * <p>
     * This is simply a convenience method for creating an {@link EnumDataType} without
     * explicitly specifying a mapper function (which is not needed in most cases).
     * <p>
     * Example usage for a {@code VarInt} enum:
     * <pre>{@code
     * public enum TestEnum implements Supplier<Integer> {
     *   A(0x00),
     *   B(0x01),
     *   C(0x02);
     *
     *   private final int value;
     *
     *   TestEnum(int value) {
     *     this.value = value;
     *   }
     *
     *   @Override
     *   public Integer get() {
     *     return value;
     *   }
     * }
     *
     * DataType<TestEnum> type = DataTypes.ENUM(TestEnum.class, DataTypes.VAR_INT);
     * }</pre>
     *
     * @param enumClass the enum class (must implement {@link Supplier})
     * @param type      the {@link DataType} for the enum values
     * @param <E>       the enum type
     * @param <T>       the type of the data
     * @param <D>       the DataType for the data
     * @return a new {@link EnumDataType}
     */
    public static <E extends Enum<E> & Supplier<T>, T, D extends DataType<T>> DataType<E> ENUM(Class<E> enumClass, D type) {
        return new EnumDataType<>(enumClass, type, Supplier::get);
    }
}
