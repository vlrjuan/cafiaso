package com.cafiaso.server.network.data.types;

import com.cafiaso.server.network.data.DataType;
import com.cafiaso.server.network.io.NetworkBuffer;

/**
 * A {@link DataType} for unsigned short values.
 * <p>
 * Unsigned shorts are 16-bit unsigned integers.
 * <p>
 * Bounds: [0, 65535]
 * <p>
 * Some examples of unsigned short values and their corresponding representation in decimal:
 * <ul>
 *     <li>0x00 -> 0</li>
 *     <li>0x63 0xdd -> 25565</li>
 * </ul>
 *
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Type:Unsigned_Short">Unsigned Short Type</a>
 */
public class UnsignedShortDataType implements DataType<Integer> {

    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }

    @Override
    public Integer read(NetworkBuffer in) {
        return in.readShort() & 0xffff;
    }

    @Override
    public void write(Integer value, NetworkBuffer out) {
        if (value < 0 || value > 65535) {
            throw new IllegalArgumentException("Value out of range for unsigned short: %d".formatted(value));
        }

        out.writeShort((short) (value & 0xffff));
    }
}
