package org.cafiaso.server.network.packets.client.handshake;

import org.cafiaso.server.network.ClientPacket;
import org.cafiaso.server.network.DataType;

import java.io.DataInputStream;
import java.io.IOException;

public class LegacyServerListPingPacket implements ClientPacket {

    private int payload;

    @Override
    public void read(DataInputStream in) throws IOException {
        payload = DataType.UNSIGNED_BYTE.read(in);
    }

    @Override
    public String toString() {
        return "LegacyServerListPingPacket{" +
                "payload=" + payload +
                '}';
    }
}
