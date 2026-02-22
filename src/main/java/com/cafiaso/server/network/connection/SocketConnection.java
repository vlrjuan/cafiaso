package com.cafiaso.server.network.connection;

import com.cafiaso.server.network.io.NetworkBuffer;

import java.io.IOException;
import java.net.Socket;

/**
 * Default {@link Connection} implementation using {@link Socket} to write and read data.
 */
public class SocketConnection extends Connection {

    private final Socket socket;

    public SocketConnection(Socket socket) {
        this.socket = socket;
    }

    @Override
    public int read(NetworkBuffer buffer) throws IOException {
        return buffer.write(socket.getChannel());
    }

    @Override
    public void write(NetworkBuffer buffer) throws IOException {
        buffer.read(socket.getChannel());
    }

    @Override
    public boolean isOpen() {
        return !socket.isClosed() && socket.isConnected();
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    @Override
    public String toString() {
        return socket.getInetAddress().toString();
    }
}
