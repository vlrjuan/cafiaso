package org.cafiaso.server.network.packet.server.status;

import org.cafiaso.server.network.DataType;
import org.cafiaso.server.network.buffers.OutputBuffer;
import org.cafiaso.server.network.packet.server.ServerPacket;

import java.io.IOException;

public record StatusResponsePacket(String jsonResponse) implements ServerPacket {

    @Override
    public int getId() {
        return 0x00;
    }

    @Override
    public void write(OutputBuffer buffer) throws IOException {
        buffer.write(DataType.STRING, jsonResponse);
    }

    @Override
    public String toString() {
        return "StatusResponsePacket{" +
                "jsonResponse='" + jsonResponse + '\'' +
                '}';
    }
}
