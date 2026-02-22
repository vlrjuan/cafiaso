package com.cafiaso.server.network.connection;

/**
 * A connection state.
 */
public enum ConnectionState {

    /**
     * Initial handshake between the client and the server.
     *
     * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Handshaking">Handshaking</a>
     */
    HANDSHAKE,
    /**
     * Initial status request and ping.
     *
     * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Status">Status</a>
     */
    STATUS,
    /**
     * Login process.
     *
     * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Login">Login</a>
     */
    LOGIN,
    /**
     * Configuration process.
     *
     * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Configuration">Configuration</a>
     */
    CONFIGURATION,
    /**
     * Main game interaction.
     *
     * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Play">Play</a>
     */
    PLAY
}
