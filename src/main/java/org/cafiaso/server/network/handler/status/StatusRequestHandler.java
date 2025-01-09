package org.cafiaso.server.network.handler.status;

import org.cafiaso.server.Server;
import org.cafiaso.server.network.connection.Connection;
import org.cafiaso.server.network.handler.PacketHandler;
import org.cafiaso.server.network.packet.client.status.StatusRequestPacket;
import org.cafiaso.server.network.packet.server.status.StatusResponsePacket;
import org.json.JSONObject;

import java.io.IOException;

public class StatusRequestHandler implements PacketHandler<StatusRequestPacket> {

    @Override
    public void handle(Connection connection, StatusRequestPacket packet) throws IOException {
        Server server = connection.getServer();

        JSONObject response = new JSONObject();

        JSONObject version = new JSONObject();
        version.put("name", Server.VERSION_NAME);
        version.put("protocol", Server.PROTOCOL_VERSION);

        response.put("version", version);

        JSONObject players = new JSONObject();
        players.put("max", server.getConfiguration().getMaximumPlayers());
        players.put("online", server.getPlayerManager().getOnlinePlayers());

        response.put("players", players);

        JSONObject description = new JSONObject();
        description.put("text", server.getConfiguration().getDescription());

        response.put("description", description);

        String jsonResponse = response.toString();

        connection.sendPacket(new StatusResponsePacket(jsonResponse));
    }
}
