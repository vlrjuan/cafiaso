package org.cafiaso.server.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HexUtilsTest {

    @Test
    void toHexString_ShouldFormatIntegerToHexString() {
        int n = 255;
        String hex = HexUtils.toHexString(n);
        assertEquals("0xff", hex);
    }
}
