package com.cafiaso.generator;

import com.cafiaso.generator.generators.*;

public class Main {

    private static final Generator[] GENERATORS = {
            new MinecraftConstantsGenerator(),
            new PacketMappingsGenerator()
    };

    static void main() {
        for (Generator generator : GENERATORS) {
            generator.generate();
        }
    }
}
