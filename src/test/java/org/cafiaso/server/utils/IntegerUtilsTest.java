package org.cafiaso.server.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntegerUtilsTest {

    @Test
    void getSize_ShouldReturnCorrectSize() {
        int[] values = {25565, 42, 0};
        int[] sizes = {3, 1, 1};

        for (int i = 0; i < values.length; i++) {
            assertEquals(sizes[i], IntegerUtils.getSerializedLength(values[i]));
        }
    }
}
