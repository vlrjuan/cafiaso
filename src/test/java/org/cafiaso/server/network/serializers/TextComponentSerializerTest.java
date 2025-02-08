package org.cafiaso.server.network.serializers;

import org.cafiaso.server.network.serializers.TextComponentSerializer.TextComponent;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TextComponentSerializerTest {

    @Test
    void deserialize_ShouldDeserializeTextComponent() throws IOException {
        String json = "{\"text\":\"text\"}";
        ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes());
        DataInputStream dataIn = new DataInputStream(in);

        TextComponentSerializer serializer = new TextComponentSerializer();
        TextComponent textComponent = serializer.deserialize(dataIn);

        assertEquals("text", textComponent.text());
    }

    @Test
    void deserialize_ShouldThrowIOExceptionForInvalidJson() {
        String invalidJson = "{\"invalid\":\"json\"}";
        ByteArrayInputStream in = new ByteArrayInputStream(invalidJson.getBytes());
        DataInputStream dataIn = new DataInputStream(in);

        TextComponentSerializer serializer = new TextComponentSerializer();

        assertThrows(IOException.class, () -> serializer.deserialize(dataIn));
    }

    @Test
    void serialize_ShouldSerializeTextComponent() throws IOException {
        TextComponent textComponent = new TextComponent("text");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);

        TextComponentSerializer serializer = new TextComponentSerializer();
        serializer.serialize(dataOut, textComponent);

        String json = out.toString().substring(1); // Remove the first character (0x00)
        assertEquals("{\"text\":\"text\"}", json);
    }
}
