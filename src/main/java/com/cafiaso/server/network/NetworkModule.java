package com.cafiaso.server.network;

import com.cafiaso.server.network.dispatcher.PacketDispatcher;
import com.cafiaso.server.network.dispatcher.PacketDispatcherImpl;
import com.cafiaso.server.network.processor.PacketProcessor;
import com.cafiaso.server.network.processor.PacketProcessorFactory;
import com.cafiaso.server.network.processor.PacketProcessorImpl;
import com.cafiaso.server.network.protocol.MojangPacketMappings;
import com.cafiaso.server.network.protocol.ProtocolModule;
import com.cafiaso.server.network.protocol.PacketMappings;
import com.cafiaso.server.network.server.NetworkServer;
import com.cafiaso.server.network.server.SocketServer;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(PacketDispatcher.class).to(PacketDispatcherImpl.class);

        install(
                new FactoryModuleBuilder()
                        .implement(PacketProcessor.class, PacketProcessorImpl.class)
                        .build(PacketProcessorFactory.class)
        );

        bind(PacketMappings.class).to(MojangPacketMappings.class);

        // Network server
        bind(ExecutorService.class).toInstance(Executors.newCachedThreadPool());
        bind(NetworkServer.class).to(SocketServer.class);

        // Sub-modules
        install(new ProtocolModule());
    }
}
