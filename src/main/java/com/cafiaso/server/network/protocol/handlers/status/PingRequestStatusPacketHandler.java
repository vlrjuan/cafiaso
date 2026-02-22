package com.cafiaso.server.network.protocol.handlers.status;

import com.cafiaso.server.network.connection.Connection;
import com.cafiaso.server.network.connection.ConnectionRegistry;
import com.cafiaso.server.network.dispatcher.PacketDispatcher;
import com.cafiaso.server.network.protocol.PacketHandler;
import com.cafiaso.server.network.protocol.packets.clientbound.status.PongResponsePacket;
import com.cafiaso.server.network.protocol.packets.serverbound.status.PingRequestStatusPacket;
import jakarta.inject.Inject;

import java.io.IOException;

public class PingRequestStatusPacketHandler implements PacketHandler<PingRequestStatusPacket> {

    private final PacketDispatcher packetDispatcher;
    private final ConnectionRegistry connectionRegistry;

    @Inject
    public PingRequestStatusPacketHandler(PacketDispatcher packetDispatcher, ConnectionRegistry connectionRegistry) {
        this.packetDispatcher = packetDispatcher;
        this.connectionRegistry = connectionRegistry;
    }

    @Override
    public void handle(Connection connection, PingRequestStatusPacket packet) throws IOException {
        long payload = packet.payload();

        // Send a pong response back to the client using the same payload
        packetDispatcher.dispatch(connection, new PongResponsePacket(payload));

        // Close the connection after sending the response
        connectionRegistry.closeConnection(connection);
    }
}
