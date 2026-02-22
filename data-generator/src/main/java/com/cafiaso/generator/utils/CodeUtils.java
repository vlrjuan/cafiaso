package com.cafiaso.generator.utils;

import com.palantir.javapoet.JavaFile;
import com.palantir.javapoet.TypeSpec;

import java.io.File;
import java.io.IOException;

public class CodeUtils {

    private static final String OUTPUT_PATH = "src/generated/java/";
    private static final String ROOT_PACKAGE = "com.cafiaso.server";

    public static void writeClassToFile(TypeSpec spec, String... packages) {
        String subPackage = String.join(".", packages);
        String fullPackage = ROOT_PACKAGE;
        if (!subPackage.isEmpty()) {
            fullPackage += "." + subPackage;
        }

        JavaFile javaFile = JavaFile.builder(fullPackage, spec)
                .indent("    ")
                .build();

        try {
            javaFile.writeTo(new File(OUTPUT_PATH));
        } catch (IOException e) {
            throw new RuntimeException("Failed to write class to file: " + spec.name(), e);
        }
    }
}
