package org.cafiaso.server.network.packets.client.status;

import org.cafiaso.server.network.ClientPacket;

import java.io.DataInputStream;
import java.io.IOException;

public class StatusRequestPacket implements ClientPacket {

    @Override
    public void read(DataInputStream in) throws IOException {
        // No data to read
    }

    @Override
    public String toString() {
        return "StatusRequestPacket{}";
    }
}
