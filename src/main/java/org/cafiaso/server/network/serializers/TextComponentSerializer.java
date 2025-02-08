package org.cafiaso.server.network.serializers;

import org.cafiaso.server.network.Serializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A {@link Serializer} for {@link TextComponentSerializer.TextComponent} values.
 *
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Data_types#Type:Text_Component">Text Component Type</a>
 */
public class TextComponentSerializer implements Serializer<TextComponentSerializer.TextComponent> {

    private static final String JSON = "{\"text\":\"%s\"}";

    @Override
    public TextComponent deserialize(DataInputStream in) throws IOException {
        byte[] serializedData = new byte[in.available()];
        in.readFully(serializedData);

        String serializedText = new String(serializedData);

        // Extract the text from the JSON object using regex
        Pattern pattern = Pattern.compile("\"text\":\"(.*?)\"");
        Matcher matcher = pattern.matcher(serializedText);

        if (!matcher.find()) {
            throw new IOException("Invalid JSON object");
        }

        String deserializedText = matcher.group(1);

        return new TextComponent(deserializedText);
    }

    @Override
    public void serialize(DataOutputStream out, TextComponent value) throws IOException {
        Serializer.STRING.serialize(out, JSON.formatted(value.text));
    }

    public record TextComponent(String text) {

    }
}
