package org.cafiaso.server;

public class Main {

    public static void main(String[] args) {
        new ServerBootstrap(new ServerImpl()).run(args);
    }
}
