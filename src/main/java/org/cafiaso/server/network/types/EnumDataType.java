package org.cafiaso.server.network.types;

import org.cafiaso.server.network.DataType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.function.Function;

public class EnumDataType<E extends Enum<E>, T, D extends DataType<T>> implements DataType<E> {

    private final Class<E> enumClass;
    private final D dataType;
    private final Function<E, T> getter;

    public EnumDataType(Class<E> enumClass, D dataType, Function<E, T> getter) {
        this.enumClass = enumClass;
        this.dataType = dataType;
        this.getter = getter;
    }

    @Override
    public E read(DataInputStream in) throws IOException {
        E[] constants = enumClass.getEnumConstants();
        T value = dataType.read(in);

        for (E constant : constants) {
            if (getter.apply(constant).equals(value)) {
                return constant;
            }
        }

        throw new IOException("Invalid enum value");
    }

    @Override
    public void write(DataOutputStream out, E value) throws IOException {
        dataType.write(out, getter.apply(value));
    }
}
