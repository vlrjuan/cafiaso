package org.cafiaso.server.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UUIDUtilsTest {

    @Test
    void fromStringWithoutDashes_ShouldReturnUUID() {
        String undashed = "069a79f444e94726a5befca90e38aaf5";
        String result = UUIDUtils.fromStringWithoutDashes(undashed).toString();

        assertEquals("069a79f4-44e9-4726-a5be-fca90e38aaf5", result);
    }
}
