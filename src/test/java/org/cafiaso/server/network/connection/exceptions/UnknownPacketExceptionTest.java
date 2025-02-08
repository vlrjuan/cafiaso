package org.cafiaso.server.network.connection.exceptions;

import org.cafiaso.server.network.connection.ConnectionState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnknownPacketExceptionTest {

    @Test
    void constructor_ShouldCreateExceptionWithCorrectMessage() {
        int packetId = 0x01;
        ConnectionState connectionState = ConnectionState.HANDSHAKE;
        UnknownPacketException exception = new UnknownPacketException(packetId, connectionState);

        assertEquals(
                "Received unknown packet with ID 0x1. Please check the packet ID mapping for state HANDSHAKE.",
                exception.getMessage()
        );
    }
}
