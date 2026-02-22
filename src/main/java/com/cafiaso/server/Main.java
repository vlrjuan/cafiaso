package com.cafiaso.server;

import com.cafiaso.server.configuration.ServerConfiguration;
import com.cafiaso.server.configuration.loader.ServerConfigurationLoader;
import com.cafiaso.server.configuration.loader.PropertiesServerConfigurationLoader;
import com.cafiaso.server.network.data.DataTypes;
import com.cafiaso.server.network.io.NetworkBuffer;
import com.cafiaso.server.network.io.NetworkByteBuffer;
import com.cafiaso.server.network.protocol.packets.serverbound.handshake.HandshakePacket;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.help.HelpFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The main entry point for the Minecraft server.
 * <p>
 * This class is responsible for parsing command line arguments, loading the configuration,
 * initializing the server, and starting it.
 */
public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static final String DEFAULT_HOST = "127.0.0.1";
    public static final int DEFAULT_PORT = 25565;

    private static final String CONFIG_PATH = "server.properties";

    static void main(String[] args) {
        Options options = new Options();

        Option helpOption = Option.builder()
                .longOpt("help")
                .desc("Show this help message")
                .get();

        Option hostOption = Option.builder()
                .option("h")
                .longOpt("host")
                .desc("The host to listen on")
                .hasArg()
                .get();

        Option portOption = Option.builder()
                .option("p")
                .longOpt("port")
                .desc("The port to listen on")
                .hasArg()
                .converter(Integer::parseInt)
                .get();

        options.addOption(helpOption);
        options.addOption(hostOption);
        options.addOption(portOption);

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine line = parser.parse(options, args);

            if (line.hasOption(helpOption)) {
                HelpFormatter formatter = HelpFormatter.builder()
                        .get();

                formatter.printHelp("help", "Server", options, "", true);

                return;
            }

            ServerConfigurationLoader configurationLoader = new PropertiesServerConfigurationLoader(CONFIG_PATH);
            ServerConfiguration configuration = configurationLoader.load();

            String host = line.getOptionValue(hostOption, DEFAULT_HOST);
            int port = line.getParsedOptionValue(portOption, DEFAULT_PORT);

            Injector injector = Guice.createInjector(new ServerModule(configuration));
            MinecraftServer server = injector.getInstance(MinecraftServer.class);

            server.start(host, port);

            NetworkBuffer buffer = new NetworkByteBuffer();
            buffer.write(DataTypes.VAR_INT, 774);
            buffer.write(DataTypes.STRING, "127.0.0.1");
            buffer.write(DataTypes.UNSIGNED_SHORT, 25565);
            buffer.write(DataTypes.ENUM(HandshakePacket.Intention.class, DataTypes.VAR_INT), HandshakePacket.Intention.STATUS);

            // Add a shutdown hook to stop the server gracefully
            Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
        } catch (ParseException e) {
            LOGGER.error("Failed to parse command line arguments", e);
        } catch (Exception e) {
            LOGGER.error("An error occurred while starting the server", e);
        }
    }
}
