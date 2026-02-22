package com.cafiaso.server.icon;

import java.util.Optional;

/**
 * The server icon.
 * The server icon is displayed in the server list of the Minecraft client.
 *
 * @see PngCachedServerIcon
 */
public interface ServerIcon {

    /**
     * Returns the server icon as a base64-encoded string.
     *
     * @return an {@link Optional} containing the base64-encoded icon string,
     * or an empty {@link Optional} if an error occurred while loading the icon
     */
    Optional<String> getData();
}
