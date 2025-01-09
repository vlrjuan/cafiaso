package org.cafiaso.server.network.connection;

import org.cafiaso.server.Server;
import org.cafiaso.server.network.DataType;
import org.cafiaso.server.network.buffers.InputBuffer;
import org.cafiaso.server.network.buffers.OutputBuffer;
import org.cafiaso.server.network.connection.ConnectionState.PacketEntry;
import org.cafiaso.server.network.handler.PacketHandler;
import org.cafiaso.server.network.packet.client.ClientPacket;
import org.cafiaso.server.network.packet.server.ServerPacket;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.InetAddress;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
class ConnectionTest {

    private static final int CAFIASO_PACKET_ID = 0;

    private static final String CAFIASO_PACKET_MESSAGE = "Hello, world!";
    private static final int CAFIASO_PACKET_NUMBER = 42;

    // Data types
    private static final DataType<Integer> VAR_INT_DATA_TYPE = DataType.VAR_INT;
    private static final DataType<String> STRING_DATA_TYPE = DataType.STRING;

    @Test
    void readPacket_ShouldHandleCafiasoPacket() throws IOException {
        InputBuffer inputBuffer = createInputBufferWithCafiasoPacket();

        CafiasoPacket packetReceivedByServer = new CafiasoPacket();
        PacketHandler<CafiasoPacket> handler = new CafiasoPacketHandler();
        PacketEntry<? extends ClientPacket> entry = createPacketEntry(packetReceivedByServer, handler);

        ConnectionState state = mock(ConnectionState.class);
        when(state.getPacketById(CAFIASO_PACKET_ID)).thenReturn((PacketEntry<ClientPacket>) entry);

        Connection connection = new CafiasoConnection(mock(InetAddress.class), inputBuffer, mock(OutputBuffer.class));
        connection.setState(state);

        boolean isPacketRead = connection.readPacket();

        assertTrue(isPacketRead);

        assertEquals(CAFIASO_PACKET_MESSAGE, packetReceivedByServer.message);
        assertEquals(CAFIASO_PACKET_NUMBER, packetReceivedByServer.number);

        assertFalse(connection.isOpen());
    }

    @Test
    void readPacket_ShouldReturnFalse_WhenBufferIsEmpty() throws IOException {
        InputBuffer inputBuffer = mock(InputBuffer.class);
        when(inputBuffer.isEmpty()).thenReturn(true);

        ConnectionState state = mock(ConnectionState.class);

        Connection connection = new CafiasoConnection(mock(InetAddress.class), inputBuffer, mock(OutputBuffer.class));
        connection.setState(state);

        boolean isPacketRead = connection.readPacket();

        assertFalse(isPacketRead);
    }

    @Test
    void readPacket_ShouldReturnFalse_WhenPacketIsNotFound() throws IOException {
        InputBuffer inputBuffer = createInputBufferWithCafiasoPacket();
        assertFalse(inputBuffer.isEmpty());

        ConnectionState state = mock(ConnectionState.class);
        when(state.getPacketById(CAFIASO_PACKET_ID)).thenReturn(null);

        Connection connection = new CafiasoConnection(mock(InetAddress.class), inputBuffer, mock(OutputBuffer.class));
        connection.setState(state);

        boolean isPacketRead = connection.readPacket();

        assertFalse(isPacketRead);
    }

    @Test
    void sendPacket_ShouldWritePacketToOutputBuffer() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);

        OutputBuffer outputBuffer = new OutputBuffer(dataOut);

        CafiasoPacket packet = new CafiasoPacket(CAFIASO_PACKET_MESSAGE, CAFIASO_PACKET_NUMBER);

        Connection connection = new CafiasoConnection(mock(InetAddress.class), mock(InputBuffer.class), outputBuffer);
        connection.sendPacket(packet);

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        DataInputStream dataIn = new DataInputStream(in);

        assertEquals(packet.getLength(), VAR_INT_DATA_TYPE.read(dataIn));
        assertEquals(CAFIASO_PACKET_ID, VAR_INT_DATA_TYPE.read(dataIn));

        CafiasoPacket packetReceivedByServer = new CafiasoPacket();
        packetReceivedByServer.read(new InputBuffer(dataIn));

        assertEquals(CAFIASO_PACKET_MESSAGE, packetReceivedByServer.message);
        assertEquals(CAFIASO_PACKET_NUMBER, packetReceivedByServer.number);
    }

    @Test
    void toString_ShouldReturnAddress() {
        InetAddress address = InetAddress.getLoopbackAddress();
        CafiasoConnection connection = new CafiasoConnection(address, mock(InputBuffer.class), mock(OutputBuffer.class));

        assertEquals(address.toString(), connection.toString());
    }

    private InputBuffer createInputBufferWithCafiasoPacket() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);

        OutputBuffer outputBuffer = new OutputBuffer(dataOut);

        CafiasoPacket packetSentByClient = new CafiasoPacket(CAFIASO_PACKET_MESSAGE, CAFIASO_PACKET_NUMBER);

        outputBuffer.write(VAR_INT_DATA_TYPE, packetSentByClient.getLength());
        outputBuffer.write(VAR_INT_DATA_TYPE, CAFIASO_PACKET_ID);
        packetSentByClient.write(outputBuffer);

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        DataInputStream dataIn = new DataInputStream(in);

        return new InputBuffer(dataIn);
    }

    private PacketEntry<? extends ClientPacket> createPacketEntry(CafiasoPacket packet, PacketHandler<CafiasoPacket> handler) {
        PacketEntry<CafiasoPacket> entry = mock(PacketEntry.class);
        when(entry.packet()).thenReturn(packet);
        when(entry.handler()).thenReturn(handler);

        return entry;
    }

    private static class CafiasoConnection extends AbstractConnection {

        private boolean open = true;

        public CafiasoConnection(InetAddress address, InputBuffer inputBuffer, OutputBuffer outputBuffer) {
            super(mock(Server.class), address, inputBuffer, outputBuffer);
        }

        @Override
        public boolean isOpen() {
            return open;
        }

        @Override
        public void close() throws IOException {
            super.close();

            open = false;
        }
    }

    private static class CafiasoPacket implements ClientPacket, ServerPacket {

        private String message;
        private int number;

        public CafiasoPacket(String message, int number) {
            this.message = message;
            this.number = number;
        }

        public CafiasoPacket() {

        }

        @Override
        public int getId() {
            return CAFIASO_PACKET_ID;
        }

        @Override
        public void read(InputBuffer buffer) throws IOException {
            message = buffer.read(STRING_DATA_TYPE);
            number = buffer.read(VAR_INT_DATA_TYPE);
        }

        @Override
        public void write(OutputBuffer buffer) throws IOException {
            buffer.write(STRING_DATA_TYPE, message);
            buffer.write(VAR_INT_DATA_TYPE, number);
        }
    }

    private static class CafiasoPacketHandler implements PacketHandler<CafiasoPacket> {

        @Override
        public void handle(Connection connection, CafiasoPacket packet) throws IOException {
            connection.close();
        }
    }
}
