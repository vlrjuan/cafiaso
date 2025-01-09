package org.cafiaso.server.network.packet.client.status;

import org.cafiaso.server.network.buffers.InputBuffer;
import org.cafiaso.server.network.packet.client.ClientPacket;

import java.io.IOException;

public class StatusRequestPacket implements ClientPacket {

    @Override
    public void read(InputBuffer buffer) throws IOException {
        // No data to read
    }

    @Override
    public String toString() {
        return "StatusRequestPacket{}";
    }
}
