package com.cafiaso.server.network.protocol;

import com.cafiaso.server.network.connection.ConnectionState;

import java.util.Map;

/**
 * Defines the mappings between packet resource IDs and their corresponding numeric IDs
 * for both clientbound and serverbound packets, organized by connection state.
 * <p>
 * This interface is typically implemented by generated code based on protocol definition files.
 */
public interface PacketMappings {

    /**
     * Gets the clientbound packet mappings.
     *
     * @return a map where the key is the connection state and the value is another map
     * mapping packet resource IDs to their numeric IDs
     */
    Map<ConnectionState, Map<String, Integer>> clientbound();

    /**
     * Gets the serverbound packet mappings.
     *
     * @return a map where the key is the connection state and the value is another map
     * mapping packet numeric IDs to their resource IDs
     */
    Map<ConnectionState, Map<Integer, String>> serverbound();
}
