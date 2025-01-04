package org.cafiaso.server.network.types;

import org.cafiaso.server.network.DataType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class UnsignedShortDataType implements DataType<Integer> {

    @Override
    public Integer read(DataInputStream in) throws IOException {
        return in.readUnsignedShort();
    }

    @Override
    public void write(DataOutputStream out, Integer value) throws IOException {
        out.writeShort(value);
    }
}
