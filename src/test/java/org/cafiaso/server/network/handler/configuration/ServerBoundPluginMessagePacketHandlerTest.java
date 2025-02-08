package org.cafiaso.server.network.handler.configuration;

import org.cafiaso.server.network.packet.client.configuration.ServerBoundPluginMessagePacket;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServerBoundPluginMessagePacketHandlerTest {

    private static final ServerBoundPluginMessagePacketHandler HANDLER = new ServerBoundPluginMessagePacketHandler();

    @Test
    void handle_ShouldHandlePacket() {
        ServerBoundPluginMessagePacket packet = new ServerBoundPluginMessagePacket();
        assertDoesNotThrow(() -> HANDLER.handle(null, packet));
    }
}
