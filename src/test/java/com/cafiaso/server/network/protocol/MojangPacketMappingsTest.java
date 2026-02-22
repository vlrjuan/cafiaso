package com.cafiaso.server.network.protocol;

import com.cafiaso.server.network.connection.ConnectionState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MojangPacketMappingsTest {

    private static final MojangPacketMappings INSTANCE = new MojangPacketMappings();

    @Test
    void clientbound_ShouldReturnCorrectMappings() {
        assertNull(INSTANCE.clientbound().get(ConnectionState.HANDSHAKE));
        assertEquals(2, INSTANCE.clientbound().get(ConnectionState.STATUS).size());
        assertEquals(1, INSTANCE.clientbound().get(ConnectionState.LOGIN).get("hello"));
    }

    @Test
    void serverbound_ShouldReturnCorrectMappings() {
        assertEquals(1, INSTANCE.serverbound().get(ConnectionState.HANDSHAKE).size());
        assertEquals(2, INSTANCE.serverbound().get(ConnectionState.STATUS).size());
        assertEquals("hello", INSTANCE.serverbound().get(ConnectionState.LOGIN).get(0));
    }
}
