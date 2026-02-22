package com.cafiaso.server.network.processor;

import com.cafiaso.server.network.connection.Connection;

public interface PacketProcessorFactory {

    PacketProcessor create(Connection connection);
}
