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
 * {@link NetworkServer} implementation that uses sockets.
 */
public class SocketNetworkServer extends AbstractNetworkServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketNetworkServer.class);

    private static final int TIMEOUT = (int) TimeUnit.SECONDS.toMillis(15);

    private final Server server;

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

        LOGGER.info("Socket server bound to {}:{}", host, port);

        while (serverSocketChannel.isOpen()) {
            try {
                // Accept a new incoming connection
                SocketChannel clientSocketChannel = serverSocketChannel.accept();

                // Set socket options
                Socket clientSocket = clientSocketChannel.socket();
                clientSocket.setSoTimeout(TIMEOUT); // If no data is received within this time, the connection will be closed
                clientSocket.setTcpNoDelay(true);

                SocketConnection connection = new SocketConnection(server, clientSocketChannel);
                acceptConnection(connection);
            } catch (IOException e) {
                if (serverSocketChannel.isOpen()) {
                    LOGGER.error("Failed to accept connection", e);
                }
            }
        }
    }

    @Override
    public void close() throws IOException {
        super.close();

        if (serverSocketChannel != null) {
            serverSocketChannel.close();
        }
    }
}
