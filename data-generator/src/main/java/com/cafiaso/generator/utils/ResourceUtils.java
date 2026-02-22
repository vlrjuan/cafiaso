package com.cafiaso.generator.utils;

import com.cafiaso.generator.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

public class ResourceUtils {

    public static File getResource(String path) throws IOException {
        URL url = Main.class.getClassLoader().getResource(path);
        if (url == null) {
            throw new FileNotFoundException("Resource folder not found: %s".formatted(path));
        }

        try {
            return new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new IOException("Invalid resource path: %s".formatted(path), e);
        }
    }

    public static InputStream getResourceAsStream(String path) throws IOException {
        InputStream in = Main.class.getClassLoader().getResourceAsStream(path);
        if (in == null) {
            throw new FileNotFoundException("Resource file not found: %s".formatted(path));
        }

        return in;
    }
}
