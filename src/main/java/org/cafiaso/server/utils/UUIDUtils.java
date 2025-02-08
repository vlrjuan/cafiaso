package org.cafiaso.server.utils;

import java.util.UUID;

/**
 * Utility class for working with UUIDs.
 */
public class UUIDUtils {

    private static final String UUID_PATTERN = "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})";

    /**
     * Creates a UUID from a string without dashes.
     * <p>
     * Example:
     * <pre>
     *     String undashed = "069a79f444e94726a5befca90e38aaf5";
     *     UUID uuid = UUIDUtils.fromStringWithoutDashes(undashed);
     *     System.out.println(uuid); // Output: 069a79f4-44e9-4726-a5be-fca90e38aaf5
     *   </pre>
     *
     * @param string the string to convert
     * @return the UUID
     */
    public static UUID fromStringWithoutDashes(String string) {
        return UUID.fromString(string.replaceFirst(UUID_PATTERN, "$1-$2-$3-$4-$5"));
    }
}
