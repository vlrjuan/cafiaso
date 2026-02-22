package com.cafiaso.server.network.protocol;

import com.cafiaso.server.network.registry.PacketBinding;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class ProtocolModule extends AbstractModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProtocolModule.class);

    @Override
    protected void configure() {
        Reflections packetReflections = new Reflections("com.cafiaso.server.network.protocol.packets.serverbound");

        // Map of packet classes to their @Packet annotations
        Map<Class<?>, Packet> packetAnnotations = packetReflections.getTypesAnnotatedWith(Packet.class)
                .stream()
                .collect(Collectors.toMap(Function.identity(), packetClass -> packetClass.getAnnotation(Packet.class)));

        Reflections handlerReflections = new Reflections("com.cafiaso.server.network.protocol.handlers");

        // Map of packet classes to their handler implementations
        Map<Class<?>, Class<? extends PacketHandler<?>>> handlerImplementations =
                handlerReflections.getSubTypesOf(PacketHandler.class)
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        handlerClass -> {
                                            Type[] interfaces = handlerClass.getGenericInterfaces();
                                            if (interfaces.length == 0 || !(interfaces[0] instanceof ParameterizedType)) {
                                                return Object.class;
                                            }

                                            Type arg = ((ParameterizedType) interfaces[0]).getActualTypeArguments()[0];

                                            return (Class<?>) (arg instanceof Class ? arg : Object.class);
                                        },
                                        handlerClass -> (Class<? extends PacketHandler<?>>) handlerClass
                                )
                        );

        @SuppressWarnings("Convert2Diamond")
        Multibinder<PacketBinding<Object>> packetBindingBinder = Multibinder.newSetBinder(
                binder(),
                new TypeLiteral<PacketBinding<Object>>() {}
        );

        for (Map.Entry<Class<?>, Packet> entry : packetAnnotations.entrySet()) {
            Packet packet = entry.getValue();

            Class<?> packetClass = entry.getKey();
            Class<? extends PacketHandler<?>> handlerClass = handlerImplementations.get(packetClass);

            PacketBinding<Object> binding = getBinding(packet, handlerClass);
            packetBindingBinder.addBinding().toInstance(binding);
        }

        LOGGER.info("Registered {} client packets", packetAnnotations.size());
    }

    private <P> PacketBinding<P> getBinding(Packet packet, Class<? extends PacketHandler<?>> handlerClass) {
        return new PacketBinding<>(
                packet.state(),
                packet.resourceId(),
                getProvider((Class<? extends PacketDeserializer<P>>) packet.deserializer()),
                handlerClass == null
                        ? () -> (_, _) -> { }  // No-op handler
                        : getProvider((Class<? extends PacketHandler<P>>) handlerClass)
        );
    }
}
