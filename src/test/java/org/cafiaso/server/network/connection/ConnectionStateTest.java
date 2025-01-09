package org.cafiaso.server.network.connection;

import org.cafiaso.server.network.packet.client.ClientPacket;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionStateTest {

    @Test
    void getPacketById_ShouldReturnPacket_WhenPacketIsRegistered() {
        ConnectionState state = ConnectionState.HANDSHAKE;

        ConnectionState.PacketEntry<ClientPacket> packetEntry = state.getPacketById(0x00);
        assertNotNull(packetEntry);

        ClientPacket packet = packetEntry.packet();
        assertEquals("HandshakePacket", packet.getClass().getSimpleName());

        // Switch to ConnectionState.STATUS
        state = ConnectionState.STATUS;

        packetEntry = state.getPacketById(0x00);
        assertNotNull(packetEntry);

        packet = packetEntry.packet();
        assertEquals("StatusRequestPacket", packet.getClass().getSimpleName());
    }

    @Test
    void getPacketById_ShouldReturnNull_WhenPacketIsNotRegistered() {
        ConnectionState state = ConnectionState.LOGIN;

        ConnectionState.PacketEntry<ClientPacket> packetEntry = state.getPacketById(0xFF);
        assertNull(packetEntry);
    }
}
