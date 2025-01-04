package org.cafiaso.server.network.packets.server.status;

import org.cafiaso.server.network.ServerPacket;
import org.cafiaso.server.network.DataType;

import java.io.DataOutputStream;
import java.io.IOException;

public class PingResponsePacket extends ServerPacket {

    private final long payload;

    public PingResponsePacket(long payload) {
        super(0x01);

        this.payload = payload;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        DataType.LONG.write(out, payload);
    }

    @Override
    public String toString() {
        return "PingResponsePacket{" +
                "payload=" + payload +
                '}';
    }
}
