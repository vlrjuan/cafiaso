package org.cafiaso.server.network.packet.client.configuration;

import org.cafiaso.server.network.Serializer;
import org.cafiaso.server.network.packet.client.configuration.ClientInformationPacket.ChatMode;
import org.cafiaso.server.network.packet.client.configuration.ClientInformationPacket.MainHand;
import org.cafiaso.server.network.packet.client.configuration.ClientInformationPacket.ParticleStatus;
import org.cafiaso.server.network.stream.input.ByteArrayInputStream;
import org.cafiaso.server.network.stream.input.InputStream;
import org.cafiaso.server.network.stream.output.ByteArrayOutputStream;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ClientInformationPacketTest {

    @Test
    void read_ShouldReadPacket() throws IOException {
        String locale = "en_GB";
        byte viewDistance = 10;
        ChatMode chatMode = ChatMode.COMMANDS_ONLY;
        boolean chatColors = true;
        byte displayedSkinParts = 0b1111;
        MainHand mainHand = MainHand.RIGHT;
        boolean enableTextFiltering = true;
        boolean allowServerListings = true;
        ParticleStatus particleStatus = ParticleStatus.ALL;

        byte[] data = serializeData(locale, viewDistance, chatMode, chatColors, displayedSkinParts, mainHand, enableTextFiltering, allowServerListings, particleStatus);

        InputStream in = new ByteArrayInputStream(data);

        ClientInformationPacket packet = new ClientInformationPacket();
        packet.read(in);

        assertEquals(locale, packet.getLocale());
        assertEquals(viewDistance, packet.getViewDistance());
        assertEquals(chatMode, packet.getChatMode());
        assertEquals(chatColors, packet.isChatColors());
        assertEquals(displayedSkinParts, packet.getDisplayedSkinParts());
        assertEquals(mainHand, packet.getMainHand());
        assertEquals(enableTextFiltering, packet.isEnableTextFiltering());
        assertEquals(allowServerListings, packet.isAllowServerListings());
        assertEquals(particleStatus, packet.getParticleStatus());
    }

    private byte[] serializeData(
            String locale,
            byte viewDistance,
            ChatMode chatMode,
            boolean chatColors,
            byte displayedSkinParts,
            MainHand mainHand,
            boolean enableTextFiltering,
            boolean allowServerListings,
            ParticleStatus particleStatus
    ) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            out.write(Serializer.STRING(16), locale);
            out.write(Serializer.BYTE, viewDistance);
            out.write(Serializer.ENUM(ChatMode.class, Serializer.VAR_INT, ChatMode::getId), chatMode);
            out.write(Serializer.BOOLEAN, chatColors);
            out.write(Serializer.UNSIGNED_BYTE, displayedSkinParts);
            out.write(Serializer.ENUM(MainHand.class, Serializer.VAR_INT, MainHand::getId), mainHand);
            out.write(Serializer.BOOLEAN, enableTextFiltering);
            out.write(Serializer.BOOLEAN, allowServerListings);
            out.write(Serializer.ENUM(ParticleStatus.class, Serializer.VAR_INT, ParticleStatus::getId), particleStatus);

            return out.toByteArray();
        }
    }
}
