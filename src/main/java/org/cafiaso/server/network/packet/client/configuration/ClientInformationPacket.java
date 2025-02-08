package org.cafiaso.server.network.packet.client.configuration;

import org.cafiaso.server.network.Serializer;
import org.cafiaso.server.network.packet.client.ClientPacket;
import org.cafiaso.server.network.stream.input.InputStream;

import java.io.IOException;

/**
 * Packet sent by the client when settings are changed.
 *
 * @see <a href="https://minecraft.wiki/w/Java_Edition_protocol#Client_Information_(configuration)">Client Information (configuration)</a>
 */
public class ClientInformationPacket implements ClientPacket {

    private static final Serializer<String> LOCALE_SERIALIZER = Serializer.STRING(16);
    private static final Serializer<ChatMode> CHAT_MODE_SERIALIZER = Serializer.ENUM(ChatMode.class, Serializer.VAR_INT, ChatMode::getId);
    private static final Serializer<MainHand> MAIN_HAND_SERIALIZER = Serializer.ENUM(MainHand.class, Serializer.VAR_INT, MainHand::getId);
    private static final Serializer<ParticleStatus> PARTICLE_STATUS_SERIALIZER = Serializer.ENUM(ParticleStatus.class, Serializer.VAR_INT, ParticleStatus::getId);

    private String locale;
    private byte viewDistance;
    private ChatMode chatMode;
    private boolean chatColors;
    private byte displayedSkinParts;
    private MainHand mainHand;
    private boolean enableTextFiltering;
    private boolean allowServerListings;
    private ParticleStatus particleStatus;

    @Override
    public void read(InputStream in) throws IOException {
        locale = in.read(LOCALE_SERIALIZER);
        viewDistance = in.read(Serializer.BYTE);
        chatMode = in.read(CHAT_MODE_SERIALIZER);
        chatColors = in.read(Serializer.BOOLEAN);
        displayedSkinParts = in.read(Serializer.UNSIGNED_BYTE);
        mainHand = in.read(MAIN_HAND_SERIALIZER);
        enableTextFiltering = in.read(Serializer.BOOLEAN);
        allowServerListings = in.read(Serializer.BOOLEAN);
        particleStatus = in.read(PARTICLE_STATUS_SERIALIZER);
    }

    /**
     * Get the client locale (e.g {@code en_GB}).
     *
     * @return the client locale
     */
    public String getLocale() {
        return locale;
    }

    /**
     * Get the client render distance, in chunks.
     *
     * @return the client render distance
     */
    public byte getViewDistance() {
        return viewDistance;
    }

    /**
     * Get the client chat mode.
     *
     * @return the client chat mode
     */
    public ChatMode getChatMode() {
        return chatMode;
    }

    /**
     * Checks whether the client enabled colors in chat.
     *
     * @return {@code true} if colors are enabled, {@code false} otherwise
     */
    public boolean isChatColors() {
        return chatColors;
    }

    /**
     * Gets the client skin parts.
     * <p>
     * This is a byte mask defined as follows :
     * <ul>
     *     <li>Bit 0 (0x01): Cape enabled</li>
     *     <li>Bit 1 (0x02): Jacket enabled</li>
     *     <li>Bit 2 (0x04): Left Sleeve enabled</li>
     *     <li>Bit 3 (0x08): Right Sleeve enabled</li>
     *     <li>Bit 4 (0x10): Left Pants Leg enabled</li>
     *     <li>Bit 5 (0x20): Right Pants Leg enabled</li>
     *     <li>Bit 6 (0x40): Hat enabled</li>
     * </ul>
     *
     * @return the client skin parts
     */
    public byte getDisplayedSkinParts() {
        return displayedSkinParts;
    }

    /**
     * Get the client main hand.
     *
     * @return the client main hand
     */
    public MainHand getMainHand() {
        return mainHand;
    }

    /**
     * Checks whether the client enables filtering of text and writing book titles.
     *
     * @return {@code true} if client enabled text filtering, {@code false} otherwise
     */
    public boolean isEnableTextFiltering() {
        return enableTextFiltering;
    }

    /**
     * Checks whether the client should be listed as online player.
     *
     * @return {@code true} if the client is listed as online player, {@code false} otherwise
     */
    public boolean isAllowServerListings() {
        return allowServerListings;
    }

    /**
     * Get the client particle setting.
     *
     * @return the client particle setting
     */
    public ParticleStatus getParticleStatus() {
        return particleStatus;
    }

    @Override
    public String toString() {
        return "ClientInformationPacket{" +
                "locale='" + locale + '\'' +
                ", viewDistance=" + viewDistance +
                ", chatMode=" + chatMode +
                ", chatColors=" + chatColors +
                ", displayedSkinParts=" + displayedSkinParts +
                ", mainHand=" + mainHand +
                ", enableTextFiltering=" + enableTextFiltering +
                ", allowServerListings=" + allowServerListings +
                ", particleStatus=" + particleStatus +
                '}';
    }

    /**
     * Represents the client settings for chat.
     *
     * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Chat#Client_chat_mode">Client chat mode</a>
     */
    public enum ChatMode {
        /**
         * Chat is fully enabled.
         */
        ENABLED(0),
        /**
         * Only command feedback is displayed.
         */
        COMMANDS_ONLY(1),
        /**
         * Chat is hidden (except System Chat Message (overlay)).
         */
        HIDDEN(2);

        private final int id;

        ChatMode(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    /**
     * Represents the player main hand.
     */
    public enum MainHand {
        /**
         * Left hand
         */
        LEFT(0),
        /**
         * Right hand
         */
        RIGHT(1);

        private final int id;

        MainHand(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    /**
     * Represents the client settings for particles.
     */
    public enum ParticleStatus {
        /**
         * All particles are displayed.
         */
        ALL(0),
        /**
         * Particles are not fully displayed.
         */
        DECREASED(1),
        /**
         * Minimal particles are displayed.
         */
        MINIMAL(2);

        private final int id;

        ParticleStatus(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}
