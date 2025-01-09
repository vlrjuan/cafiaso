package org.cafiaso.server.network.types;

import org.cafiaso.server.network.DataType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class StringDataType implements DataType<String> {

    @Override
    public String read(DataInputStream in) throws IOException {
        int length = DataType.VAR_INT.read(in);
        byte[] value = new byte[length];
        in.readFully(value);

        return new String(value, StandardCharsets.UTF_8);
    }

    @Override
    public void write(DataOutputStream out, String value) throws IOException {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        DataType.VAR_INT.write(out, bytes.length);
        out.write(bytes);
    }
}
