package com.cafiaso.server.icon;

import com.cafiaso.server.Main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.Optional;

/**
 * A {@link ServerIcon} implementation that loads a PNG icon from the classpath
 * and caches the base64-encoded string representation of the icon.
 */
public class PngCachedServerIcon implements ServerIcon {

    private static final int ICON_SIZE = 64;

    private final String path;

    private String icon;

    public PngCachedServerIcon(String path) {
        this.path = path;
    }

    @Override
    public Optional<String> getData() {
        if (icon != null) {
            return Optional.of(icon);
        }

        URL url = Main.class.getClassLoader().getResource(path);

        if (url == null) {
            return Optional.empty();
        }

        File file = new File(url.getFile());

        try {
            BufferedImage favicon = ImageIO.read(file);

            if (favicon == null || (favicon.getWidth() != ICON_SIZE || favicon.getHeight() != ICON_SIZE)) {
                return Optional.empty();
            }

            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                ImageIO.write(favicon, "png", out);
                byte[] data = out.toByteArray();

                icon = Base64.getEncoder().encodeToString(data);

                return Optional.of(icon);
            }
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
