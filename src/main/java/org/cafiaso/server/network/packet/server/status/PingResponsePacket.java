package org.cafiaso.server.network.packet.server.status;

import org.cafiaso.server.network.DataType;
import org.cafiaso.server.network.buffers.OutputBuffer;
import org.cafiaso.server.network.packet.server.ServerPacket;

import java.io.IOException;

public record PingResponsePacket(long payload) implements ServerPacket {

    @Override
    public int getId() {
        return 0x01;
    }

    @Override
    public void write(OutputBuffer buffer) throws IOException {
        buffer.write(DataType.LONG, payload);
    }

    @Override
    public String toString() {
        return "PingResponsePacket{" +
                "payload=" + payload +
                '}';
    }
}
