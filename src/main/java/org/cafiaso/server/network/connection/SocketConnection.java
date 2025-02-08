package org.cafiaso.server.network.connection;

import org.cafiaso.server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * {@link AbstractConnection} implementation that uses a {@link SocketChannel} to communicate with the client.
 */
public class SocketConnection extends AbstractConnection {

    private final SocketChannel channel;

    /**
     * SocketConnection constructor.
     *
     * @param server  the server instance
     * @param channel the socket channel
     */
    public SocketConnection(Server server, SocketChannel channel) throws IOException {
        super(server, channel.socket().getInetAddress(),
                new DataInputStream(channel.socket().getInputStream()),
                new DataOutputStream(channel.socket().getOutputStream())
        );

        this.channel = channel;
    }

    @Override
    public boolean isOpen() {
        return channel.isOpen();
    }

    @Override
    public void close() throws IOException {
        if (channel.isOpen()) {
            channel.close();
        }
    }
}
