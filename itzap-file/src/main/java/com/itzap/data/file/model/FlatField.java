package com.itzap.data.file.model;

import com.itzap.data.api.Field;

public interface FlatField extends Field {
    int getRecordPosition();

    default int getOffset() {
        return 0;
    }

    static FlatFieldImpl.Builder builder() {
        return new FlatFieldImpl.Builder();
    }

    static FlatFieldImpl.Builder from(FlatField field) {
        FlatFieldImpl.Builder builder = builder()
                .setOffset(field.getOffset())
                .setRecordPosition(field.getRecordPosition())
                .setSize(field.getSize());

        if (field instanceof FlatArrayField) {
            return builder.setLength(((FlatArrayField) field).getLength());
        }

        return builder;
    }
}
