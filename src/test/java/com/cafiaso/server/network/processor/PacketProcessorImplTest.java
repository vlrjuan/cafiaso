package com.cafiaso.server.network.processor;

import com.cafiaso.server.network.connection.Connection;
import com.cafiaso.server.network.connection.ConnectionState;
import com.cafiaso.server.network.data.DataTypes;
import com.cafiaso.server.network.io.NetworkBuffer;
import com.cafiaso.server.network.io.NetworkByteBuffer;
import com.cafiaso.server.network.protocol.PacketDeserializer;
import com.cafiaso.server.network.protocol.PacketHandler;
import com.cafiaso.server.network.registry.PacketBinding;
import com.cafiaso.server.network.registry.PacketRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
class PacketProcessorImplTest {

    @Mock
    private PacketRegistry packetRegistry;

    @Mock
    private Connection connection;

    @InjectMocks
    private PacketProcessorImpl processor;

    @Test
    void process_shouldThrowException_whenConnectionIsClosed() {
        // Given
        when(connection.isOpen()).thenReturn(false);

        // When/Then
        assertThrowsExactly(IllegalStateException.class, () -> processor.process());
    }

    @Test
    void process_shouldReturnEarly_whenNoDataRead() throws IOException {
        // Given
        when(connection.isOpen()).thenReturn(true);
        when(connection.read(any(NetworkBuffer.class))).thenReturn(-1);

        // When
        processor.process();

        // Then
        verifyNoInteractions(packetRegistry);
    }

    @Test
    void process_shouldProcessSingleCompletePacket() throws IOException {
        // Given
        when(connection.isOpen()).thenReturn(true);
        when(connection.getState()).thenReturn(ConnectionState.PLAY);

        PacketDeserializer<Object> deserializer = new TestPacketDeserializer();
        PacketHandler<Object> handler = mock(PacketHandler.class);

        int packetId = 0x00;
        String data = "data";

        setupPacket(packetId, deserializer, handler);

        NetworkBuffer buffer = createPacketBuffer(packetId, data);

        setupConnectionToReturnData(buffer);

        // When
        processor.process();

        // Then
        verify(handler).handle(connection, new TestPacket(data));
    }

    @Test
    void process_shouldProcessMultipleCompletePackets() throws IOException {
        // Given
        when(connection.isOpen()).thenReturn(true);
        when(connection.getState()).thenReturn(ConnectionState.PLAY);

        PacketDeserializer<Object> deserializer1 = new TestPacketDeserializer();
        PacketHandler<Object> handler1 = mock(PacketHandler.class);

        int packetId1 = 0x00;
        String data1 = "data1";

        setupPacket(packetId1, deserializer1, handler1);

        PacketDeserializer<Object> deserializer2 = new TestPacketDeserializer();
        PacketHandler<Object> handler2 = mock(PacketHandler.class);

        int packetId2 = 0x01;
        String data2 = "data2";

        setupPacket(packetId2, deserializer2, handler2);

        // Buffer of 2 packets
        NetworkBuffer packetBuffer1 = createPacketBuffer(packetId1, data1);
        NetworkBuffer packetBuffer2 = createPacketBuffer(packetId2, data2);

        NetworkBuffer combinedBuffer = new NetworkByteBuffer();
        combinedBuffer.write(packetBuffer1);
        combinedBuffer.write(packetBuffer2);

        setupConnectionToReturnData(combinedBuffer);

        // When
        processor.process();

        // Then
        verify(handler1).handle(connection, new TestPacket(data1));
        verify(handler2).handle(connection, new TestPacket(data2));
    }

    @Test
    void process_shouldHandleIncompletePacketLength() throws IOException {
        // Given
        when(connection.isOpen()).thenReturn(true);

        NetworkBuffer incompleteBuffer = new NetworkByteBuffer();
        incompleteBuffer.writeByte((byte) 0x80); // First byte of a multibyte VAR_INT

        setupConnectionToReturnData(incompleteBuffer);

        // When
        processor.process();

        // Then
        verifyNoInteractions(packetRegistry);
    }

    @Test
    void process_shouldHandleIncompletePacketBody() throws IOException {
        // Given
        when(connection.isOpen()).thenReturn(true);
        when(connection.getState()).thenReturn(ConnectionState.PLAY);

        PacketDeserializer<Object> deserializer = new TestPacketDeserializer();
        PacketHandler<Object> handler = mock(PacketHandler.class);

        int packetId = 0x00;
        String data = "data";

        setupPacket(packetId, deserializer, handler);

        NetworkBuffer incompleteBuffer = new NetworkByteBuffer();
        incompleteBuffer.write(DataTypes.VAR_INT, 6); // Packet length = 6 bytes (1 byte for packet ID + 5 bytes for data)
        incompleteBuffer.writeByte((byte) packetId); // Write packet ID
        incompleteBuffer.write(DataTypes.VAR_INT, 4);
        incompleteBuffer.writeBytes(new byte[]{'d', 'a'}); // Incomplete data

        setupConnectionToReturnData(incompleteBuffer);

        // When
        processor.process();

        // Then
        verifyNoInteractions(packetRegistry);

        // Second trame

        // Given
        NetworkBuffer remainingBuffer = new NetworkByteBuffer();
        remainingBuffer.writeBytes(new byte[]{'t', 'a'}); // Remaining data to complete the packet

        setupConnectionToReturnData(remainingBuffer);

        // When
        processor.process();

        // Then
        verify(handler).handle(connection, new TestPacket(data));
    }

    @Test
    void process_ShouldHandlePacketWithExtraBytes() throws IOException {
        // Given
        when(connection.isOpen()).thenReturn(true);
        when(connection.getState()).thenReturn(ConnectionState.PLAY);

        PacketDeserializer<Object> deserializer = new TestPacketDeserializer();
        PacketHandler<Object> handler = mock(PacketHandler.class);

        int packetId = 0x00;
        String data = "data";

        setupPacket(packetId, deserializer, handler);

        NetworkBuffer bigBuffer = new NetworkByteBuffer();
        bigBuffer.write(DataTypes.VAR_INT, 10); // 10 bytes of packet body
        bigBuffer.writeByte((byte) packetId); // Write packet ID
        bigBuffer.write(DataTypes.VAR_INT, 4);
        bigBuffer.writeBytes(new byte[]{'d', 'a', 't', 'a'}); // Write packet data
        bigBuffer.writeBytes(new byte[]{0x00, 0x01, 0x02, 0x03}); // Extra bytes that should be ignored

        setupConnectionToReturnData(bigBuffer);

        // When
        processor.process();

        // Then
        verify(handler).handle(connection, new TestPacket(data));
    }

    @Test
    void process_shouldHandleUnknownPacketId() throws IOException {
        // Given
        when(connection.isOpen()).thenReturn(true);
        when(connection.getState()).thenReturn(ConnectionState.PLAY);

        int unknownPacketId = 0xFF;

        NetworkBuffer buffer = createPacketBuffer(unknownPacketId, "data");

        setupConnectionToReturnData(buffer);

        when(packetRegistry.getPacket(ConnectionState.PLAY, unknownPacketId))
                .thenReturn(Optional.empty());

        // When
        processor.process();

        // Then
        verify(packetRegistry).getPacket(ConnectionState.PLAY, unknownPacketId);
    }

    @Test
    void process_shouldHandleLegacyServerListPingPacket() throws IOException {
        // Given
        when(connection.isOpen()).thenReturn(true);

        int legacyPingId = PacketProcessorImpl.LEGACY_PACKET_ID;

        NetworkBuffer buffer = new NetworkByteBuffer();
        buffer.write(DataTypes.VAR_INT, legacyPingId); // Length = packet ID for legacy ping

        setupConnectionToReturnData(buffer);

        // When
        processor.process();

        // Then
        verifyNoInteractions(packetRegistry);
    }

    // === Helper methods ===

    private void setupPacket(int packetId, PacketDeserializer<Object> deserializer, PacketHandler<Object> handler) {
        PacketBinding<Object> packetBinding = mock(PacketBinding.class);

        when(packetBinding.getDeserializer()).thenReturn(deserializer);
        when(packetBinding.getHandler()).thenReturn(handler);

        when(packetRegistry.getPacket(ConnectionState.PLAY, packetId))
                .thenReturn(Optional.of(packetBinding));
    }

    private NetworkBuffer createPacketBuffer(int packetId, String data) throws IOException {
        NetworkBuffer bodyBuffer = new NetworkByteBuffer();
        bodyBuffer.write(DataTypes.VAR_INT, packetId);
        bodyBuffer.write(DataTypes.STRING, data);

        int packetLength = bodyBuffer.getPosition();

        NetworkBuffer packetBuffer = new NetworkByteBuffer();
        packetBuffer.write(DataTypes.VAR_INT, packetLength);
        packetBuffer.write(bodyBuffer);

        return packetBuffer;
    }

    private void setupConnectionToReturnData(NetworkBuffer dataBuffer) throws IOException {
        when(connection.read(any(NetworkBuffer.class))).thenAnswer(invocation -> {
            NetworkBuffer targetBuffer = invocation.getArgument(0);
            targetBuffer.write(dataBuffer);

            return dataBuffer.getPosition();
        });
    }

    private record TestPacket(String data) {

    }

    private static class TestPacketDeserializer implements PacketDeserializer<Object> {

        @Override
        public Object deserialize(NetworkBuffer in) throws IOException {
            return new TestPacket(in.read(DataTypes.STRING));
        }
    }
}
