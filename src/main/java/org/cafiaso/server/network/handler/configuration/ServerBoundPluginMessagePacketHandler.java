package org.cafiaso.server.network.handler.configuration;

import org.cafiaso.server.network.connection.Connection;
import org.cafiaso.server.network.handler.PacketHandler;
import org.cafiaso.server.network.packet.client.configuration.ServerBoundPluginMessagePacket;

import java.io.IOException;

/**
 * Handles the {@link ServerBoundPluginMessagePacket}.
 */
public class ServerBoundPluginMessagePacketHandler implements PacketHandler<ServerBoundPluginMessagePacket> {

    @Override
    public void handle(Connection connection, ServerBoundPluginMessagePacket packet) throws IOException {
        // TODO: Implement
    }
}
