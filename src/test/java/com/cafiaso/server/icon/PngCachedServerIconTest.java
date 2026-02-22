package com.cafiaso.server.icon;

import org.junit.jupiter.api.Test;

import java.util.Base64;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PngCachedServerIconTest {

    private static final String VALID_ICON_PATH = "icon.png";
    private static final String NON_EXISTENT_PATH = "non-existent.png";

    @Test
    void getData_shouldReturnValidBase64Icon_whenIconExists() {
        // Given
        PngCachedServerIcon serverIcon = new PngCachedServerIcon(VALID_ICON_PATH);

        // When
        Optional<String> result = serverIcon.getData();

        // Then
        assertTrue(result.isPresent());

        String base64Data = result.get();
        assertNotNull(base64Data);

        // Vérifier que c'est du Base64 valide
        assertDoesNotThrow(() -> Base64.getDecoder().decode(base64Data));
    }

    @Test
    void getData_shouldReturnEmpty_whenIconDoesNotExist() {
        // Given
        PngCachedServerIcon serverIcon = new PngCachedServerIcon(NON_EXISTENT_PATH);

        // When
        Optional<String> result = serverIcon.getData();

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void getData_shouldCacheIcon_afterFirstCall() {
        // Given
        PngCachedServerIcon serverIcon = new PngCachedServerIcon(VALID_ICON_PATH);

        // When
        Optional<String> firstCall = serverIcon.getData();
        Optional<String> secondCall = serverIcon.getData();

        // Then
        assertTrue(firstCall.isPresent());
        assertTrue(secondCall.isPresent());

        // Vérifier que c'est la même instance (cache)
        assertSame(firstCall.get(), secondCall.get());
    }
}
