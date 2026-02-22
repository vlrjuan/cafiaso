package com.cafiaso.server.network.processor;

import java.io.IOException;

/**
 * A processor for reading packets from a connection and handling them.
 */
public interface PacketProcessor {

    /**
     * Reads and handles {@code n} packets from the connection.
     * <p>
     * This method will return once no more packets are available (either because the buffer is empty
     * or because a packet has not been fully sent yet).
     *
     * @throws IOException if an I/O error occurs while reading from the connection
     */
    void process() throws IOException;
}
