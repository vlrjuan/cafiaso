package com.cafiaso.server.network.dispatcher;

import com.cafiaso.server.network.connection.Connection;
import com.cafiaso.server.network.connection.ConnectionState;
import com.cafiaso.server.network.data.DataTypes;
import com.cafiaso.server.network.io.NetworkBuffer;
import com.cafiaso.server.network.protocol.SerializablePacket;
import com.cafiaso.server.network.registry.PacketRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PacketDispatcherImplTest {

    @Mock
    private PacketRegistry registry;

    @Mock
    private Connection connection;

    @InjectMocks
    private PacketDispatcherImpl dispatcher;

    @Test
    void dispatch_shouldThrowIOException_whenPacketIdNotFound() throws IOException {
        // Given
        when(connection.getState()).thenReturn(ConnectionState.PLAY);

        String resourceId = "minecraft:unknown_packet";

        TestPacket packet = new TestPacket(resourceId, "data");

        when(registry.getPacketId(ConnectionState.PLAY, resourceId))
                .thenReturn(Optional.empty());

        // When/Then
        IOException exception = assertThrows(IOException.class,
                () -> dispatcher.dispatch(connection, packet));

        assertEquals("Unknown packet resource ID: minecraft:unknown_packet",
                exception.getMessage());

        verify(connection, never()).write(any());
    }

    @Test
    void dispatch_shouldSendPacketWithCorrectFormat() throws IOException {
        // Given
        when(connection.getState()).thenReturn(ConnectionState.PLAY);

        String resourceId = "minecraft:chat_message";
        int packetId = 0x17;

        TestPacket packet = new TestPacket(resourceId, "Hello World");

        when(registry.getPacketId(ConnectionState.PLAY, resourceId))
                .thenReturn(Optional.of(packetId));

        ArgumentCaptor<NetworkBuffer> bufferCaptor = ArgumentCaptor.forClass(NetworkBuffer.class);

        // When
        dispatcher.dispatch(connection, packet);

        // Then
        verify(connection).write(bufferCaptor.capture());

        NetworkBuffer sentBuffer = bufferCaptor.getValue();

        int packetLength = sentBuffer.read(DataTypes.VAR_INT);
        int receivedPacketId = sentBuffer.read(DataTypes.VAR_INT);
        String receivedData = sentBuffer.read(DataTypes.STRING);

        assertEquals(packetId, receivedPacketId);
        assertEquals("Hello World", receivedData);

        assertTrue(packetLength > 0);

        assertFalse(sentBuffer.hasRemainingBytes());
    }

    private record TestPacket(String resourceId, String data) implements SerializablePacket {

        @Override
        public void serialize(NetworkBuffer buffer) throws IOException {
            buffer.write(DataTypes.STRING, data);
        }
    }
}
