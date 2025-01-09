package org.cafiaso.server.network.packet.client.handshake;

import org.cafiaso.server.network.DataType;
import org.cafiaso.server.network.buffers.InputBuffer;
import org.cafiaso.server.network.packet.client.ClientPacket;

import java.io.IOException;

public class LegacyServerListPingPacket implements ClientPacket {

    private int payload;

    @Override
    public void read(InputBuffer buffer) throws IOException {
        payload = buffer.read(DataType.UNSIGNED_BYTE);
    }

    public int getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "LegacyServerListPingPacket{" +
                "payload=" + payload +
                '}';
    }
}
