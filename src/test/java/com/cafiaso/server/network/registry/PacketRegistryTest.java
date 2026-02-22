package com.cafiaso.server.network.registry;

import com.cafiaso.server.network.connection.ConnectionState;
import com.cafiaso.server.network.protocol.PacketDeserializer;
import com.cafiaso.server.network.protocol.PacketHandler;
import com.cafiaso.server.network.protocol.PacketMappings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PacketRegistryTest {

    @Mock
    private PacketMappings mappings;

    @Mock
    private PacketDeserializer<Object> deserializer;

    @Mock
    private PacketHandler<Object> handler;

    @Test
    void getPacket_shouldReturnPacket_whenPacketExistsAndIsMapped() {
        // Given
        PacketBinding<Object> expectedPacket = createPacketBinding(
                ConnectionState.LOGIN,
                "minecraft:login_start"
        );

        when(mappings.serverbound()).thenReturn(Map.of(ConnectionState.LOGIN, Map.of(0x00, "minecraft:login_start")));

        PacketRegistry registry = new PacketRegistry(mappings, Set.of(expectedPacket));

        // When
        Optional<PacketBinding<Object>> result = registry.getPacket(ConnectionState.LOGIN, 0x00);

        // Then
        assertTrue(result.isPresent());

        PacketBinding<Object> packetBinding = result.get();
        assertEquals(deserializer, packetBinding.getDeserializer());
        assertEquals(handler, packetBinding.getHandler());
    }

    @Test
    void getPacket_shouldReturnEmpty_whenPacketIdIsUnknownFromNotchianClient() {
        // Given
        PacketBinding<Object> packet = createPacketBinding(
                ConnectionState.PLAY,
                "minecraft:some_packet"
        );

        when(mappings.serverbound()).thenReturn(Map.of(ConnectionState.PLAY, Collections.emptyMap()));

        PacketRegistry registry = new PacketRegistry(mappings, Set.of(packet));

        // When
        Optional<PacketBinding<Object>> result = registry.getPacket(ConnectionState.PLAY, 0x99);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void getPacket_shouldReturnEmpty_whenNoPacketsImplementedForState() {
        // Given
        PacketBinding<Object> loginPacket = createPacketBinding(
                ConnectionState.LOGIN,
                "minecraft:login_start"
        );

        when(mappings.serverbound()).thenReturn(Map.of(ConnectionState.PLAY, Map.of(0x00, "minecraft:some_packet")));

        PacketRegistry registry = new PacketRegistry(mappings, Set.of(loginPacket));

        // When
        Optional<PacketBinding<Object>> result = registry.getPacket(ConnectionState.PLAY, 0x00);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void getPacket_shouldReturnEmpty_whenPacketKnownButNotImplemented() {
        // Given
        PacketBinding<Object> implementedPacket = createPacketBinding(
                ConnectionState.PLAY,
                "minecraft:chat_message"
        );

        when(mappings.serverbound()).thenReturn(
                Map.of(ConnectionState.PLAY,
                        Map.of(
                                0x00, "minecraft:chat_message",
                                0x01, "minecraft:player_position"
                        )
                )
        );

        PacketRegistry registry = new PacketRegistry(mappings, Set.of(implementedPacket));

        // When
        Optional<PacketBinding<Object>> result = registry.getPacket(ConnectionState.PLAY, 0x01);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void getPacketId_shouldReturnPacketId_whenMappingExists() {
        // Given
        when(mappings.clientbound()).thenReturn(Map.of(ConnectionState.LOGIN, Map.of("minecraft:disconnect", 0x00)));

        PacketRegistry registry = new PacketRegistry(mappings, Set.of());

        // When
        Optional<Integer> result = registry.getPacketId(
                ConnectionState.LOGIN,
                "minecraft:disconnect"
        );

        // Then
        assertTrue(result.isPresent());
        assertEquals(0x00, result.get());
    }

    @Test
    void getPacketId_shouldReturnEmpty_whenMappingDoesNotExist() {
        // Given
        when(mappings.clientbound()).thenReturn(Map.of(ConnectionState.LOGIN, Collections.emptyMap()));

        PacketRegistry registry = new PacketRegistry(mappings, Set.of());

        // When
        Optional<Integer> result = registry.getPacketId(
                ConnectionState.LOGIN,
                "minecraft:unknown_packet"
        );

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void getPacket_shouldHandleEmptyPacketSet() {
        // Given
        when(mappings.serverbound()).thenReturn(Map.of(ConnectionState.HANDSHAKE, Map.of(0x00, "minecraft:handshake")));

        PacketRegistry registry = new PacketRegistry(mappings, Set.of());

        // When
        Optional<PacketBinding<Object>> result = registry.getPacket(ConnectionState.HANDSHAKE, 0x00);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void getPacket_shouldHandleDifferentConnectionStates() {
        // Given
        PacketBinding<Object> handshakePacket = createPacketBinding(
                ConnectionState.HANDSHAKE,
                "minecraft:handshake"
        );
        PacketBinding<Object> statusPacket = createPacketBinding(
                ConnectionState.STATUS,
                "minecraft:status_request"
        );
        PacketBinding<Object> loginPacket = createPacketBinding(
                ConnectionState.LOGIN,
                "minecraft:login_start"
        );
        PacketBinding<Object> configPacket = createPacketBinding(
                ConnectionState.CONFIGURATION,
                "minecraft:client_information"
        );
        PacketBinding<Object> playPacket = createPacketBinding(
                ConnectionState.PLAY,
                "minecraft:chat_message"
        );

        when(mappings.serverbound()).thenReturn(
                Map.of(
                        ConnectionState.HANDSHAKE, Map.of(0x00, "minecraft:handshake"),
                        ConnectionState.STATUS, Map.of(0x00, "minecraft:status_request"),
                        ConnectionState.LOGIN, Map.of(0x00, "minecraft:login_start"),
                        ConnectionState.CONFIGURATION, Map.of(0x00, "minecraft:client_information"),
                        ConnectionState.PLAY, Map.of(0x00, "minecraft:chat_message")
                )
        );

        PacketRegistry registry = new PacketRegistry(mappings, Set.of(
                handshakePacket, statusPacket, loginPacket, configPacket, playPacket
        ));

        // When/Then
        assertTrue(registry.getPacket(ConnectionState.HANDSHAKE, 0x00).isPresent());
        assertTrue(registry.getPacket(ConnectionState.STATUS, 0x00).isPresent());
        assertTrue(registry.getPacket(ConnectionState.LOGIN, 0x00).isPresent());
        assertTrue(registry.getPacket(ConnectionState.CONFIGURATION, 0x00).isPresent());
        assertTrue(registry.getPacket(ConnectionState.PLAY, 0x00).isPresent());
    }

    // === Helper methods ===

    private PacketBinding<Object> createPacketBinding(ConnectionState state, String resourceId) {
        return new PacketBinding<>(
                state,
                resourceId,
                () -> deserializer,
                () -> handler
        );
    }
}
