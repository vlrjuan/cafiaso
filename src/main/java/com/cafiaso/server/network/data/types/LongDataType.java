package com.cafiaso.server.network.data.types;

import com.cafiaso.server.network.data.DataType;
import com.cafiaso.server.network.io.NetworkBuffer;

/**
 * A {@link DataType} for {@link Long} values.
 * <p>
 * Longs are 64-bit signed integers.
 * <p>
 * Bounds: [-9,223,372,036,854,775,808, 9,223,372,036,854,775,807]
 * <p>
 * Some examples of long values and their corresponding representation in decimal:
 * <ul>
 *     <li>0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 -> 0</li>
 *     <li>0x01 0x02 0x03 0x04 0x05 0x06 0x07 0x08 -> 72623859790382856</li>
 * </ul>
 *
 * @see <a href="https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol#Type:Long">Long Type</a>
 */
public class LongDataType implements DataType<Long> {

    @Override
    public Class<Long> getType() {
        return Long.class;
    }

    @Override
    public Long read(NetworkBuffer in) {
        return in.readLong();
    }

    @Override
    public void write(Long value, NetworkBuffer out) {
        out.writeLong(value);
    }
}
