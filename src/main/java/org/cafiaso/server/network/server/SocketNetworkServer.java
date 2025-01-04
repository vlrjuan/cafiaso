package org.cafiaso.server.network.server;

import org.cafiaso.server.Server;
import org.cafiaso.server.network.connection.SocketConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of a {@link NetworkServer} that uses sockets.
 */
public class SocketNetworkServer extends AbstractNetworkServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketNetworkServer.class);

    /**
     * The timeout for the socket connections in milliseconds.
     * <p>
     * If no data is received within this time, the connection will be closed.
     */
    private static final int TIMEOUT = (int) TimeUnit.SECONDS.toMillis(15);

    /**
     * The server instance.
     */
    private final Server server;

    /**
     * The server socket channel.
     */
    private ServerSocketChannel serverSocketChannel;

    /**
     * SocketNetworkServer constructor.
     *
     * @param server the server instance
     */
    public SocketNetworkServer(Server server) {
        this.server = server;
    }

    @Override
    public void bind(String host, int port) throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(host, port));

        Thread.startVirtualThread(() -> {
            while (serverSocketChannel.isOpen()) {
                try {
                    SocketChannel clientSocketChannel = serverSocketChannel.accept();
                    closeConnectionIfExist(clientSocketChannel.socket().getInetAddress());

                    // Set socket options
                    Socket clientSocket = clientSocketChannel.socket();
                    clientSocket.setSoTimeout(TIMEOUT);
                    clientSocket.setTcpNoDelay(true);

                    SocketConnection connection = new SocketConnection(server, clientSocketChannel);
                    acceptConnection(connection);
                } catch (IOException e) {
                    if (serverSocketChannel.isOpen()) {
                        LOGGER.error("Failed to accept connection", e);
                    }
                }
            }
        });
    }

    @Override
    public void close() throws IOException {
        super.close();

        if (serverSocketChannel != null && serverSocketChannel.isOpen()) {
            serverSocketChannel.close();
        }
    }
}
