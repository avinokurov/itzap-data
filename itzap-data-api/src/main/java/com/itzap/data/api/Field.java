package com.itzap.data.api;

import com.itzap.common.Named;

public interface Field extends Named {
    enum Type implements Named {
        TEXT, BINARY, ARRAY;

        @Override
        public String getName() {
            return name();
        }
    }

    int getSize();

    default Named getType() {
        return  Type.TEXT;
    }
}
