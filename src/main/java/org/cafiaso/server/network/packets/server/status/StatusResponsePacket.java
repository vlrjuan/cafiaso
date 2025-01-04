package org.cafiaso.server.network.packets.server.status;

import org.cafiaso.server.network.ServerPacket;
import org.cafiaso.server.network.DataType;

import java.io.DataOutputStream;
import java.io.IOException;

public class StatusResponsePacket extends ServerPacket {

    private final String jsonResponse;

    public StatusResponsePacket(String jsonResponse) {
        super(0x00);

        this.jsonResponse = jsonResponse;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        DataType.STRING.write(out, jsonResponse);
    }

    @Override
    public String toString() {
        return "StatusResponsePacket{" +
                "jsonResponse='" + jsonResponse + '\'' +
                '}';
    }
}
