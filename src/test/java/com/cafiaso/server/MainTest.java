package com.cafiaso.server;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.*;

class MainTest {

    @Test
    void main_shouldStartServerWithDefaultConfigurationHostAndPort_WhenNoArgumentsProvided() {
        // Given
        MinecraftServer server = mock(MinecraftServer.class);

        Injector injector = mock(Injector.class);
        when(injector.getInstance(MinecraftServer.class)).thenReturn(server);

        try (MockedStatic<Guice> guiceMock = mockStatic(Guice.class)) {
            guiceMock.when(() -> Guice.createInjector(any(Module[].class))).thenReturn(injector);

            // When
            Main.main(new String[]{});

            // Then
            verify(server).start(Main.DEFAULT_HOST, Main.DEFAULT_PORT);
        }
    }

    @Test
    void main_shouldStartServerWithGivenHostAndPort_WhenArgumentsProvided() {
        // Given
        MinecraftServer server = mock(MinecraftServer.class);

        Injector injector = mock(Injector.class);
        when(injector.getInstance(MinecraftServer.class)).thenReturn(server);

        try (MockedStatic<Guice> guiceMock = mockStatic(Guice.class)) {
            guiceMock.when(() -> Guice.createInjector(any(Module[].class))).thenReturn(injector);

            // When
            Main.main(new String[]{"--host", "999.999.999.999", "--port", "9999"});

            // Then
            verify(server).start("999.999.999.999", 9999);
        }
    }
}
