package com.cafiaso.server.network.server;

import com.cafiaso.server.network.connection.Connection;
import com.cafiaso.server.network.connection.ConnectionRegistry;
import com.cafiaso.server.network.processor.PacketProcessor;
import com.cafiaso.server.network.processor.PacketProcessorFactory;
import com.google.common.util.concurrent.MoreExecutors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SocketServerTest {

    @Mock
    private ConnectionRegistry connectionRegistry;

    @Mock
    private PacketProcessorFactory packetProcessorFactory;

    private SocketServer server;

    @BeforeEach
    void setUp() {
        ExecutorService executorService = MoreExecutors.newDirectExecutorService(); // Guava

        server = new SocketServer(executorService, connectionRegistry, packetProcessorFactory);
    }

    @Test
    void close_shouldCloseAllConnections() throws IOException {
        // Given
        server.bind("127.0.0.1", 25565);

        // When
        server.close();

        // Then
        verify(connectionRegistry).closeAllConnections();
    }

    @Test
    void acceptConnection_shouldRegisterAndProcessPacket() throws Exception {
        // Given
        Connection connection = mock(Connection.class);
        when(connection.isOpen()).thenReturn(true).thenReturn(false);

        PacketProcessor packetProcessor = mock(PacketProcessor.class);
        when(packetProcessorFactory.create(connection)).thenReturn(packetProcessor);

        // When
        server.acceptConnection(connection);

        // Then
        verify(connectionRegistry).addConnection(connection);
        verify(packetProcessor).process();
        verify(connectionRegistry, never()).closeConnection(connection);
    }

    @Test
    void acceptConnection_shouldCloseConnection_whenProcessThrowsIOException() throws Exception {
        // Given
        Connection connection = mock(Connection.class);
        when(connection.isOpen()).thenReturn(true).thenReturn(false);

        PacketProcessor packetProcessor = mock(PacketProcessor.class);
        when(packetProcessorFactory.create(connection)).thenReturn(packetProcessor);

        doThrow(new IOException("Read error")).when(packetProcessor).process();

        // When
        server.acceptConnection(connection);

        // Then
        verify(connectionRegistry).addConnection(connection);
        verify(packetProcessor).process();
        verify(connectionRegistry).closeConnection(connection);
    }

    @Test
    void acceptConnection_shouldCloseConnection_whenProcessThrowsSocketException() throws Exception {
        // Given
        Connection connection = mock(Connection.class);
        when(connection.isOpen()).thenReturn(true).thenReturn(false);

        PacketProcessor packetProcessor = mock(PacketProcessor.class);
        when(packetProcessorFactory.create(connection)).thenReturn(packetProcessor);

        doThrow(new SocketException("Connection reset")).when(packetProcessor).process();

        // When
        server.acceptConnection(connection);

        // Then
        verify(connectionRegistry).addConnection(connection);
        verify(packetProcessor).process();
        verify(connectionRegistry).closeConnection(connection);
    }

    @Test
    void acceptConnection_shouldProcessMultiplePackets() throws Exception {
        // Given
        Connection connection = mock(Connection.class);
        when(connection.isOpen())
                .thenReturn(true)  // 1st iteration
                .thenReturn(true)  // 2nd iteration
                .thenReturn(true)  // 3rd iteration
                .thenReturn(false); // Loop ends

        PacketProcessor packetProcessor = mock(PacketProcessor.class);
        when(packetProcessorFactory.create(connection)).thenReturn(packetProcessor);

        // When
        server.acceptConnection(connection);

        // Then
        verify(connectionRegistry).addConnection(connection);
        verify(packetProcessor, times(3)).process();
        verify(connectionRegistry, never()).closeConnection(connection);
    }
}
