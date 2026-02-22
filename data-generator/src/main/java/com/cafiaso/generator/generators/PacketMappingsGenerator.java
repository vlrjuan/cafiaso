package com.cafiaso.generator.generators;

import com.cafiaso.generator.Generator;
import com.cafiaso.generator.utils.CodeUtils;
import com.cafiaso.generator.utils.ResourceUtils;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterizedTypeName;
import com.palantir.javapoet.TypeSpec;
import org.json.JSONObject;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PacketMappingsGenerator implements Generator {

    private static final String MAPPING_FILE = "reports/packets.json";

    private static final ClassName CONNECTION_STATE_CLASS =
            ClassName.get("com.cafiaso.server.network.connection", "ConnectionState");

    private static final ClassName PACKET_MAPPINGS_CLASS =
            ClassName.get("com.cafiaso.server.network.protocol", "PacketMappings");

    private static final String[] CLIENTBOUND_STATES = {
            "status",
            "login",
            "configuration",
            "play"
    };

    private static final String[] SERVERBOUND_STATES = {
            "handshake",
            "status",
            "login",
            "configuration",
            "play"
    };

    @Override
    public void generate() {
        try (InputStream in = ResourceUtils.getResourceAsStream(MAPPING_FILE)) {
            String jsonContent = new String(in.readAllBytes());
            JSONObject packetMappingJson = new JSONObject(jsonContent);

            // Generate clientbound mapping
            CodeBlock.Builder clientboundMappingCodeBlock = CodeBlock.builder()
                    .add("$T.ofEntries(\n", ClassName.get(Map.class));

            for (int s = 0; s < CLIENTBOUND_STATES.length; s++) {
                String state = CLIENTBOUND_STATES[s];
                boolean isLastState = s == CLIENTBOUND_STATES.length - 1;

                CodeBlock.Builder stateMappingBlock = CodeBlock.builder()
                        .add("    $T.entry($T.$L, $T.<$T, $T>ofEntries(\n",
                                ClassName.get(Map.class),
                                CONNECTION_STATE_CLASS,
                                state.toUpperCase(),
                                ClassName.get(Map.class),
                                ClassName.get(String.class),
                                ClassName.get(Integer.class)
                        );

                JSONObject mapping = packetMappingJson.getJSONObject(state).getJSONObject("clientbound");
                List<String> packetNames = new ArrayList<>(mapping.keySet());

                for (int i = 0; i < packetNames.size(); i++) {
                    String packetName = packetNames.get(i);
                    int packetId = mapping.getJSONObject(packetName).getInt("protocol_id");
                    String simpleName = packetName.substring(packetName.lastIndexOf(':') + 1); // Remove namespace

                    stateMappingBlock.add("        $T.entry($S, $L)", ClassName.get(Map.class), simpleName, packetId);
                    if (i < packetNames.size() - 1) {
                        stateMappingBlock.add(",\n");
                    } else {
                        stateMappingBlock.add("\n");
                    }
                }

                stateMappingBlock.add("    ))");
                if (!isLastState) {
                    stateMappingBlock.add(",\n");
                } else {
                    stateMappingBlock.add("\n");
                }

                clientboundMappingCodeBlock.add(stateMappingBlock.build());
            }

            clientboundMappingCodeBlock.add(")");

            // Generate serverbound mapping
            CodeBlock.Builder serverboundMappingCodeBlock = CodeBlock.builder()
                    .add("$T.ofEntries(\n", ClassName.get(Map.class));

            for (int s = 0; s < SERVERBOUND_STATES.length; s++) {
                String state = SERVERBOUND_STATES[s];
                boolean isLastState = s == SERVERBOUND_STATES.length - 1;

                CodeBlock.Builder stateMappingBlock = CodeBlock.builder()
                        .add("    $T.entry($T.$L, $T.<$T, $T>ofEntries(\n",
                                ClassName.get(Map.class),
                                CONNECTION_STATE_CLASS,
                                state.toUpperCase(),
                                ClassName.get(Map.class),
                                ClassName.get(Integer.class),
                                ClassName.get(String.class)
                        );

                JSONObject mapping = packetMappingJson.getJSONObject(state).getJSONObject("serverbound");
                List<String> packetNames = new ArrayList<>(mapping.keySet());

                for (int i = 0; i < packetNames.size(); i++) {
                    String packetName = packetNames.get(i);
                    int packetId = mapping.getJSONObject(packetName).getInt("protocol_id");
                    String simpleName = packetName.substring(packetName.lastIndexOf(':') + 1); // Remove namespace

                    stateMappingBlock.add("        $T.entry($L, $S)", ClassName.get(Map.class), packetId, simpleName);
                    if (i < packetNames.size() - 1) {
                        stateMappingBlock.add(",\n");
                    } else {
                        stateMappingBlock.add("\n");
                    }
                }

                stateMappingBlock.add("    ))");
                if (!isLastState) {
                    stateMappingBlock.add(",\n");
                } else {
                    stateMappingBlock.add("\n");
                }

                serverboundMappingCodeBlock.add(stateMappingBlock.build());
            }

            serverboundMappingCodeBlock.add(")");

            // Create clientbound() method
            MethodSpec clientboundMethod = MethodSpec.methodBuilder("clientbound")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .returns(ParameterizedTypeName.get(
                            ClassName.get(Map.class),
                            CONNECTION_STATE_CLASS,
                            ParameterizedTypeName.get(
                                    ClassName.get(Map.class),
                                    ClassName.get(String.class),
                                    ClassName.get(Integer.class)
                            )
                    ))
                    .addStatement("return CLIENTBOUND_MAPPING")
                    .build();

            // Create serverbound() method
            MethodSpec serverboundMethod = MethodSpec.methodBuilder("serverbound")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .returns(ParameterizedTypeName.get(
                            ClassName.get(Map.class),
                            CONNECTION_STATE_CLASS,
                            ParameterizedTypeName.get(
                                    ClassName.get(Map.class),
                                    ClassName.get(Integer.class),
                                    ClassName.get(String.class)
                            )
                    ))
                    .addStatement("return SERVERBOUND_MAPPING")
                    .build();

            TypeSpec packetMappingsSpec = TypeSpec.classBuilder("MojangPacketMappings")
                    .addJavadoc("This class is auto-generated. Do not edit it.")
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(PACKET_MAPPINGS_CLASS)
                    .addField(
                            FieldSpec.builder(
                                            ParameterizedTypeName.get(
                                                    ClassName.get(Map.class),
                                                    CONNECTION_STATE_CLASS,
                                                    ParameterizedTypeName.get(
                                                            ClassName.get(Map.class),
                                                            ClassName.get(String.class),
                                                            ClassName.get(Integer.class)
                                                    )
                                            ),
                                            "CLIENTBOUND_MAPPING",
                                            Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL
                                    )
                                    .initializer(clientboundMappingCodeBlock.build())
                                    .build()
                    )
                    .addField(
                            FieldSpec.builder(
                                            ParameterizedTypeName.get(
                                                    ClassName.get(Map.class),
                                                    CONNECTION_STATE_CLASS,
                                                    ParameterizedTypeName.get(
                                                            ClassName.get(Map.class),
                                                            ClassName.get(Integer.class),
                                                            ClassName.get(String.class)
                                                    )
                                            ),
                                            "SERVERBOUND_MAPPING",
                                            Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL
                                    )
                                    .initializer(serverboundMappingCodeBlock.build())
                                    .build()
                    )
                    .addMethod(clientboundMethod)
                    .addMethod(serverboundMethod)
                    .build();

            CodeUtils.writeClassToFile(packetMappingsSpec, "network", "protocol");
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate PacketMappings", e);
        }
    }
}
