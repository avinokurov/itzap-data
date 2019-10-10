package com.itzap.data.file.model;

import com.itzap.common.BuilderInterface;
import com.itzap.data.api.AbstractField;

public class FlatFieldImpl extends AbstractField implements FlatField {
    private final String name;
    private final int offset;
    private final int size;
    private final Type type;
    private final int recordPosition;

    FlatFieldImpl(Builder builder) {
        this.name = builder.name;
        this.offset = builder.offset;
        this.size = builder.size;
        this.type = builder.type;
        this.recordPosition = builder.recordPosition;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public int getRecordPosition() {
        return this.recordPosition;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public int getSize() {
        return size;
    }

    public static class Builder implements BuilderInterface<FlatField> {
        private String name;
        private int offset;
        private int size;
        private Type type = Type.TEXT;
        int length = 0;
        private int recordPosition;

        public Builder setOffset(int offset) {
            this.offset = offset;
            return this;
        }

        public Builder setSize(int size) {
            this.size = size;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setType(Type type) {
            this.type = type;
            return this;
        }

        public Builder setLength(int length) {
            this.length = length;
            return this;
        }

        public Builder setRecordPosition(int recordPosition) {
            this.recordPosition = recordPosition;
            return this;
        }

        @Override
        public FlatField build() {
            if (this.length > 0) {
                return new FlatArrayFieldImpl(this);
            }

            return new FlatFieldImpl(this);
        }
    }
}
