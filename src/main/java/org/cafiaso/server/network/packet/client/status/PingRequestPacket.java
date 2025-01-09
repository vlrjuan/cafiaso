package org.cafiaso.server.network.packet.client.status;

import org.cafiaso.server.network.DataType;
import org.cafiaso.server.network.buffers.InputBuffer;
import org.cafiaso.server.network.packet.client.ClientPacket;

import java.io.IOException;

public class PingRequestPacket implements ClientPacket {

    private long payload;

    @Override
    public void read(InputBuffer buffer) throws IOException {
        payload = buffer.read(DataType.LONG);
    }

    @Override
    public String toString() {
        return "PingRequestPacket{" +
                "payload=" + payload +
                '}';
    }

    public long getPayload() {
        return payload;
    }
}
