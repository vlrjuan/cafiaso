package org.cafiaso.server.player;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerManagerImplTest {

    @Test
    void getOnlinePlayers_ShouldReturnOnlinePlayers() {
        PlayerManagerImpl playerManager = new PlayerManagerImpl();

        assertEquals(0, playerManager.getOnlinePlayers());
    }
}
