package com.cafiaso.server.network.registry;

import com.cafiaso.server.network.connection.ConnectionState;
import com.cafiaso.server.network.protocol.PacketHandler;
import com.cafiaso.server.network.protocol.PacketDeserializer;
import com.google.inject.Provider;

/**
 * A binding between a packet resource ID and its deserializer and handler.
 *
 * @param state                the connection state
 * @param resourceId           the packet resource ID
 * @param deserializerProvider the packet deserializer provider
 * @param handlerProvider      the packet handler provider
 * @param <P>                  the packet type
 */
public record PacketBinding<P>(
        ConnectionState state,
        String resourceId,
        Provider<? extends PacketDeserializer<P>> deserializerProvider,
        Provider<? extends PacketHandler<P>> handlerProvider
) {

    /**
     * Gets the packet deserializer.
     *
     * @return the packet deserializer
     */
    public PacketDeserializer<P> getDeserializer() {
        return deserializerProvider.get();
    }

    /**
     * Gets the packet handler.
     *
     * @return the packet handler
     */
    public PacketHandler<P> getHandler() {
        return handlerProvider.get();
    }
}
