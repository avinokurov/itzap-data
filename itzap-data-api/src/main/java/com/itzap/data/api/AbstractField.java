package com.itzap.data.api;

import com.google.common.base.MoreObjects;

public abstract class AbstractField implements Field {
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", this.getName())
                .add("type", this.getType().getName())
                .add("size", this.getSize())
                .toString();
    }
}
