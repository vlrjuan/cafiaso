package org.cafiaso.server;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 25565;

    public static void main(String[] args) {
        Options options = new Options();

        Option helpOption = new Option("help", "Prints this message");

        Option hostOption = Option.builder()
                .option("h")
                .longOpt("host")
                .desc("The host to listen on")
                .hasArg()
                .build();

        Option portOption = Option.builder()
                .option("p")
                .longOpt("port")
                .desc("The port to listen on")
                .hasArg()
                .converter(Integer::parseInt)
                .build();

        options.addOption(helpOption);
        options.addOption(hostOption);
        options.addOption(portOption);

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine line = parser.parse(options, args);

            String host = line.getOptionValue(hostOption, DEFAULT_HOST);
            int port = line.getParsedOptionValue(portOption, DEFAULT_PORT);

            Server server = new Server();
            server.start(host, port);
        } catch (ParseException e) {
            LOGGER.error("Failed to parse command line arguments", e);
        }
    }
}
