package org.cafiaso.server.mojang;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
class MojangClientImplTest {

    private static final String MOJANG_RESPONSE = "{\"id\": \"069a79f444e94726a5befca90e38aaf5\", \"name\": \"Choukas\", \"properties\": [{\"name\": \"textures\", \"value\": \"property_value\", \"signature\": \"property_signature\"}]}";

    @Test
    void verifyClientLoginSession_ShouldReturnPlayerProfileOnSuccess() throws IOException, InterruptedException {
        try (HttpClient mockHttpClient = mock(HttpClient.class)) {
            HttpResponse<String> mockResponse = mock(HttpResponse.class);

            when(mockResponse.statusCode()).thenReturn(200);
            when(mockResponse.body()).thenReturn(MOJANG_RESPONSE);

            when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockResponse);

            MojangClient mojangClient = new MojangClientImpl(() -> mockHttpClient);

            PlayerProfile profile = mojangClient.verifyClientLoginSession("username", "serverId", "127.0.0.1");

            assertNotNull(profile);
            assertEquals("069a79f444e94726a5befca90e38aaf5", profile.id()); // Undashed UUID
            assertEquals("069a79f4-44e9-4726-a5be-fca90e38aaf5", profile.getId().toString()); // Dashed UUID
            assertEquals("Choukas", profile.name());
            assertEquals(1, profile.properties().length);
            assertEquals("textures", profile.properties()[0].name());
            assertEquals("property_value", profile.properties()[0].value());
            assertEquals("property_signature", profile.properties()[0].signature());
        }
    }

    @Test
    void verifyClientLoginSession_ShouldThrowException_WhenNon200Response() throws IOException, InterruptedException {
        try (HttpClient mockHttpClient = mock(HttpClient.class)) {
            HttpResponse<String> mockResponse = mock(HttpResponse.class);

            when(mockResponse.statusCode()).thenReturn(401);
            when(mockResponse.body()).thenReturn("Unauthorized");
            when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockResponse);

            MojangClient mojangClient = new MojangClientImpl(() -> mockHttpClient);

            assertThrowsExactly(RuntimeException.class,
                    () -> mojangClient.verifyClientLoginSession("username", "serverId", "127.0.0.1"),
                    "Failed to authenticate user. Response code: 401, body: Unauthorized");
        }
    }

    @Test
    void verifyClientLoginSession_ShouldThrowExceptionOnIOException() throws IOException, InterruptedException {
        try (HttpClient mockHttpClient = mock(HttpClient.class)) {
            when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenThrow(new IOException("Network error"));

            MojangClient mojangClient = new MojangClientImpl(() -> mockHttpClient);

            assertThrowsExactly(RuntimeException.class, () -> mojangClient.verifyClientLoginSession("username", "serverId", "127.0.0.1"));
        }
    }
}
