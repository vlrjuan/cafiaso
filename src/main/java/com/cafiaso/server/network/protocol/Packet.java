package com.cafiaso.server.network.protocol;

import com.cafiaso.server.network.connection.ConnectionState;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Packet {

    /**
     * The packet connection state.
     *
     * @return the connection state
     */
    ConnectionState state() default ConnectionState.PLAY;

    /**
     * The packet resource ID.
     * <p>
     * This id refers to the unique identifier set by the Notchian client.
     * It must be unique for each packet in a given {@link ConnectionState}.
     *
     * @return the packet resource ID
     */
    String resourceId();

    /**
     * The packet deserializer class.
     *
     * @return the packet deserializer class
     * @see PacketDeserializer
     */
    Class<? extends PacketDeserializer<?>> deserializer();
}
