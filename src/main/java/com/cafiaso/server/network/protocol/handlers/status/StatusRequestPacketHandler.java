package com.cafiaso.server.network.protocol.handlers.status;

import com.cafiaso.server.configuration.ServerConfiguration;
import com.cafiaso.server.icon.ServerIcon;
import com.cafiaso.server.network.connection.Connection;
import com.cafiaso.server.network.dispatcher.PacketDispatcher;
import com.cafiaso.server.network.protocol.PacketHandler;
import com.cafiaso.server.network.protocol.packets.clientbound.status.StatusResponsePacket;
import com.cafiaso.server.network.protocol.packets.serverbound.status.StatusRequestPacket;
import com.cafiaso.server.player.Player;
import com.cafiaso.server.player.PlayerManager;
import jakarta.inject.Inject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collection;

import static com.cafiaso.server.MinecraftConstants.MINECRAFT_VERSION;
import static com.cafiaso.server.MinecraftConstants.PROTOCOL_VERSION;

public class StatusRequestPacketHandler implements PacketHandler<StatusRequestPacket> {

    private final ServerConfiguration configuration;
    private final ServerIcon icon;
    private final PlayerManager playerManager;
    private final PacketDispatcher packetDispatcher;

    @Inject
    public StatusRequestPacketHandler(
            ServerConfiguration configuration,
            ServerIcon icon,
            PlayerManager playerManager,
            PacketDispatcher packetDispatcher
    ) {
        this.configuration = configuration;
        this.icon = icon;
        this.playerManager = playerManager;
        this.packetDispatcher = packetDispatcher;
    }

    @Override
    public void handle(Connection connection, StatusRequestPacket packet) throws IOException {
        // Version
        JSONObject version = new JSONObject()
                .put("name", MINECRAFT_VERSION)
                .put("protocol", PROTOCOL_VERSION);

        Collection<Player> onlinePlayers = playerManager.getPlayers();

        // Sample player array
        JSONArray sample = new JSONArray();
        sample.putAll(
                onlinePlayers.stream()
                        .map(player ->
                                new JSONObject()
                                        .put("name", player.getName())
                                        .put("id", player.getId())
                        )
                        .toList()
        );

        // Players
        JSONObject players = new JSONObject()
                .put("max", configuration.getMaxPlayers())
                .put("online", onlinePlayers.size())
                .put("sample", sample);

        // Description (MOTD)
        JSONObject description = new JSONObject();
        description.put("text", configuration.getDescription());

        JSONObject status = new JSONObject()
                .put("version", version)
                .put("players", players)
                .put("description", description);

        // Favicon
        icon.getData().ifPresent(encodedIcon -> status.put("favicon", encodedIcon));

        packetDispatcher.dispatch(connection, new StatusResponsePacket(status.toString()));
    }
}
