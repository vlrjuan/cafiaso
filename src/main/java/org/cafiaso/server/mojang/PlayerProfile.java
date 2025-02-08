package org.cafiaso.server.mojang;

import org.cafiaso.server.utils.UUIDUtils;

import java.util.UUID;

/**
 * Represents the profile of a player.
 *
 * @param id         the player UUID without dashes
 * @param name       the player name, case-sensitive
 * @param properties a list of player {@link Property}s
 * @see <a href="https://minecraft.wiki/w/Mojang_API#Query_player's_skin_and_cape">Mojang API</a>
 */
public record PlayerProfile(String id, String name, Property[] properties) {

    /**
     * Represents a property of the user.
     *
     * @param name      the name of the property. For now, the only property that exists is <code>textures</code>
     * @param value     Base64 encoded JSON string containing all player textures
     * @param signature signature signed with Yggdrasil private key as Base64 string
     */
    public record Property(String name, String value, String signature) {

    }

    /**
     * Gets the ID of the user.
     *
     * @return the user ID
     */
    public UUID getId() {
        return UUIDUtils.fromStringWithoutDashes(id);
    }
}
