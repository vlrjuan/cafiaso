package com.cafiaso.server.network.server;

import com.cafiaso.server.network.connection.ConnectionRegistry;
import com.cafiaso.server.network.connection.SocketConnection;
import com.cafiaso.server.network.processor.PacketProcessorFactory;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;

/**
 * Default {@link NetworkServer} implementation using Java NIO.
 */
public class SocketServer extends NetworkServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketServer.class);

    private static final int TIMEOUT = 5000;

    private ServerSocketChannel channel;

    private boolean running = false;

    @Inject
    public SocketServer(
            ExecutorService executorService,
            ConnectionRegistry connectionRegistry,
            PacketProcessorFactory packetProcessorFactory
    ) {
        super(executorService, connectionRegistry, packetProcessorFactory);
    }

    @Override
    public void bind(String host, int port) throws IOException {
        channel = ServerSocketChannel.open();
        channel.bind(new InetSocketAddress(host, port));

        running = true;

        LOGGER.info("Listening connections on {}", channel.getLocalAddress());

        // Listen to connections in a separate thread
        new Thread(() -> {
            while (running) {
                try {
                    // Accept a new connection
                    SocketChannel clientChannel = channel.accept();

                    Socket socket = clientChannel.socket();
                    socket.setSoTimeout(TIMEOUT); // If the client doesn't send data within TIMEOUT milliseconds, close the connection
                    socket.setTcpNoDelay(true); // Disable Nagle's algorithm (packets are sent immediately)

                    SocketConnection connection = new SocketConnection(socket);
                    acceptConnection(connection);
                } catch (IOException e) {
                    LOGGER.error("Failed to accept connection", e);
                }
            }
        }).start();
    }

    @Override
    public void close() throws IOException {
        if (channel == null || !channel.isOpen()) {
            return;
        }

        super.close();

        running = false;

        channel.close();

        LOGGER.info("Stopped listening for connections");
    }
}
