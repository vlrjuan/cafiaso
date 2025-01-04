package org.cafiaso.server.network.types;

import org.cafiaso.server.network.DataType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class LongDataType implements DataType<Long> {

    @Override
    public Long read(DataInputStream in) throws IOException {
        return in.readLong();
    }

    @Override
    public void write(DataOutputStream out, Long value) throws IOException {
        out.writeLong(value);
    }
}
