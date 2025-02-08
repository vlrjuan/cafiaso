package org.cafiaso.server.network.connection.exceptions;

import org.cafiaso.server.network.connection.ConnectionState;
import org.cafiaso.server.utils.HexUtils;

/**
 * Thrown when an unknown packet is read for the current connection state.
 */
public class UnknownPacketException extends RuntimeException {

    public UnknownPacketException(int packetId, ConnectionState connectionState) {
        super(
                "Received unknown packet with ID %s. Please check the packet ID mapping for state %s."
                        .formatted(HexUtils.toHexString(packetId), connectionState)
        );
    }
}
