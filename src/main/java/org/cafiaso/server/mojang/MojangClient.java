package org.cafiaso.server.mojang;

/**
 * A client to interact with the Mojang API.
 */
public interface MojangClient {

    /**
     * Verifies the client login session.
     *
     * @param username the player username
     * @param serverId the server ID
     * @param ip       the player IP
     * @return the player profile
     * @throws RuntimeException if an error occurs while verifying the login session
     */
    PlayerProfile verifyClientLoginSession(String username, String serverId, String ip);
}
