package org.cafiaso.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class ServerBootstrapTest {

    private Server server;
    private ServerBootstrap serverBootstrap;

    @BeforeEach
    void setUp() {
        server = mock(Server.class);
        doNothing().when(server).start(anyString(), anyInt());

        serverBootstrap = new ServerBootstrap(server);
    }

    @Test
    void run_ShouldStartServerWithGivenHostAndPort_WhenArgumentsAreProvided() {
        serverBootstrap.run(new String[]{"-h", "mc.cafiaso.org", "-p", "8000"});

        verify(server).start("mc.cafiaso.org", 8000);
    }

    @Test
    void run_ShouldStartServerWithDefaultHostAndPort_WhenNoArgumentsAreProvided() {
        serverBootstrap.run(new String[]{});

        verify(server).start(ServerBootstrap.DEFAULT_HOST, ServerBootstrap.DEFAULT_PORT);
    }

    @Test
    void run_ShouldStartServerWithDefaultHostAndGivenPort_WhenOnlyPortIsProvided() {
        serverBootstrap.run(new String[]{"-p", "8000"});

        verify(server).start("localhost", 8000);
    }

    @Test
    void run_ShouldNotStartServer_WhenPortIsNotANumber() {
        serverBootstrap.run(new String[]{"-p", "not_a_number"});

        verify(server, never()).start(anyString(), anyInt());
    }
}
