package org.cafiaso.server.network.types;

import org.cafiaso.server.network.DataType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.function.Function;

public class EnumDataType<E extends Enum<E>, T, S extends DataType<T>> implements DataType<E> {

    private final S type;
    private final Function<T, E> getter;
    private final Function<E, T> setter;

    public EnumDataType(S type, Function<T, E> getter, Function<E, T> setter) {
        this.type = type;
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public E read(DataInputStream in) throws IOException {
        T value = type.read(in);

        return getter.apply(value);
    }

    @Override
    public void write(DataOutputStream out, E value) throws IOException {
        type.write(out, setter.apply(value));
    }
}
