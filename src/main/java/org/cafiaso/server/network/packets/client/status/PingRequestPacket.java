package org.cafiaso.server.network.packets.client.status;

import org.cafiaso.server.network.ClientPacket;
import org.cafiaso.server.network.DataType;

import java.io.DataInputStream;
import java.io.IOException;

public class PingRequestPacket implements ClientPacket {

    private long payload;

    @Override
    public void read(DataInputStream in) throws IOException {
        payload = DataType.LONG.read(in);
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
