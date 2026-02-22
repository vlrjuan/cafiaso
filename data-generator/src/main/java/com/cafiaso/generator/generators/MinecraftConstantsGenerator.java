package com.cafiaso.generator.generators;

import com.cafiaso.generator.Generator;
import com.cafiaso.generator.utils.CodeUtils;
import com.cafiaso.generator.utils.ResourceUtils;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.TypeSpec;
import org.json.JSONObject;

import javax.lang.model.element.Modifier;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class MinecraftConstantsGenerator implements Generator {

    @Override
    public void generate() {
        try (JarFile jar = new JarFile(ResourceUtils.getResource("server.jar"))) {
            JarEntry versionEntry = jar.getJarEntry("version.json");
            if (versionEntry == null) {
                throw new FileNotFoundException("version.json not found in server.jar");
            }

            try (InputStream in = jar.getInputStream(versionEntry)) {
                String jsonContent = new String(in.readAllBytes());
                JSONObject versionJson = new JSONObject(jsonContent);

                String minecraftVersion = versionJson.getString("name");
                Integer protocolVersion = versionJson.getInt("protocol_version");

                TypeSpec minecraftConstantsSpec = TypeSpec.classBuilder("MinecraftConstants")
                        .addJavadoc("This class is auto-generated. Do not edit it.")
                        .addModifiers(Modifier.PUBLIC)
                        .addField(
                                FieldSpec.builder(String.class, "MINECRAFT_VERSION", Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                                        .initializer("$S", minecraftVersion)
                                        .build()
                        )
                        .addField(
                                FieldSpec.builder(Integer.class, "PROTOCOL_VERSION", Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                                        .initializer("$L", protocolVersion)
                                        .build()
                        )
                        .build();

                CodeUtils.writeClassToFile(minecraftConstantsSpec);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate MinecraftConstants", e);
        }
    }
}
