package org.cafiaso.server.network.connection;

import org.cafiaso.server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;

/**
 * Implementation of {@link AbstractConnection} that uses a {@link SocketChannel} to communicate with the client.
 */
public class SocketConnection extends AbstractConnection {

    /**
     * The socket channel.
     */
    private final SocketChannel channel;

    /**
     * SocketConnection constructor.
     *
     * @param server  the server instance
     * @param channel the socket channel
     */
    public SocketConnection(Server server, SocketChannel channel) {
        super(
                server,
                channel.socket().getInetAddress(),
                new DataInputStream(Channels.newInputStream(channel)),
                new DataOutputStream(Channels.newOutputStream(channel))
        );

        this.channel = channel;
    }

    @Override
    public boolean isOpen() {
        return channel.isOpen();
    }

    @Override
    public void close() throws IOException {
        super.close();

        channel.close();
    }
}
