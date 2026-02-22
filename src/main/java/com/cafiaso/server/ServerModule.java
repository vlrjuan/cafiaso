package com.cafiaso.server;

import com.cafiaso.server.configuration.ServerConfiguration;
import com.cafiaso.server.icon.PngCachedServerIcon;
import com.cafiaso.server.icon.ServerIcon;
import com.cafiaso.server.network.NetworkModule;
import com.google.inject.AbstractModule;

public class ServerModule extends AbstractModule {

    private static final String ICON_PATH = "icon.png";

    private final ServerConfiguration configuration;

    public ServerModule(ServerConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected void configure() {
        bind(ServerConfiguration.class).toInstance(configuration);

        bind(ServerIcon.class).toInstance(new PngCachedServerIcon(ICON_PATH));

        // Sub-modules
        install(new NetworkModule());
    }
}
