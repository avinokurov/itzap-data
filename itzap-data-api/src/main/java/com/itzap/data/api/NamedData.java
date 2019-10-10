package com.itzap.data.api;

import com.itzap.common.Named;

public interface NamedData extends Named {
    enum SourceType implements Named {
        FILE;

        @Override
        public String getName() {
            return name();
        }
    }

    default Named getType() {
        return SourceType.FILE;
    }

    Connection getConnection();

    boolean isValid();
}
