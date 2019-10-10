package com.itzap.data.api;

public interface ArrayField extends Field {
    int getLength();

    @Override
    default Type getType() {
        return Type.ARRAY;
    }
}
