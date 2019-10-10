package com.itzap.data.file.utils;

import com.itzap.common.exception.IZapException;
import com.itzap.data.api.Field;
import com.itzap.data.file.exceptions.FileErrorCodes;
import com.itzap.data.file.model.FlatField;

import java.nio.ByteBuffer;

public final class FieldUtils {
    private FieldUtils() {}

    public static Number parseBin(FlatField f, byte[] buffer) {
        if (buffer == null || buffer.length == 0) {
            return 0;
        }

        if (f.getType() == Field.Type.BINARY) {
            switch (f.getSize()) {
                case 2:
                    return ByteBuffer.wrap(buffer, f.getOffset(), f.getSize()).getShort();
                case 4:
                    return ByteBuffer.wrap(buffer, f.getOffset(), f.getSize()).getInt();
                case 8:
                    return ByteBuffer.wrap(buffer, f.getOffset(), f.getSize()).getLong();
            }
        }

        throw new IZapException(String.format("Failed to convert data for field [%s]", f),
                FileErrorCodes.FIELD_TYPE_CONVERSION);
    }
}
