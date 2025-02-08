package org.cafiaso.server.network.handler.configuration;

import org.cafiaso.server.network.connection.Connection;
import org.cafiaso.server.network.handler.PacketHandler;
import org.cafiaso.server.network.packet.client.configuration.ClientInformationPacket;
import org.cafiaso.server.network.packet.server.configuration.ClientBoundPluginMessagePacket;

import java.io.IOException;

/**
 * Handles the {@link ClientInformationPacket}.
 */
public class ClientInformationPacketHandler implements PacketHandler<ClientInformationPacket> {

    private static final String BRAND = "cafiaso";

    @Override
    public void handle(Connection connection, ClientInformationPacket packet) throws IOException {
        connection.sendPacket(new ClientBoundPluginMessagePacket("minecraft:brand", BRAND.getBytes()));
    }
}
